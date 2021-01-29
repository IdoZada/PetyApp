package com.example.pety.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pety.R;
import com.example.pety.adapters.ItemAdapter;
import com.example.pety.objects.Family;
import com.example.pety.objects.InsertDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;


public class HomeFragment extends Fragment  {

    RecyclerView recyclerView;
    //FloatingActionButton fab_button;
    ArrayList<Family> itemData = new ArrayList<>();
    FirebaseDatabase database;
    ItemAdapter itemAdapter;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        findViews(view);


        itemAdapter = new ItemAdapter(itemData, getContext());
        itemAdapter.setOnItemClickListener(position -> {
            Log.d("ttttt", "onCreateView: ffffff");
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(itemAdapter);
        return view;
    }

//    @Override
//    public void applyTexts(String familyName) {
//        Family family = new Family(R.drawable.ic_baseline_android_24, UUID.randomUUID(), null, familyName);
//        Log.d("ttst", "applyTexts: " + family.getF_name() + " UUID: " + family.getFamily_id());
//        itemData.add(0, family);
//        itemAdapter.notifyItemInserted(0);
//    }


    public void setItem(String familyName){
        Family family = new Family(R.drawable.ic_baseline_android_24, UUID.randomUUID(),null,familyName);
        itemData.add(0,family);
        itemAdapter.notifyItemInserted(0);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }
}