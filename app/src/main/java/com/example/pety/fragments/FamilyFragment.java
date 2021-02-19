package com.example.pety.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pety.R;
import com.example.pety.adapters.ItemFamilyAdapter;
import com.example.pety.interfaces.OnItemClickListener;
import com.example.pety.objects.Family;
import com.example.pety.objects.User;
import com.example.pety.utils.FirebaseDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FamilyFragment extends Fragment  {

    RecyclerView recyclerView;
    ItemFamilyAdapter itemFamilyAdapter;
    ImageView imgProfile;
    View view;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    User currentUser;
    ArrayList<Family> families;
    SendFamilyCallback sendFamilyCallback;
    Context mContext;

    public FamilyFragment(Context context){
        this.mContext = context;
        this.currentUser = new User();
        families = new ArrayList<>();

        Handler handler = new Handler();
        int delay = 1000; //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){
                if(families.size() != currentUser.getFamilies_map().size())//checking if the data is loaded or not
                    handler.postDelayed(this, delay);
            }
        }, delay);

        itemFamilyAdapter = new ItemFamilyAdapter(families, mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_family, container, false);
        findViews(view);

        itemFamilyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Family family = families.get(position);
                sendFamilyCallback.sendFamily(family);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(itemFamilyAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    public ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position  = viewHolder.getAdapterPosition();
            Family family = families.get(position);
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseDB.deleteFamilyFromDB(family,families.get(position).getFamily_key());
                            families.remove(position);
                            itemFamilyAdapter.notifyItemRemoved(position);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            itemFamilyAdapter.notifyItemChanged(position);
                        }
                    })
                    .setMessage("Are you sure you want to delete this family?")
                    .show();
        }
    };

    public interface SendFamilyCallback {
        void sendFamily(Family family);
        void sendUser(User user);
    }

    public void setSendFamilyCallback(SendFamilyCallback sendFamilyCallback){
        this.sendFamilyCallback = sendFamilyCallback;
    }


    public void setItem(String familyName, String imageName, Uri imageUri){
        Family family = new Family(familyName);
        family.setImageUrl(imageUri.toString());

        if(currentUser.getFamilies_map() == null) {
            Map<String,String> families_map = new HashMap<>();
            currentUser.setFamilies_map(families_map);
        }

        firebaseDB.writeNewFamilyToDB(family , currentUser.getFamilies_map(),imageName,imageUri);
        families.add(0,family);
        itemFamilyAdapter.notifyItemInserted(0);
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
        readFamilies(currentUser.getFamilies_map(), families);
        Log.d("RealTimeDatabase", "setCurrentUser: " + user.toString());
    }

    public void readFamilies(Map<String, String> families_map , ArrayList<Family> families){
        firebaseDB.retrieveAllFamilies(families_map,families);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        imgProfile = view.findViewById(R.id.imgProfile);
    }
}
