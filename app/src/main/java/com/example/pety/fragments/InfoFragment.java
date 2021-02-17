package com.example.pety.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pety.R;
import com.example.pety.objects.Family;

public class InfoFragment extends Fragment {
    public TextView info_LBL_title;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_info, container, false);
        Log.d("TAG", "onCreateView: info fragment");
        findViews(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void findViews(View view) {

        info_LBL_title = view.findViewById(R.id.info_LBL_title);
    }

}