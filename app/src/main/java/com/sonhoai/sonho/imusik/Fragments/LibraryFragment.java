package com.sonhoai.sonho.imusik.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sonhoai.sonho.imusik.API.Get;
import com.sonhoai.sonho.imusik.API.Post;
import com.sonhoai.sonho.imusik.Adapters.PlayListAdapter;
import com.sonhoai.sonho.imusik.Adapters.TabAdapter;
import com.sonhoai.sonho.imusik.Constants.Connect;
import com.sonhoai.sonho.imusik.Constants.User;
import com.sonhoai.sonho.imusik.Interface.CallBack;
import com.sonhoai.sonho.imusik.Models.Playlist;
import com.sonhoai.sonho.imusik.R;
import com.sonhoai.sonho.imusik.Util.SharedPreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LibraryFragment extends Fragment {
//    private TabLayout tabLayout;
//    private ViewPager vpForTabLayout;
//    TabAdapter tabAdapter;
private PlayListAdapter playListAdapter;
    private List<Playlist> playlists;
    private RecyclerView rvPlaylist;
    private Button btnCreatePlaylist;
    private ImageView imgNull;
//    private String idUser = SharedPreferencesHelper.getInstance(getContext()).getSharePre("USERINFO", Context.MODE_PRIVATE).getString("idUser", "");
//    private String token = SharedPreferencesHelper.getInstance(getContext()).getSharePre("USERINFO", Context.MODE_PRIVATE).getString("tokenUser", "");
    public LibraryFragment() {
    }

    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        init(view);
        return view;
    }

//    private void init(View v){
////        tabAdapter = new TabAdapter(getChildFragmentManager());
////        tabLayout = v.findViewById(R.id.sliding_tabs);
////        vpForTabLayout = v.findViewById(R.id.viewpager);
//    }

//    private void setTabLayout(){
//        tabAdapter.addFragment(PlayListFragment.newInstance(), "Playlist");
//        tabAdapter.addFragment(HistoryFragment.newInstance(), "History");
//        vpForTabLayout.setAdapter(tabAdapter);
//        vpForTabLayout.setOffscreenPageLimit(2);
//        tabLayout.setupWithViewPager(vpForTabLayout);
//    }

    private void init(View view){
        btnCreatePlaylist = view.findViewById(R.id.btnCreatePlayList);
        rvPlaylist = view.findViewById(R.id.rvPlaylist);
        imgNull = view.findViewById(R.id.imgNull);

        //set list playlist
        playlists = new ArrayList<>();
        playListAdapter = new PlayListAdapter(playlists,getContext());
        rvPlaylist.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        rvPlaylist.setLayoutManager(manager);
        rvPlaylist.setAdapter(playListAdapter);
        //init func
        initFun();
    }
    private void initFun(){
        createPlayList();
        getPlayList();
    }
    private void getPlayList(){
        new Get(new CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i = 0; i < array.length();i++){
                        JSONObject object = array.getJSONObject(i);
                        playlists.add(new Playlist(
                                object.getInt("idPlaylist"),
                                object.getInt("idUser"),
                                object.getString("namePlayList"),
                                object.getString("imagePlaylist"),
                                object.getString("created_date")
                        ));

                    }
                    if(playlists.size()>0){
                        imgNull.setVisibility(View.GONE);
                        rvPlaylist.setVisibility(View.VISIBLE);
                    }
                    playListAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(String result) {

            }
        }).execute("/PlaylistsApi/?list="+ User.getInstance(getContext()).idUser+"&token="+User.getInstance(getContext()).token);
    }

    private void createPlayList(){
        btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.myDialog));
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.dialog_create_playlist, null);
                builder.setView(view);

                final EditText editText = view.findViewById(R.id.edtNamePlayList);

                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a, dd-MM-yyyy");
                        String currentDateTimeString = sdf.format(currentTime);

                        JSONObject object = new JSONObject();
                        try {
                            object.put("idUser", Integer.valueOf(User.getInstance(getContext()).idUser));
                            object.put("namePlaylist", editText.getText().toString());
                            object.put("created_date", currentDateTimeString);
                            object.put("imagePlaylist", Connect.PLAYLIS);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new Post(new CallBack<String>() {
                            @Override
                            public void onSuccess(String result) {
                                if(!result.isEmpty() && result.length() > 0 && result != null){
                                    try {
                                        JSONObject res = new JSONObject(result);
                                        playlists.add(new Playlist(
                                                res.getInt("idPlaylist"),
                                                res.getInt("idUser"),
                                                res.getString("namePlaylist"),
                                                res.getString("imagePlaylist"),
                                                res.getString("created_date")
                                        ));
                                        playListAdapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFail(String result) {

                            }
                        }, object).execute("/PlaylistsApi/");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });
    }
}
