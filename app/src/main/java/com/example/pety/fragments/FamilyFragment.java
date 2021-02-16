package com.example.pety.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pety.R;
import com.example.pety.adapters.ItemAdapter;
import com.example.pety.objects.Family;
import com.example.pety.objects.User;
import com.example.pety.utils.FirebaseDB;
import com.example.pety.utils.MySP;

import java.util.ArrayList;
import java.util.UUID;


public class FamilyFragment extends Fragment  {

    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    View view;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    User user;
    ArrayList<Family> families;
    SendFamilyCallback sendFamilyCallback;

    public FamilyFragment(Context context){
        MySP.initialize(context);
        user = MySP.getInstance().readDataFromStorage();
        families = new ArrayList<>();
        firebaseDB.retrieveAllFamilies(user.getFamilies_keys(),families);

        Handler handler = new Handler();
        int delay = 1000; //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){
                if(families.size() != user.getFamilies_keys().size())//checking if the data is loaded or not
                    handler.postDelayed(this, delay);
            }
        }, delay);

        itemAdapter = new ItemAdapter(families, context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_family, container, false);
        findViews(view);

        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Family family = families.get(position);
                sendFamilyCallback.sendFamily(family);
                //Log.d("TAG", "onCreateView: " + family.getF_name());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(itemAdapter);
        return view;
    }

    public interface SendFamilyCallback {
        void sendFamily(Family family);
    }

    public void setSendFamilyCallback(SendFamilyCallback sendFamilyCallback){
        this.sendFamilyCallback = sendFamilyCallback;
    }


    public void setItem(String familyName, String imageName, Uri imageUri){
        Family family = new Family(familyName);
        family.setImageUrl(imageUri.toString());
        firebaseDB.writeNewFamilyToDB(family , user.getFamilies_keys(),imageName,imageUri);
        families.add(0,family);
        itemAdapter.notifyItemInserted(0);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }
}
