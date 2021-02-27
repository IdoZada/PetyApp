package com.example.pety.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.example.pety.interfaces.InsertDialogInterface;
import com.example.pety.interfaces.OnItemClickListener;
import com.example.pety.objects.Fab;
import com.example.pety.objects.Family;
import com.example.pety.objects.Feed;
import com.example.pety.objects.InsertTimeDialog;
import com.example.pety.objects.Pet;
import com.example.pety.objects.Walk;
import com.example.pety.utils.FirebaseDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class WalkFeedFragment<T> extends Fragment {
    private static final String TAG = "WalkFeedFragment";
    ImageView pet_img_walk_feed;
    TextView pet_title_walk_feed;
    ProgressBar pet_progressbar_walk;
    ProgressBar pet_progressbar_feed;
    ImageView pet_progressbar_img_walk_feed;
    RecyclerView pet_recyclerView_walk_feed;
    ItemWalkFeedAdapter itemWalkFeedAdapter;
    InsertTimeDialog insertTimeDialog;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    ArrayList<T> lists;
    Fab fab = Fab.WALK_FAB;
    Family family;
    Pet pet;
    Context mContext;
    int position;
    int fillProgressBar_feed;
    int fillProgressBar_walk;
    int maxWalkElements;
    int maxFeedElements;


    public WalkFeedFragment(Context context) {
        this.mContext = context;
        lists = new ArrayList<T>();
        itemWalkFeedAdapter = new ItemWalkFeedAdapter(lists, mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walk_feed, container, false);
        findViews(view);
        updateUI(fab);

        if(fab == Fab.WALK_FAB) {
            Collections.sort((ArrayList<Walk>)(T)lists, Walk.myTime);
            Log.d("TAG", "onCreateView: Collections Walk " + lists);
        }else{
            Collections.sort((ArrayList<Feed>)(T)lists, Feed.myTime);
            Log.d("TAG", "onCreateView: Collections Feed " + lists);
        }

        itemWalkFeedAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) { }

            @Override
            public void onItemCareClick(int position, Fab chose_fab) { }

            @Override
            public void onSwitchItemClick(boolean isChecked,int position) {
                T obj = lists.get(position);
                if(obj instanceof Walk){
                    if(isChecked){
                        Log.d("TAG", "onSwitchItemClick: " + isChecked);
                        fillProgressBar_walk++;
                    }else{
                        fillProgressBar_walk--;
                    }
                    pet_progressbar_walk.setMax(lists.size());
                    pet_progressbar_walk.setProgress(fillProgressBar_walk);
                }else{
                    if(isChecked){
                        Log.d("TAG", "onSwitchItemClick: " + isChecked);
                        fillProgressBar_feed++;
                    }else{
                        fillProgressBar_feed--;
                    }
                    pet_progressbar_feed.setMax(lists.size());
                    pet_progressbar_feed.setProgress(fillProgressBar_feed);
                }
                firebaseDB.updateSwitchToDB(family.getFamily_key(),pet.getPet_id(),obj);
            }
        });

        pet_recyclerView_walk_feed.setLayoutManager(new LinearLayoutManager(getActivity()));
        pet_recyclerView_walk_feed.setAdapter(itemWalkFeedAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(pet_recyclerView_walk_feed);

        // Inflate the layout for this fragment
        return view;
    }


    public ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            position = viewHolder.getAdapterPosition();
            T obj = lists.get(position);
            if (direction == ItemTouchHelper.RIGHT) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseDB.deleteTimeFromDB(family.getFamily_key(), pet.getPet_id(), obj);
                                if (obj instanceof Walk) {
                                    Walk walk = (Walk) obj;
                                    pet.getWalks().remove(walk.getId());
                                } else if (obj instanceof Feed) {
                                    Feed feed = (Feed) obj;
                                    pet.getFeeds().remove(feed.getId());
                                }
                                lists.remove(position);
                                itemWalkFeedAdapter.notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemWalkFeedAdapter.notifyItemChanged(position);
                            }
                        })
                        .setMessage("Are you sure you want to delete?")
                        .show();
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("Edit")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                insertTimeDialog.setInsertOrUpdate(InsertTimeDialog.UPDATE, itemWalkFeedAdapter, position);
                                insertTimeDialog.show(getParentFragmentManager(), "Update specific walk");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemWalkFeedAdapter.notifyItemChanged(position);
                            }
                        })
                        .setMessage("Are you sure you want to edit?")
                        .show();
            }
        }
    };

    public void updateUI(Fab fab) {
        Glide.with(getContext()).load(pet.getImage_url()).into(pet_img_walk_feed);
        if (Fab.WALK_FAB == fab) {
            pet_title_walk_feed.setText("Walking");
            pet_progressbar_feed.setVisibility(View.INVISIBLE);
            pet_progressbar_walk.setVisibility(View.VISIBLE);
            pet_progressbar_img_walk_feed.setImageResource(R.drawable.ic_pet_footprint);
        } else {
            pet_title_walk_feed.setText("Feeding");
            pet_progressbar_walk.setVisibility(View.INVISIBLE);
            pet_progressbar_feed.setVisibility(View.VISIBLE);
            pet_progressbar_img_walk_feed.setImageResource(R.drawable.ic_pet_feeding);
        }
        pet_progressbar_walk.setMax(maxWalkElements);
        pet_progressbar_walk.setProgress(fillProgressBar_walk);
        pet_progressbar_feed.setMax(maxFeedElements);
        pet_progressbar_feed.setProgress(fillProgressBar_feed);
    }

    public void setInsertTimeDialog(InsertTimeDialog insertTimeDialog) {
        this.insertTimeDialog = insertTimeDialog;
    }

    public void setPet(Family family, Pet pet, Fab chose_fab) {
        lists.clear();
        fillProgressBar_walk = 0;
        fillProgressBar_feed = 0;
        maxWalkElements = 0;
        maxFeedElements = 0;
        this.fab = chose_fab;
        this.pet = pet;
        this.family = family;

        for (Map.Entry<String, Walk> entry : pet.getWalks().entrySet()) {
            if(entry.getValue().isActive()){
                fillProgressBar_walk++;
                Log.d(TAG, "setPet: WALK_FAB" + fillProgressBar_walk);
            }
            maxWalkElements++;
            if (Fab.WALK_FAB == fab){
                lists.add((T) entry.getValue());
            }
        }

        for (Map.Entry<String, Feed> entry : pet.getFeeds().entrySet()) {
            if (entry.getValue().isActive()) {

                fillProgressBar_feed++;
                Log.d(TAG, "setPet: FEED_FAB" + fillProgressBar_feed);
            }
            maxFeedElements++;
            if (Fab.FEED_FAB == fab){
                lists.add((T) entry.getValue());
            }

        }


//        if (Fab.WALK_FAB == fab) {
//            for (Map.Entry<String, Walk> entry : pet.getWalks().entrySet()) {
//                if(entry.getValue().isActive()){
//                    fillProgressBar_walk++;
//                    Log.d("TAG", "setPet: WALK_FAB" + fillProgressBar_walk);
//                }
//                lists.add((T) entry.getValue());
//            }
//        } else {
//            for (Map.Entry<String, Feed> entry : pet.getFeeds().entrySet()) {
//                if(entry.getValue().isActive()){
//
//                    fillProgressBar_feed++;
//                    Log.d("TAG", "setPet: FEED_FAB" + fillProgressBar_feed);
//                }
//                lists.add((T) entry.getValue());
//            }
//        }

        Log.d(TAG, "setPet: " + lists.size());
        //Log.d("TAG", "setPet: " + family.toString() + "pet " + pet.toString() + "care: " + chose_fab.name());
    }

