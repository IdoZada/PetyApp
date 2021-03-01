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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pety.R;
import com.example.pety.adapters.ItemPetAdapter;
import com.example.pety.interfaces.OnItemClickListener;
import com.example.pety.enums.Fab;
import com.example.pety.interfaces.SendDataCallback;
import com.example.pety.objects.Family;
import com.example.pety.objects.Pet;
import com.example.pety.utils.FirebaseDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PetFragment extends Fragment {

    RecyclerView recyclerView;
    ItemPetAdapter itemPetAdapter;
    ArrayList<Pet> pets;
    Family family;
    Context mContext;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    SendDataCallback sendDataCallback;


    public PetFragment(Context context) {
        this.mContext = context;
        pets = new ArrayList<>();
        family = new Family();
        itemPetAdapter = new ItemPetAdapter(pets, mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet, container, false);// Inflate the layout for this fragment
        findViews(view);

        moveToPetAction();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(itemPetAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    /**
     * Move to one action (Walking,Feeding,Beauty,Health)
     */
    public void moveToPetAction() {
        itemPetAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onItemCareClick(int position, Fab chose_fab) {
                Pet pet = pets.get(position);
                sendDataCallback.sendPet(family, pet, chose_fab);
            }

            @Override
            public void onSwitchItemClick(boolean isChecked, int position) {

            }
        });
    }

    /**
     * This callback allow to swipe specific pet:
     * swipe right allow to delete pet
     */
    public ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Pet pet = pets.get(position);

            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.delete)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseDB.deletePetFromDB(pet, family.getFamily_key());
                            family.getPets().remove(pet.getPet_id());
                            pets.remove(position);
                            sendDataCallback.sendFamily(family, null);
                            itemPetAdapter.notifyItemRemoved(position);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            itemPetAdapter.notifyItemChanged(position);
                        }
                    })
                    .setMessage(R.string.question_delete_family)
                    .show();
        }
    };

    public void setDataCallback(SendDataCallback sendDataCallback) {
        this.sendDataCallback = sendDataCallback;
    }

    public void displayReceivedData(Family family) {
        pets.clear();
        this.family = family;
        for (Map.Entry<String, Pet> entry : family.getPets().entrySet()) {
            pets.add(entry.getValue());
        }
    }

    public void setPetItem(String petName, String petType, String birthday, String petImageName, Uri imageUri) {
        Map<String, Pet> pets_map;
        Pet pet = new Pet();
        pet.setName(petName);
        pet.setPet_type(petType);
        pet.setBirthday(birthday);
        pet.setImage_url(imageUri.toString());

        if (family.getPets() == null) {
            pets_map = new HashMap<>();
            family.setPets(pets_map);
        }
        firebaseDB.writeNewPetToDB(pet, this.family, petImageName, imageUri);
        pets.add(pet);
        itemPetAdapter.notifyItemInserted(pets.size() - 1);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }
}