package com.example.pety.fragments;

import android.content.Context;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;


public class FamilyFragment extends Fragment  {

    RecyclerView recyclerView;
    //ArrayList<Family> itemData = new ArrayList<>();
    ItemAdapter itemAdapter;
    View view;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    User user;
    ArrayList<Family> families;

    public FamilyFragment(Context context){
//        itemData = MySP.getInstance().readFamiliesFromStorage();
//        Log.d("tttttt", "FamilyFragment: " + itemData);
//        for(int i = 0 ; i < families.size(); i++){
//
//            Log.d("tttttt", "FamilyFragment: " + families.get(i).getF_name());
//        }


        MySP.initialize(context);
        user = MySP.getInstance().readDataFromStorage();

        Log.d("test", "FamilyFragment: " + user.getFamilies_keys().toString());

        families = new ArrayList<>();

        firebaseDB.retrieveAllFamilies(user.getFamilies_keys(),families);
        Handler handler = new Handler();
        int delay = 1000; //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){
                if(families.size() == user.getFamilies_keys().size())//checking if the data is loaded or not
                {
                    //ArrayList<Family> families = MySP.getInstance().readFamiliesFromStorage();
                    for(int i = 0 ; i < families.size(); i++){

                        Log.d("test", "FamilyFragment: " + families.get(i).getF_name());
                    }
                }
                else
                    handler.postDelayed(this, delay);
            }
        }, delay);

        itemAdapter = new ItemAdapter(families, context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("ttttt", "onCreateView: ");
        view = inflater.inflate(R.layout.fragment_family, container, false);
        findViews(view);



        // TODO Loading ProgressBar
//        while(families.size() != user.getFamilies_keys().size()){
//                //visible
//        }

        //invisible


        itemAdapter.setOnItemClickListener(position -> {
            Log.d("ttttt", "onCreateView: ffffff");
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(itemAdapter);
        return view;
    }


    public void setItem(String familyName){
        //Log.d("ttttt", "familyName: " + familyName);
        //R.drawable.ic_baseline_android_24
        //TODO: GLIDE
        Family family = new Family("GLIDE", UUID.randomUUID(),familyName);
        firebaseDB.writeNewFamilyToDB(family , user.getFamilies_keys());
        families.add(0,family);
        itemAdapter.notifyItemInserted(0);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }
}