//    public void updateProgress(int listSize , int progressbar , Fab fab){
//
//        pet_progressbar_walk_feed.setMax(listSize);
//        pet_progressbar_walk_feed.setProgress(progressbar);
//    }

    public void updateWalkItem(String time) {
        Log.d("TAG", "updateWalkItem: " + time);
        T obj = lists.get(position);
        Walk walk = (Walk) obj;

        walk.setTime(time);
        lists.set(position, (T) walk);
        itemWalkFeedAdapter.notifyItemChanged(position);
        Log.d("TAG", "onClick: 2 time " + time);
        pet.getWalks().get(walk.getId()).setTime(time);

        firebaseDB.updateTimeToDB(family.getFamily_key(), pet.getPet_id(), walk);
    }

    public void updateFeedItem(String time) {
        Log.d("TAG", "updateWalkItem: " + time);
        T obj = lists.get(position);
        Feed feed = (Feed) obj;

        feed.setTime(time);
        lists.set(position, (T) feed);
        itemWalkFeedAdapter.notifyItemChanged(position);
        Log.d("TAG", "onClick: 2 time " + time);
        pet.getFeeds().get(feed.getId()).setTime(time);

        firebaseDB.updateTimeToDB(family.getFamily_key(), pet.getPet_id(), feed);
    }

    public void setWalkItem(String time) {
        Map<String, Walk> walkMap;
        Walk walk = new Walk(time, false);

        if (pet.getWalks() == null) {
            walkMap = new HashMap<>();
            pet.setWalks(walkMap);
        }

        firebaseDB.writeNewTimeToDB(pet, family, walk, firebaseDB.WALKS);
        lists.add(0, (T) walk);
        pet_progressbar_walk.setMax(lists.size());
        Log.d("TAG", "setWalkItem: " + lists);
        itemWalkFeedAdapter.notifyItemInserted(0);
    }

    public void setFeedItem(String time) {
        Map<String, Feed> feedMap;
        Feed feed = new Feed(time, false);

        if (pet.getFeeds() == null) {
            feedMap = new HashMap<>();
            pet.setFeeds(feedMap);
        }

        firebaseDB.writeNewTimeToDB(pet, family, feed, firebaseDB.FEEDS);
        lists.add(0, (T) feed);
        pet_progressbar_feed.setMax(lists.size());
        Log.d("TAG", "setFeedItem: " + lists);
        itemWalkFeedAdapter.notifyItemInserted(0);
    }

    public void findViews(View view) {
        pet_img_walk_feed = view.findViewById(R.id.pet_img_walk_feed);
        pet_title_walk_feed = view.findViewById(R.id.pet_title_walk_feed);
        pet_progressbar_walk = view.findViewById(R.id.pet_progressbar_walk);
        pet_progressbar_feed = view.findViewById(R.id.pet_progressbar_feed);
        pet_progressbar_img_walk_feed = view.findViewById(R.id.pet_progressbar_img_walk_feed);
        pet_recyclerView_walk_feed = view.findViewById(R.id.pet_recyclerView_walk_feed);
    }
}