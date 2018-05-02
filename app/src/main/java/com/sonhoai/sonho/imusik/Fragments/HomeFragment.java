package com.sonhoai.sonho.imusik.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sonhoai.sonho.imusik.API.Get;
import com.sonhoai.sonho.imusik.Adapters.ForYouAdapter;
import com.sonhoai.sonho.imusik.Adapters.KindAdapter;
import com.sonhoai.sonho.imusik.Adapters.SingerAdapter;
import com.sonhoai.sonho.imusik.Adapters.SongAdapter;
import com.sonhoai.sonho.imusik.Interface.CallBack;
import com.sonhoai.sonho.imusik.Models.ForYou;
import com.sonhoai.sonho.imusik.Models.Kind;
import com.sonhoai.sonho.imusik.Models.Singer;
import com.sonhoai.sonho.imusik.Models.Song;
import com.sonhoai.sonho.imusik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView, rcSinger, rcKind, rcSong;
    private ForYouAdapter forYouAdapter;
    private SongAdapter songAdapter;
    private KindAdapter kindAdapter;
    private List<Singer> singers;
    private List<Kind> kinds;
    private List<Song> songs;
    private SingerAdapter singerAdapter;
    private static HomeFragment kt = null;

    public HomeFragment() {
    }
    public static HomeFragment newInstance() {
        if(kt == null){
            kt = new HomeFragment();
        }
        return kt;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        recyclerView = view.findViewById(R.id.rcForYou);
        rcSinger = view.findViewById(R.id.rcSinger);
        rcKind = view.findViewById(R.id.rcKind);
        rcSong = view.findViewById(R.id.rcNewSong);
        initForYou(view);
        initSinger(view);
        initKind(view);
        initSong(view);
    }

    private void initForYou(View view){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        recyclerView.setLayoutManager(manager);
        forYouAdapter = new ForYouAdapter();
        recyclerView.setAdapter(forYouAdapter);

    }

    private void initSinger(View view){
        singers = new ArrayList<>();
        rcSinger.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        rcSinger.setLayoutManager(manager);
        singerAdapter = new SingerAdapter(singers, getContext());
        rcSinger.setAdapter(singerAdapter);
        //lây ds ca si
        doGetSinger();

    }
    private void doGetSinger(){
        new Get(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    JSONObject object;
                    for(int i = 0; i < array.length();i++){
                        object = array.getJSONObject(i);
                        singers.add(new Singer(
                                object.getInt("idSinger"),
                                object.getString("nameSinger"),
                                object.getString("imageSinger")
                        ));
                    }
                    singerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("AAA", result);
            }

            @Override
            public void onFail(String result) {
                Log.i("AAA", result);
            }
        }).execute("/AuthorsApi");
    }

    private void initKind(View view){
        kinds = new ArrayList<>();
        rcKind.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        rcKind.setLayoutManager(manager);
        kindAdapter = new KindAdapter(kinds);
        rcKind.setAdapter(kindAdapter);
        //lây ds ca si
        doGetKind();
    }

    private void doGetKind() {
        new Get(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    JSONObject object;
                    for(int i = 0; i < array.length();i++){
                        object = array.getJSONObject(i);
                        kinds.add(new Kind(
                                object.getInt("idKind"),
                                object.getString("nameKind")
                        ));
                    }
                    kindAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("AAA", result);
            }

            @Override
            public void onFail(String result) {
                Log.i("AAA", result);
            }
        }).execute("/KindsApi");
    }

    private void initSong(View view){
        songs = new ArrayList<>();
        rcSong.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        rcSong.setLayoutManager(manager);
        songAdapter = new SongAdapter(getContext(), songs, R.layout.item_song);
        rcSong.setAdapter(songAdapter);
        doGetSong();
    }

    private void doGetSong(){
        new Get(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    JSONObject object;
                    for(int i = 0; i < array.length();i++){
                        object = array.getJSONObject(i);
                        songs.add(new Song(
                                object.getInt("idSong"),
                                object.getInt("idKind"),
                                object.getInt("idSinger"),
                                object.getString("nameSong"),
                                object.getString("nameSinger"),
                                object.getString("imageSong"),
                                object.getString("urlSong"),
                                object.getString("luotNghe")
                        ));
                    }
                    songAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("AAA", result);
            }

            @Override
            public void onFail(String result) {
                Log.i("AAA", result);
            }
        }).execute("/SongsApi/?page=0");
    }
}
