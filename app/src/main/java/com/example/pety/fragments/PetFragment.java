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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pety.R;
import com.example.pety.adapters.ItemPetAdapter;
import com.example.pety.interfaces.OnItemClickListener;
import com.example.pety.objects.Family;
import com.example.pety.objects.Pet;
import com.example.pety.utils.Converter;
import com.example.pety.utils.FirebaseDB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PetFragment extends Fragment {

    RecyclerView recyclerView;
    ItemPetAdapter itemPetAdapter;
    ArrayList<Pet> pets;
    Family family;
    Context mContext;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    int counter  = 0;

    public PetFragment(Context context){
        this.mContext = context;
        pets = new ArrayList<>();
        itemPetAdapter = new ItemPetAdapter(pets, mContext);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_pet, container, false);
        Log.d("TAG", "onCreateView: pet fragment ");
        findViews(view);

        itemPetAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Family family = families.get(position);
//                sendFamilyCallback.sendFamily(family);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(itemPetAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Inflate the layout for this fragment
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
            Pet pet = pets.get(position);
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            firebaseDB.deleteFamilyFromDB(family,families.get(position).getFamily_key());
//                            families.remove(position);
//                            itemAdapter.notifyItemRemoved(position);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            itemPetAdapter.notifyItemChanged(position);
                        }
                    })
                    .setMessage("Are you sure you want to delete this family?")
                    .show();
        }
    };

    public void displayReceivedData(Family family)
    {
        pets.clear();
        this.family = family;


        // = fromMap(family.getPets().values())
        List<Pet> petList = new ArrayList<Pet>(family.getPets().values());
        for(int i = 0 ; i < petList.size(); i++){
          Pet pet = (Pet) Converter.fromMap((Map<String, Object>) petList.get(i));
          pets.add(pet);
        }
        //pets.addAll(petList);

        Log.d("TAG", "displayReceivedData: " + pets);
    }

//    public Pet fromMap(Map<String, Object> result){
//        Pet pet = new Pet();
//        Object pet_id = result.get("pet_id");
//        Object name = result.get("name");
//        Object image_url = result.get("image_url");
//        Object pet_type = result.get("pet_type");
//        Object birthday = result.get("birthday");
//
//        pet.setPet_id(pet_id.toString());
//        pet.setName(name.toString());
//        pet.setImage_url(image_url.toString());
//        pet.setPet_type(pet_type.toString());
//        pet.setBirthday(birthday.toString());
//
//        return pet;
//    }

    public void setPetItem(String petName, String petType, String birthday, String petImageName, Uri imageUri){
        Log.d("TAG", "setPetItem: " + petName + " " + petType + " " + birthday + " " + petImageName + " " + imageUri);
        Map<String,Pet> pets_map;

        Pet pet = new Pet();
        pet.setName(petName);
        pet.setPet_type(petType);
        pet.setBirthday(birthday);
        pet.setImage_url(imageUri.toString());


        if(family.getPets() == null) {
            pets_map = new HashMap<>();
            family.setPets(pets_map);
        }

        firebaseDB.writeNewPetToDB(pet,family,petImageName,imageUri);
        pets.add(0,pet);
        itemPetAdapter.notifyItemInserted(0);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }
}