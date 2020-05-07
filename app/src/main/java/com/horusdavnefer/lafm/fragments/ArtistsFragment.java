package com.horusdavnefer.lafm.fragments;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
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
import com.horusdavnefer.lafm.db.AppDatabase;
import com.horusdavnefer.lafm.db.AppExecutors;
import com.horusdavnefer.lafm.db.models.Artist;
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

import static com.horusdavnefer.lafm.R.*;

public class ArtistsFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ArtistAdapter adapter;
    List<Artist> artistList = new ArrayList<>();
    RelativeLayout noConexion;
    Button tryAgain;
    Context mContext;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;


    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(layout.artists_fragment,container,false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setPadding(0,20,0,0);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        noConexion = view.findViewById(R.id.noConexion);
        tryAgain = view.findViewById(id.tryAgain);
        tryAgain.setOnClickListener(v -> seachData());
        swipeRefreshLayout.setOnRefreshListener(this::seachData);

        AppExecutors.getInstance().diskIO().execute(() -> {
            artistList = AppDatabase.getInstance(getActivity()).getArtistDao().getAllArtist();
            if (artistList != null && artistList.size() > 0) {
                loadData(artistList);
            } else {
                seachData();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }



    public void loadData(List<Artist> artists){
        AppExecutors.getInstance().mainThread().execute(() -> {
            adapter = new ArtistAdapter(mContext,artists);
            recyclerView.setAdapter(adapter);
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
                            artistList = AppDatabase.getInstance(getActivity()).getArtistDao().getAllArtist();
                            if (artistList != null && artistList.size() > 0) {
                                loadData(artistList);
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
                            List<Artist> searchArtist = AppDatabase.getInstance(mContext).getArtistDao().getSearchArtist(query);
                            loadData(searchArtist);
                        });
                    } else {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            artistList = AppDatabase.getInstance(getActivity()).getArtistDao().getAllArtist();
                            if (artistList != null && artistList.size() > 0) {
                                loadData(artistList);
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
            String url = "https://ws.audioscrobbler.com/2.0/?method=geo.gettopartists&country=spain&api_key="+getString(string.app_keyAPI)+"&format=json";
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
                                JSONObject object = jsonObject.getJSONObject("topartists");
                                JSONArray array = object.getJSONArray("artist");
                                long creationDate = new Date().getTime();


                                for (int i = 0; i < array.length(); i++) {
                                    Artist newartist = new Artist();
                                    JSONObject jsonItem = array.getJSONObject(i);
                                    newartist.setCreationDate(creationDate);
                                    newartist.setName(jsonItem.getString("name"));
                                    newartist.setListeners(jsonItem.getString("listeners"));
                                    newartist.setMbid(jsonItem.getString("mbid"));
                                    newartist.setStreamable(jsonItem.getString("streamable"));
                                    newartist.setUrl(jsonItem.getString("url"));
                                    JSONArray images = jsonItem.getJSONArray("image");
                                    newartist.setImgsmall(images.getJSONObject(0).getString("#text"));
                                    newartist.setImgmedium(images.getJSONObject(1).getString("#text"));
                                    newartist.setImglarge(images.getJSONObject(2).getString("#text"));
                                    newartist.setImgextralarge(images.getJSONObject(3).getString("#text"));
                                    newartist.setImgmega(images.getJSONObject(3).getString("#text"));
                                    creationDate = creationDate + 1;
                                    AppDatabase.getInstance(mContext).getArtistDao().addArtist(newartist);
                                }
                                List<Artist> artistList = AppDatabase.getInstance(mContext).getArtistDao().getAllArtist();
                                loadData(artistList);

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
