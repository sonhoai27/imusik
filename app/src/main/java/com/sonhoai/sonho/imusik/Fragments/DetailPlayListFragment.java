package com.sonhoai.sonho.imusik.Fragments;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonToken;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sonhoai.sonho.imusik.API.Get;
import com.sonhoai.sonho.imusik.Adapters.ForYouAdapter;
import com.sonhoai.sonho.imusik.Adapters.SongAdapter;
import com.sonhoai.sonho.imusik.Constants.User;
import com.sonhoai.sonho.imusik.Interface.CallBack;
import com.sonhoai.sonho.imusik.Models.Song;
import com.sonhoai.sonho.imusik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailPlayListFragment extends DialogFragment {
    private static String idPL = null;
    private static String namePL = null;
    private RecyclerView rcSong;
    private Button btnPlayPlayList;
    private SongAdapter songAdapter;
    private TextView txtNamePlaylist;
    private List<Song> songList;


    public DetailPlayListFragment() {

    }

    public static DetailPlayListFragment newInstance(String id, String name) {
        DetailPlayListFragment fragment = new DetailPlayListFragment();
        Bundle args = new Bundle();
        args.putString("IDPL", id);
        args.putString("NAMEPL", name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void dismiss() {
    }
    @Override
    public boolean isCancelable() {
        return super.isCancelable();
    }

    @Override
    public void setStyle(int style, int theme) {
        super.setStyle(style, theme);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_play_list, container);
        init(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idPL = getArguments().getString("IDPL");
            namePL = getArguments().getString("NAMEPL");
        }
    }

    private void init(View view) {

        rcSong = view.findViewById(R.id.rcSongPL);
        txtNamePlaylist = view.findViewById(R.id.txtNamePlaylist);
        txtNamePlaylist.setText(namePL);

        //init fun
        initFun(view);
    }

    private void initFun(View view) {
        initAdapter(view);
        getSong();
    }

    private void initAdapter(View view){
        songList = new ArrayList<>();
        rcSong.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        rcSong.setLayoutManager(manager);
        songAdapter = new SongAdapter(getContext(), songList, R.layout.item_song_pl);
        rcSong.setAdapter(songAdapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStart() {
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (int) (getResources().getDisplayMetrics().heightPixels);
        Dialog d = getDialog();
        if (d != null) {
            d.getWindow().setLayout(width, height);
            d.getWindow().setGravity(Gravity.BOTTOM);
        }
        super.onStart();
    }

    private void getSong(){
        new Get(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                    if(!result.isEmpty()){
                        try {
                            JSONArray array = new JSONArray(result);
                            Log.i("LLELLELEE", array.length()+"");
                            for (int i = 0; i < array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                songList.add(new Song(
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
                    }
            }

            @Override
            public void onFail(String result) {

            }
        }).execute("/PlaylistsApi/"+idPL+"/?list="+User.getInstance(getContext()).idUser+"&token="+User.getInstance(getContext()).token);
    }
}
