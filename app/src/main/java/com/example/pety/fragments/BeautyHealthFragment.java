package com.example.pety.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pety.R;
import com.example.pety.adapters.ItemBeautyHealthAdapter;
import com.example.pety.interfaces.OnItemClickListener;
import com.example.pety.objects.Beauty;
import com.example.pety.objects.Fab;
import com.example.pety.objects.Family;
import com.example.pety.objects.Feed;
import com.example.pety.objects.Health;
import com.example.pety.objects.InsertTimeDateDialog;
import com.example.pety.objects.InsertTimeDialog;
import com.example.pety.objects.Pet;
import com.example.pety.objects.Walk;
import com.example.pety.utils.FirebaseDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class BeautyHealthFragment<T> extends Fragment {
    ImageView pet_img_beauty_health;
    TextView pet_title_beauty_health;
    ProgressBar pet_progressbar_health;
    ProgressBar pet_progressbar_beauty;
    ImageView pet_progressbar_img_beauty_health;
    RecyclerView pet_recyclerView_beauty_health;
    ItemBeautyHealthAdapter itemBeautyHealthAdapter;
    InsertTimeDateDialog insertTimeDateDialog;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    ArrayList<T> lists;
    Fab fab = Fab.BEAUTY_FAB;
    Family family;
    Pet pet;
    Context mContext;
    int position;
    int fillProgressBar_beauty;
    int fillProgressBar_health;
    int maxBeautyElements;
    int maxHealthElements;

    public BeautyHealthFragment(Context context) {
        this.mContext = context;
        lists = new ArrayList<T>();
        itemBeautyHealthAdapter = new ItemBeautyHealthAdapter(lists, mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beauty_health, container, false);
        findViews(view);
        updateUI(fab);
//        fillProgressBar = 3;
        if(fab == Fab.BEAUTY_FAB) {
            Collections.sort((ArrayList<Beauty>)(T)lists, Beauty.myTimeDate);
            Log.d("TAG", "onCreateView: Collections Beauty " + lists);
        }else{
            Collections.sort((ArrayList<Health>)(T)lists, Health.myTimeDate);
            Log.d("TAG", "onCreateView: Collections Health " + lists);
        }

        itemBeautyHealthAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) { }

            @Override
            public void onItemCareClick(int position, Fab chose_fab) { }

            @Override
            public void onSwitchItemClick(boolean isChecked,int position) {
                T obj = lists.get(position);
                if(obj instanceof Beauty){
                    if(isChecked){
                        Log.d("TAG", "onSwitchItemClick: " + isChecked);
                        fillProgressBar_beauty++;
                    }else{
                        fillProgressBar_beauty--;
                    }
                    pet_progressbar_beauty.setMax(lists.size());
                    pet_progressbar_beauty.setProgress(fillProgressBar_beauty);
                }else{
                    if(isChecked){
                        Log.d("TAG", "onSwitchItemClick: " + isChecked);
                        fillProgressBar_health++;
                    }else{
                        fillProgressBar_health--;
                    }
                    pet_progressbar_health.setMax(lists.size());
                    pet_progressbar_health.setProgress(fillProgressBar_health);
                }
                firebaseDB.updateSwitchToDB(family.getFamily_key(),pet.getPet_id(),obj);
            }
        });

        pet_recyclerView_beauty_health.setLayoutManager(new LinearLayoutManager(getActivity()));
        pet_recyclerView_beauty_health.setAdapter(itemBeautyHealthAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(pet_recyclerView_beauty_health);

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
                                if (obj instanceof Beauty) {
                                    Beauty beauty = (Beauty) obj;
                                    pet.getBeauty().remove(beauty.getId());
                                } else if (obj instanceof Health) {
                                    Health health = (Health) obj;
                                    pet.getHealth().remove(health.getId());
                                }
                                lists.remove(position);
                                itemBeautyHealthAdapter.notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemBeautyHealthAdapter.notifyItemChanged(position);
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
                                insertTimeDateDialog.setInsertOrUpdate(InsertTimeDialog.UPDATE, itemBeautyHealthAdapter, position);
                                insertTimeDateDialog.show(getParentFragmentManager(), "Update specific beauty");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemBeautyHealthAdapter.notifyItemChanged(position);
                            }
                        })
                        .setMessage("Are you sure you want to edit?")
                        .show();
            }
        }
    };

    public void updateUI(Fab fab) {
        Glide.with(getContext()).load(pet.getImage_url()).into(pet_img_beauty_health);
        if (Fab.BEAUTY_FAB == fab) {
            pet_title_beauty_health.setText("Beauty");
            pet_progressbar_health.setVisibility(View.INVISIBLE);
            pet_progressbar_beauty.setVisibility(View.VISIBLE);
            pet_progressbar_img_beauty_health.setImageResource(R.drawable.ic_pet_beauty_care);
        } else {
            pet_title_beauty_health.setText("Health");
            pet_progressbar_beauty.setVisibility(View.INVISIBLE);
            pet_progressbar_health.setVisibility(View.VISIBLE);
            pet_progressbar_img_beauty_health.setImageResource(R.drawable.ic_pet_health);
        }
        pet_progressbar_beauty.setMax(maxBeautyElements);
        pet_progressbar_beauty.setProgress(fillProgressBar_beauty);
        pet_progressbar_health.setMax(maxHealthElements);
        pet_progressbar_health.setProgress(fillProgressBar_health);
    }

    public void setInsertTimeDateDialog(InsertTimeDateDialog insertTimeDateDialog) {
        this.insertTimeDateDialog = insertTimeDateDialog;
    }

    public void setPet(Family family, Pet pet, Fab chose_fab) {
        lists.clear();
        fillProgressBar_beauty = 0;
        fillProgressBar_health = 0;
        maxBeautyElements = 0;
        maxHealthElements = 0;
        this.fab = chose_fab;
        this.pet = pet;
        this.family = family;

        for (Map.Entry<String, Beauty> entry : pet.getBeauty().entrySet()) {
            if(entry.getValue().isActive()){
                fillProgressBar_beauty++;
                Log.d("TAG", "setPet: WALK_FAB" + fillProgressBar_beauty);
            }
            maxBeautyElements++;
            if (Fab.BEAUTY_FAB == fab){
                lists.add((T) entry.getValue());
            }
        }

        for (Map.Entry<String, Health> entry : pet.getHealth().entrySet()) {
            if (entry.getValue().isActive()) {

                fillProgressBar_health++;
                Log.d("TAG", "setPet: FEED_FAB" + fillProgressBar_health);
            }
            maxHealthElements++;
            if (Fab.HEALTH_FAB == fab){
                lists.add((T) entry.getValue());
            }

        }
    }

    public void updateBeautyItem(String time) {
        Log.d("TAG", "updateBeautyItem: " + time);
        T obj = lists.get(position);
        Beauty beauty = (Beauty) obj;

        beauty.setTimeDate(time);
        lists.set(position, (T) beauty);
        itemBeautyHealthAdapter.notifyItemChanged(position);
        Log.d("TAG", "onClick: 2 time " + time);
        pet.getBeauty().get(beauty.getId()).setTimeDate(time);

        firebaseDB.updateTimeToDB(family.getFamily_key(), pet.getPet_id(), beauty);
    }

    public void updateHealthItem(String time) {
        Log.d("TAG", "updateHealthItem: " + time);
        T obj = lists.get(position);
        Health health = (Health) obj;

        health.setTimeDate(time);
        lists.set(position, (T) health);
        itemBeautyHealthAdapter.notifyItemChanged(position);
        Log.d("TAG", "onClick: 2 time " + time);
        pet.getHealth().get(health.getId()).setTimeDate(time);

        firebaseDB.updateTimeToDB(family.getFamily_key(), pet.getPet_id(), health);
    }

    public void setBeautyItem(String time) {
        Map<String, Beauty> beautyMap;
        Beauty beauty = new Beauty(time, false);

        if (pet.getBeauty() == null) {
            beautyMap = new HashMap<>();
            pet.setBeauty(beautyMap);
        }

        firebaseDB.writeNewTimeToDB(pet, family, beauty, firebaseDB.BEAUTY);
        lists.add(0, (T) beauty);
        pet_progressbar_beauty.setMax(lists.size());
        //pet_progressbar_beauty_health.setMax(lists.size());
        Log.d("TAG", "setBeautyItem: " + lists);
        itemBeautyHealthAdapter.notifyItemInserted(0);
    }

    public void setHealthItem(String time) {
        Map<String, Health> healthMap;
        Health health = new Health(time, false);

        if (pet.getHealth() == null) {
            healthMap = new HashMap<>();
            pet.setHealth(healthMap);
        }

        firebaseDB.writeNewTimeToDB(pet, family, health, firebaseDB.HEALTH);
        lists.add(0, (T) health);
        pet_progressbar_health.setMax(lists.size());
        Log.d("TAG", "setHealthItem: " + lists);
        itemBeautyHealthAdapter.notifyItemInserted(0);
    }

    public void findViews(View view) {
        pet_img_beauty_health = view.findViewById(R.id.pet_img_beauty_health);
        pet_title_beauty_health = view.findViewById(R.id.pet_title_beauty_health);
        pet_progressbar_beauty = view.findViewById(R.id.pet_progressbar_beauty);
        pet_progressbar_health = view.findViewById(R.id.pet_progressbar_health);
        pet_progressbar_img_beauty_health = view.findViewById(R.id.pet_progressbar_img_beauty_health);
        pet_recyclerView_beauty_health = view.findViewById(R.id.pet_recyclerView_beauty_health);
    }
}