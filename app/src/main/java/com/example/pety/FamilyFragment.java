//package com.example.pety;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.widget.Toolbar;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.pety.adapters.ItemAdapter;
//import com.example.pety.objects.Family;
//import com.example.pety.objects.InsertDialog;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//import java.util.UUID;
//
//public class FamilyFragment extends Fragment {
//
//    RecyclerView recyclerView;
//    ArrayList<Family> itemData = new ArrayList<>();;
//    FirebaseDatabase database;
//    ItemAdapter itemAdapter;
//    View view;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        setHasOptionsMenu(true);
//        view = inflater.inflate(R.layout.fragment_family,container,false);
//        findViews(view);
//
//
//        ItemAdapter itemAdapter = new ItemAdapter(itemData,getContext());
////        itemAdapter.setOnItemClickListener(position->{
////            Fam winnerPlayer = list.get(position);
////            listener.onGameUserInfoSent(winnerPlayer);
////        });
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(itemAdapter);
//        return view;
//    }
//
//    private void findViews(View view) {
//
//        recyclerView = view.findViewById(R.id.recyclerView);
//    }
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        if (getContext() != null) {
////            MySP shared = new MySP(getContext());
////            list = shared.readDataFromStorage();
////        }
//    }
//
////    @Override
////    public void onAttach(@NonNull Context context) {
////        super.onAttach(context);
////        if( context instanceof FragmentTopTenListener){
////            // if our activity implements this interface
////            listener = (FragmentTopTenListener) context;
////
////        }else{
////            throw new RuntimeException(context.toString() + "must implements FragmentHighScoreListener");
////        }
////    }
//
//
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        //inflater.inflate(R.menu.item_menu,menu);
//        //super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
////        switch (item.getItemId()){
////            case R.id.iconInsert: {
////                InsertDialog dialog = new InsertDialog();
////                dialog.show(getChildFragmentManager(),"Insert Item");
////            }
////        }
//        return super.onOptionsItemSelected(item);
//    }
//
//
////    @Override
////    public void applyTexts(String familyName) {
////        Family family = new Family(R.drawable.ic_baseline_android_24, UUID.randomUUID(),null,familyName);
////        Log.d("ttst", "applyTexts: " + family.getF_name() + " UUID: " + family.getFamily_id());
////        itemData.add(0,family);
////        itemAdapter.notifyItemInserted(0);
////    }
//}
