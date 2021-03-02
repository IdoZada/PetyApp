package com.example.pety.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

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
import com.example.pety.interfaces.OnItemClickListener;
import com.example.pety.enums.Fab;
import com.example.pety.interfaces.SendDataCallback;
import com.example.pety.objects.Family;
import com.example.pety.objects.Feed;
import com.example.pety.objects.Pet;
import com.example.pety.objects.Walk;
import com.example.pety.utils.FirebaseDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class WalkFeedFragment<T> extends Fragment {
    private static final String TAG = "WalkFeedFragment";

    int position;
    int fillProgressBar_feed;
    int fillProgressBar_walk;
    int maxWalkElements;
    int maxFeedElements;

    ImageView pet_img_walk_feed;
    TextView pet_title_walk_feed;
    ProgressBar pet_progressbar_walk;
    ProgressBar pet_progressbar_feed;
    ImageView pet_progressbar_img_walk_feed;
    RecyclerView pet_recyclerView_walk_feed;

    ItemWalkFeedAdapter itemWalkFeedAdapter;
    InsertTimeDialog insertTimeDialog;
    SendDataCallback sendDataCallback;


    ArrayList<T> lists;
    Fab fab = Fab.WALK_FAB;
    Pet pet;
    Family family;

    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    Context mContext;

    public WalkFeedFragment(Context context) {
        this.mContext = context;
        lists = new ArrayList<T>();
        itemWalkFeedAdapter = new ItemWalkFeedAdapter(lists, mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walk_feed, container, false);// Inflate the layout for this fragment
        findViews(view);
        updateUI(fab);

        if (fab == Fab.WALK_FAB) {
            Collections.sort((ArrayList<Walk>) (T) lists, Walk.myTime);
        } else {
            Collections.sort((ArrayList<Feed>) (T) lists, Feed.myTime);
        }

        updateSwitchAndProgressBar();

        pet_recyclerView_walk_feed.setLayoutManager(new LinearLayoutManager(getActivity()));
        pet_recyclerView_walk_feed.setAdapter(itemWalkFeedAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(pet_recyclerView_walk_feed);

        return view;
    }

    /**
     * Click on switch to update firebase and top progressBar
     */
    public void updateSwitchAndProgressBar() {
        itemWalkFeedAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onItemCareClick(int position, Fab chose_fab) {
            }

            @Override
            public void onSwitchItemClick(boolean isChecked, int position) {
                T obj = lists.get(position);
                if (obj instanceof Walk) {
                    if (isChecked) {
                        fillProgressBar_walk++;
                    } else {
                        fillProgressBar_walk--;
                    }
                    pet_progressbar_walk.setMax(lists.size());
                    pet_progressbar_walk.setProgress(fillProgressBar_walk);
                    Log.d("TAG", "onSwitchItemClick: " + lists.size());
                    updatePetUI(fillProgressBar_walk , lists.size(), Fab.WALK_FAB);
                    firebaseDB.updateSwitchToDB(family.getFamily_key(), pet.getPet_id(), obj,fillProgressBar_walk,lists.size());
                } else {
                    if (isChecked) {
                        fillProgressBar_feed++;
                    } else {
                        fillProgressBar_feed--;
                    }
                    pet_progressbar_feed.setMax(lists.size());
                    pet_progressbar_feed.setProgress(fillProgressBar_feed);
                    updatePetUI(fillProgressBar_feed , lists.size(), Fab.FEED_FAB);
                    firebaseDB.updateSwitchToDB(family.getFamily_key(), pet.getPet_id(), obj,fillProgressBar_feed,lists.size());
                }
            }
        });
    }

    /**
     * This callback allow to swipe specific time:
     * swipe right allow to delete item time
     * swipe left allow to edit item time
     */
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
                        .setTitle(R.string.delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (obj instanceof Walk) {
                                    Walk walk = (Walk) obj;
                                    if(pet.getWalks().get(walk.getId()).isActive()){
                                        fillProgressBar_walk--;
                                    }
                                    maxWalkElements--;
                                    pet_progressbar_walk.setMax(maxWalkElements);
                                    pet_progressbar_walk.setProgress(fillProgressBar_walk);
                                    pet.getWalks().remove(walk.getId());
                                    updatePetUI(fillProgressBar_walk , maxWalkElements, Fab.WALK_FAB);
                                    firebaseDB.deleteTimeFromDB(family.getFamily_key(), pet.getPet_id(), obj,maxWalkElements,fillProgressBar_walk);
                                } else if (obj instanceof Feed) {
                                    Feed feed = (Feed) obj;
                                    if(pet.getFeeds().get(feed.getId()).isActive()){
                                        fillProgressBar_feed--;
                                    }
                                    maxFeedElements--;
                                    pet_progressbar_feed.setMax(maxFeedElements);
                                    pet_progressbar_feed.setProgress(fillProgressBar_feed);
                                    pet.getFeeds().remove(feed.getId());
                                    updatePetUI(fillProgressBar_feed , maxFeedElements, Fab.FEED_FAB);
                                    firebaseDB.deleteTimeFromDB(family.getFamily_key(), pet.getPet_id(), obj,maxFeedElements,fillProgressBar_feed);
                                }
                                lists.remove(position);
                                itemWalkFeedAdapter.notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemWalkFeedAdapter.notifyItemChanged(position);
                            }
                        })
                        .setMessage(R.string.question_delete)
                        .show();
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.edit)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                insertTimeDialog.setInsertOrUpdate(InsertTimeDialog.UPDATE, itemWalkFeedAdapter, position);
                                insertTimeDialog.show(getParentFragmentManager(), "Update specific walk");
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemWalkFeedAdapter.notifyItemChanged(position);
                            }
                        })
                        .setMessage(R.string.question_edit)
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

    public void updatePetUI(int fillProgressBar , int maxElements , Fab fab){
        sendDataCallback.sendActionPetUi(fillProgressBar,maxElements,fab);
    }

    public void setDataCallback(SendDataCallback sendDataCallback){
        this.sendDataCallback = sendDataCallback;
    }

    public void setInsertTimeDialog(InsertTimeDialog insertTimeDialog) {
        this.insertTimeDialog = insertTimeDialog;
    }

    public void resetSetPet() {
        fillProgressBar_walk = 0;
        fillProgressBar_feed = 0;
        maxWalkElements = 0;
        maxFeedElements = 0;
    }

    /**
     * Read all data about walk/feed of specific pet
     *
     * @param family
     * @param pet
     * @param chose_fab
     */
    public void setPet(Family family, Pet pet, Fab chose_fab) {
        lists.clear();
        resetSetPet();
        this.fab = chose_fab;
        this.pet = pet;
        this.family = family;

        for (Map.Entry<String, Walk> entry : pet.getWalks().entrySet()) {
            if (entry.getValue().isActive()) {
                fillProgressBar_walk++;
            }
            maxWalkElements++;
            if (Fab.WALK_FAB == fab) {
                lists.add((T) entry.getValue());
            }
        }

        for (Map.Entry<String, Feed> entry : pet.getFeeds().entrySet()) {
            if (entry.getValue().isActive()) {
                fillProgressBar_feed++;
            }
            maxFeedElements++;
            if (Fab.FEED_FAB == fab) {
                lists.add((T) entry.getValue());
            }
        }
    }

    /**
     * This method update walk time of specific item
     *
     * @param time
     */
    public void updateWalkItem(String time) {
        T obj = lists.get(position);
        Walk walk = (Walk) obj;
        walk.setTime(time);
        lists.set(position, (T) walk);
        itemWalkFeedAdapter.notifyItemChanged(position);
        pet.getWalks().get(walk.getId()).setTime(time);
        firebaseDB.updateTimeToDB(family.getFamily_key(), pet.getPet_id(), walk);
    }

    /**
     * This method update feed time of specific item
     *
     * @param time
     */
    public void updateFeedItem(String time) {
        T obj = lists.get(position);
        Feed feed = (Feed) obj;
        feed.setTime(time);
        lists.set(position, (T) feed);
        itemWalkFeedAdapter.notifyItemChanged(position);
        pet.getFeeds().get(feed.getId()).setTime(time);
        firebaseDB.updateTimeToDB(family.getFamily_key(), pet.getPet_id(), feed);
    }

    /**
     * This method write new walk time to firebase
     *
     * @param time
     */
    public void setWalkItem(String time) {
        Map<String, Walk> walkMap;
        Walk walk = new Walk(time, false);

        if (pet.getWalks() == null) {
            walkMap = new HashMap<>();
            pet.setWalks(walkMap);
        }

        firebaseDB.writeNewTimeToDB(pet, family, walk, firebaseDB.WALKS);
        lists.add((T) walk);
        pet_progressbar_walk.setMax(lists.size());
        updatePetUI(fillProgressBar_walk,lists.size(),Fab.WALK_FAB);
        itemWalkFeedAdapter.notifyItemInserted(lists.size() - 1);
    }

    /**
     * This method write new feed time to firebase
     *
     * @param time
     */
    public void setFeedItem(String time) {
        Map<String, Feed> feedMap;
        Feed feed = new Feed(time, false);

        if (pet.getFeeds() == null) {
            feedMap = new HashMap<>();
            pet.setFeeds(feedMap);
        }

        firebaseDB.writeNewTimeToDB(pet, family, feed, firebaseDB.FEEDS);
        lists.add((T) feed);
        pet_progressbar_feed.setMax(lists.size());
        updatePetUI(fillProgressBar_feed,lists.size(),Fab.FEED_FAB);
        itemWalkFeedAdapter.notifyItemInserted(lists.size() - 1);
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