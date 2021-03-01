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
import com.example.pety.enums.Fab;
import com.example.pety.interfaces.SendDataCallback;
import com.example.pety.objects.Family;
import com.example.pety.enums.FamilyFlag;
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

    ArrayList<Family> families;
    User currentUser;
    SendDataCallback sendDataCallback;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();
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
        clickFamily();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(itemFamilyAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    /**
     * This callback allow to swipe specific family:
     * swipe right allow to delete family
     *
     */
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
                            currentUser.getFamilies_map().remove(families.get(position).getFamily_key());
                            families.remove(position);
                            sendDataCallback.sendFamilies(families);
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

    /**
     * This click listener communicate with pet fragment
     */
    public void clickFamily(){
        itemFamilyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Family family = families.get(position);
                sendDataCallback.sendFamily(family,FamilyFlag.SEND_TO_PET_FRAGMENT);
            }

            @Override
            public void onItemCareClick(int position, Fab chose_fab) {

            }

            @Override
            public void onSwitchItemClick(boolean isChecked, int position) {

            }
        });
    }

    public void setDataCallback(SendDataCallback sendDataCallback){
        this.sendDataCallback = sendDataCallback;
    }

    /**
     * This method add new family to firebase
     * @param familyName name of new family
     * @param imageName name of new image
     * @param imageUri path of new image
     */
    public void setItem(String familyName, String imageName, Uri imageUri){
        Family family = new Family(familyName);
        family.setImageUrl(imageUri.toString());

        if(currentUser.getFamilies_map() == null) {
            Map<String,String> families_map = new HashMap<>();
            currentUser.setFamilies_map(families_map);
        }

        firebaseDB.writeNewFamilyToDB(family , currentUser.getFamilies_map(),imageName,imageUri);
        families.add(family);
        sendDataCallback.sendFamily(family,FamilyFlag.SEND_TO_FAV_FAMILY_FRAGMENT);
        itemFamilyAdapter.notifyItemInserted(families.size() - 1);
    }

    /**
     * This method read all user's families
     * @param user
     */
    public void setCurrentUser(User user){
        this.currentUser = user;
        firebaseDB.retrieveAllFamilies(currentUser.getFamilies_map(),families,sendDataCallback);
    }

    private void findViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        imgProfile = view.findViewById(R.id.imgProfile);
    }
}
