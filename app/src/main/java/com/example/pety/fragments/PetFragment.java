package com.example.pety.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pety.R;
import com.example.pety.objects.Family;

public class PetFragment extends Fragment {
    public TextView pet_LBL_title;
    Family family;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_pet, container, false);
        Log.d("TAG", "onCreateView: pet fragment ");
        findViews(view);

        pet_LBL_title.setText(family.getF_name());
        // Inflate the layout for this fragment
        return view;
    }

    private void findViews(View view) {
        pet_LBL_title = view.findViewById(R.id.pet_LBL_title);
    }


    public void displayReceivedData(Family family)
    {
        this.family = family;
        Log.d("TAG", "displayReceivedData: 11111   " + family.getF_name());
    }
}