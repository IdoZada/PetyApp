package com.example.pety.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pety.R;
import com.example.pety.adapters.ItemWalkFeedAdapter;
import com.example.pety.objects.Fab;
import com.example.pety.objects.Family;
import com.example.pety.objects.Feed;
import com.example.pety.objects.Pet;
import com.example.pety.objects.Walk;
import com.example.pety.utils.FirebaseDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WalkFeedFragment<T> extends Fragment {
    ImageView pet_img_walk_feed;
    TextView pet_title_walk_feed;
    ProgressBar pet_progressbar_walk_feed;
    ImageView pet_progressbar_img_walk_feed;
    RecyclerView pet_recyclerView_walk_feed;
    ItemWalkFeedAdapter itemWalkFeedAdapter;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    ArrayList<T> lists;
    Fab fab = Fab.WALK_FAB;
    Family family;
    Pet pet;
    Context mContext;

    public WalkFeedFragment(Context context){
        this.mContext = context;
        lists = new ArrayList<T>();
        itemWalkFeedAdapter = new ItemWalkFeedAdapter(lists, mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walk_feed, container, false);
        findViews(view);
        updateUI(fab);

        pet_recyclerView_walk_feed.setLayoutManager(new LinearLayoutManager(getActivity()));
        pet_recyclerView_walk_feed.setAdapter(itemWalkFeedAdapter);
        // Inflate the layout for this fragment
        return view;
    }

    public void updateUI(Fab fab){
        Glide.with(getContext()).load(pet.getImage_url()).into(pet_img_walk_feed);
        if(Fab.WALK_FAB == fab){
            pet_title_walk_feed.setText("Walking");
            pet_progressbar_img_walk_feed.setImageResource(R.drawable.ic_pet_footprint);
        }else{
            pet_title_walk_feed.setText("Feeding");
            pet_progressbar_img_walk_feed.setImageResource(R.drawable.ic_pet_feeding);
        }
    }

    public void setPet(Family family, Pet pet, Fab chose_fab){
        lists.clear();
        this.fab = chose_fab;
        this.pet = pet;
        this.family = family;

        if(Fab.WALK_FAB == fab){
            for (Map.Entry<String,Walk> entry: pet.getWalks().entrySet()){
                lists.add((T) entry.getValue());
            }
        }else{
            for (Map.Entry<String,Feed> entry: pet.getFeeds().entrySet()){
                lists.add((T) entry.getValue());
            }
        }

        Log.d("TAG", "setPet: " + family.toString() + "pet " + pet.toString() + "care: " + chose_fab.name());
    }

    public void setWalkItem(String time){
        Map<String,Walk> walkMap;
        Walk walk = new Walk(time,false);

        if(pet.getWalks() == null) {
            walkMap = new HashMap<>();
            pet.setWalks(walkMap);
        }

        firebaseDB.writeNewWalkTimeToDB(pet,family,walk);
        lists.add(0,(T) walk);
        Log.d("TAG", "setWalkItem: " + lists);
        itemWalkFeedAdapter.notifyItemInserted(0);
    }

    public void setFeedItem(String time){
        Map<String,Feed> feedMap;
        Feed feed = new Feed(time,false);

        if(pet.getFeeds() == null) {
            feedMap = new HashMap<>();
            pet.setFeeds(feedMap);
        }
        //TODO FirebaseDB.writeNewFeed(Walk walk,Pet pet --> this.pet)
        pet.getFeeds().put("S0002",feed);
        lists.add(0,(T) feed);
        Log.d("TAG", "setFeedItem: " + lists);
        itemWalkFeedAdapter.notifyItemInserted(0);
    }

    public void findViews(View view){
        pet_img_walk_feed = view.findViewById(R.id.pet_img_walk_feed);
        pet_title_walk_feed = view.findViewById(R.id.pet_title_walk_feed);
        pet_progressbar_walk_feed = view.findViewById(R.id.pet_progressbar_walk_feed);
        pet_progressbar_img_walk_feed = view.findViewById(R.id.pet_progressbar_img_walk_feed);
        pet_recyclerView_walk_feed = view.findViewById(R.id.pet_recyclerView_walk_feed);
    }
}