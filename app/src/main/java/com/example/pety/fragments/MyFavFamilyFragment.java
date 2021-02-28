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

import com.example.pety.R;
import com.example.pety.adapters.ItemMyFavFamilyAdapter;
import com.example.pety.interfaces.OnItemClickListener;
import com.example.pety.objects.Fab;
import com.example.pety.objects.Family;
import com.example.pety.objects.FamilyFlag;
import com.example.pety.utils.FirebaseDB;

import java.util.ArrayList;

public class MyFavFamilyFragment extends Fragment {
    RecyclerView my_fav_family_recyclerView;
    ArrayList<Family> families;
    ItemMyFavFamilyAdapter itemMyFavFamilyAdapter;
    Context mContext;
    FamilyFragment.SendFamilyCallback sendFamilyCallback;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();

    public MyFavFamilyFragment(Context context){
        this.mContext = context;
        families = new ArrayList<>();
        itemMyFavFamilyAdapter = new ItemMyFavFamilyAdapter(families, mContext);
    }


    public void setFamilies(ArrayList<Family> families){
        this.families.clear();
        for (Family family: families) {
            this.families.add(family);
            itemMyFavFamilyAdapter.notifyItemInserted(families.size() - 1);
        }
        Log.d("TAG", "setFamilies: families : " + this.families);
    }

    public void setFamily(Family family){
        this.families.add(family);
        itemMyFavFamilyAdapter.notifyItemInserted(families.size() - 1);
    }

    public void removeFamily(int position){
        this.families.remove(position);
        itemMyFavFamilyAdapter.notifyItemRemoved(position);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_fav_family, container, false);
        findViews(view);

        itemMyFavFamilyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Family family = families.get(position);
                sendFamilyCallback.sendFamily(family, FamilyFlag.SEND_TO_PET_HOME_FRAGMENT);
                firebaseDB.updateDefaultFamily(family.getFamily_key());

            }

            @Override
            public void onItemCareClick(int position, Fab chose_fab) { }

            @Override
            public void onSwitchItemClick(boolean isChecked, int position) { }
        });

        my_fav_family_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        my_fav_family_recyclerView.setAdapter(itemMyFavFamilyAdapter);
        // Inflate the layout for this fragment
        return view;
    }

    public void setSendFamilyCallback(FamilyFragment.SendFamilyCallback sendFamilyCallback){
        this.sendFamilyCallback = sendFamilyCallback;
    }


    public void findViews(View view){
        my_fav_family_recyclerView = view.findViewById(R.id.my_fav_family_recyclerView);
    }
}