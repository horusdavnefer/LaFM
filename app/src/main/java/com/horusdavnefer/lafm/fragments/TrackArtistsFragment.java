package com.horusdavnefer.lafm.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.horusdavnefer.lafm.R;
import com.horusdavnefer.lafm.adapters.ArtistAdapter;
import com.horusdavnefer.lafm.adapters.TrackAdapter;
import com.horusdavnefer.lafm.db.AppDatabase;
import com.horusdavnefer.lafm.db.AppExecutors;
import com.horusdavnefer.lafm.db.models.Artist;
import com.horusdavnefer.lafm.db.models.Track;
import com.horusdavnefer.lafm.utils.UtilsFM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrackArtistsFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TrackAdapter adapter;
    private List<Track> trackList = new ArrayList<>();
    private RelativeLayout noConexion;
    private Button tryAgain;
    private Context mContext;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tracks_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progressBar = view.findViewById(R.id.progress);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setPadding(0,20,0,0);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        noConexion = view.findViewById(R.id.noConexion);
        tryAgain = view.findViewById(R.id.tryAgain);
        tryAgain.setOnClickListener(v -> seachData());
        swipeRefreshLayout.setOnRefreshListener(this::seachData);

        AppExecutors.getInstance().diskIO().execute(() -> {
            trackList = AppDatabase.getInstance(getActivity()).getTrackDao().getAllTrack();
            if (trackList != null && trackList.size() > 0) {
                loadData(trackList);
            } else {
                seachData();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public void loadData(List<Track> tracks){
        AppExecutors.getInstance().mainThread().execute(() -> {
            adapter = new TrackAdapter(mContext,tracks);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.equals("")) {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            trackList = AppDatabase.getInstance(getActivity()).getTrackDao().getAllTrack();
                            if (trackList != null && trackList.size() > 0) {
                                loadData(trackList);
                            } else {
                                seachData();
                            }
                        });
                    }
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    if (query != null) {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            List<Track> searchTrack = AppDatabase.getInstance(mContext).getTrackDao().getSearchTrack(query);
                            loadData(searchTrack);
                        });
                    } else {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            trackList = AppDatabase.getInstance(getActivity()).getTrackDao().getAllTrack();
                            if (trackList != null && trackList.size() > 0) {
                                loadData(trackList);
                            } else {
                                seachData();
                            }
                        });
                    }
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    public void seachData() {
        if (UtilsFM.isConnectInternet(mContext)){
            String url = "https://ws.audioscrobbler.com/2.0/?method=geo.gettoptracks&country=spain&api_key="+getString(R.string.app_keyAPI)+"&format=json";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    AppExecutors.getInstance().mainThread().execute(() -> noConexion.setVisibility(View.GONE));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        AppExecutors.getInstance().mainThread().execute(() -> noConexion.setVisibility(View.GONE));
                        String respuesta = response.body().string();
                        AppExecutors.getInstance().diskIO().execute(() -> {

                            try {
                                JSONObject jsonObject = new JSONObject(respuesta);
                                JSONObject object = jsonObject.getJSONObject("tracks");
                                JSONArray array = object.getJSONArray("track");
                                long creationDate = new Date().getTime();


                                for (int i = 0; i < array.length(); i++) {
                                    Track newTrack = new Track();
                                    JSONObject jsonItem = array.getJSONObject(i);
                                    newTrack.setCreationDate(creationDate);
                                    newTrack.setName(jsonItem.getString("name"));
                                    newTrack.setDuration(Long.parseLong(jsonItem.getString("duration")));
                                    newTrack.setListeners(jsonItem.getString("listeners"));
                                    newTrack.setMbid(jsonItem.getString("mbid"));
                                    newTrack.setUrl(jsonItem.getString("url"));
                                    JSONArray images = jsonItem.getJSONArray("image");
                                    newTrack.setImgsmall(images.getJSONObject(0).getString("#text"));
                                    newTrack.setImgmedium(images.getJSONObject(1).getString("#text"));
                                    newTrack.setImglarge(images.getJSONObject(2).getString("#text"));
                                    newTrack.setImgextralarge(images.getJSONObject(3).getString("#text"));
                                    newTrack.setImgmega(images.getJSONObject(3).getString("#text"));
                                    JSONObject artist = jsonItem.getJSONObject("artist");
                                    newTrack.setNameartist(artist.getString("name"));
                                    newTrack.setMbidartist(artist.getString("mbid"));
                                    newTrack.setUrlartist(artist.getString("url"));
                                    JSONObject rank = jsonItem.getJSONObject("@attr");
                                    newTrack.setRank(rank.getString("rank"));
                                    creationDate = creationDate + 1;
                                    AppDatabase.getInstance(mContext).getTrackDao().addTrack(newTrack);
                                }
                                List<Track> trackList = AppDatabase.getInstance(mContext).getTrackDao().getAllTrack();
                                loadData(trackList);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                AppExecutors.getInstance().mainThread().execute(() -> {
                                    noConexion.setVisibility(View.VISIBLE);
                                });
                            }
                        });

                    }
                }
            });
        } else {
            AppExecutors.getInstance().mainThread().execute(() -> noConexion.setVisibility(View.VISIBLE));
        }
    }
}
