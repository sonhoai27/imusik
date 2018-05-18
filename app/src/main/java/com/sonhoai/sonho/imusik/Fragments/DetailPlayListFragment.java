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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sonhoai.sonho.imusik.API.Get;
import com.sonhoai.sonho.imusik.Adapters.DetailPlayListAdapter;
import com.sonhoai.sonho.imusik.Adapters.SongAdapter;
import com.sonhoai.sonho.imusik.Interface.CallBack;
import com.sonhoai.sonho.imusik.MainActivity;
import com.sonhoai.sonho.imusik.Models.DetailPlayList;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.PlayerHelper;
import com.sonhoai.sonho.imusik.Util.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailPlayListFragment extends DialogFragment {
    public static String idPL = null;
    private static String namePL = null;
    private RecyclerView rcSong;
    private Button btnPlayPlayList;
    private DetailPlayListAdapter detailPlayListAdapter;
    private TextView txtNamePlaylist;
    private List<DetailPlayList> songList;


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
        btnPlayPlayList = view.findViewById(R.id.btnPlayPlayList);
        txtNamePlaylist.setText(namePL);

        //init fun
        initFun(view);
    }

    private void initFun(View view) {
        initAdapter(view);
        getSong();
        playList();
    }

    private void playList() {
        btnPlayPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songList.size()>0){
                    SongAdapter.helper.addPlayList(songList);
                    songViewBar();
                }
                Toast.makeText(getContext(), "Playing", Toast.LENGTH_SHORT).show();
                Log.i("KAJAAA", String.valueOf(SongAdapter.helper.getSongList()));
            }
        });
    }
    private void songViewBar(){
        if(SongAdapter.helper.getCurrentSong() == null || SongAdapter.helper  == null){
            return;
        }
        MainActivity.txtNameCurrentSong.setText(SongAdapter.helper.getCurrentSong().getNameSong());
        MainActivity.imgPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
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
        detailPlayListAdapter = new DetailPlayListAdapter(getContext(), songList);
        rcSong.setAdapter(detailPlayListAdapter);
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
                                songList.add(new DetailPlayList(
                                        object.getInt("idDetail"),
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
                            detailPlayListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
            }

            @Override
            public void onFail(String result) {

            }
        }).execute("/PlaylistsApi/"+idPL+"/?list="+ SharedPreferencesHelper.getInstance(getContext()).getIdUser()+"&token="+ SharedPreferencesHelper.getInstance(getContext()).getToken());
    }
}
