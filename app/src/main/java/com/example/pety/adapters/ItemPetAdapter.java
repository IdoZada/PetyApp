package com.example.pety.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pety.R;
import com.example.pety.interfaces.OnItemClickListener;
import com.example.pety.enums.Fab;
import com.example.pety.objects.Pet;

import java.util.ArrayList;

public class ItemPetAdapter extends RecyclerView.Adapter<ItemPetAdapter.ViewHolder> {
    ArrayList<Pet> mArrayList;
    Context mContext;
    private OnItemClickListener mListener;
    //int fill
    int position = 0;

    public ItemPetAdapter(ArrayList<Pet> mArrayList, Context mContext) {
        this.mContext = mContext;
        if (mArrayList != null) {
            this.mArrayList = mArrayList;
        } else {
            this.mArrayList = new ArrayList<>();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ItemPetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pet, parent, false);
        return new ItemPetAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPetAdapter.ViewHolder holder, int position) {
        holder.pet_item_progressbar_walking.setMax( mArrayList.get(position).getMaxProgressBarWalking());
        holder.pet_item_progressbar_walking.setProgress( mArrayList.get(position).getFillProgressBarWalking());
        holder.pet_item_progressbar_feeding.setMax(mArrayList.get(position).getMaxProgressBarFeeding());
        holder.pet_item_progressbar_feeding.setProgress(mArrayList.get(position).getFillProgressBarFeeding());
        holder.pet_item_progressbar_beauty_care.setMax(mArrayList.get(position).getMaxProgressBarBeauty());
        holder.pet_item_progressbar_beauty_care.setProgress(mArrayList.get(position).getFillProgressBarBeauty());
        holder.pet_item_progressbar_health.setMax(mArrayList.get(position).getMaxProgressBarHealth());
        holder.pet_item_progressbar_health.setProgress(mArrayList.get(position).getFillProgressBarHealth());
        holder.pet_item_name.setText(mArrayList.get(position).getName());
        Glide.with(mContext).load(mArrayList.get(position).getImage_url()).into(holder.pet_item_img);
        holder.pet_item_type.setText(mArrayList.get(position).getPet_type());
        holder.pet_item_birthday.setText(mArrayList.get(position).getBirthday());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public void setProgressBar(int fillProgressBar , int maxProgressBar , Fab fab){
        Log.d("TAG", "displayReceivedData: " +  mArrayList.size());
        if(Fab.WALK_FAB == fab){
            mArrayList.get(this.position).setMaxProgressBarWalking(maxProgressBar);
            mArrayList.get(this.position).setFillProgressBarWalking(fillProgressBar);
        }else if(Fab.FEED_FAB == fab){
            mArrayList.get(this.position).setMaxProgressBarFeeding(maxProgressBar);
            mArrayList.get(this.position).setFillProgressBarFeeding(fillProgressBar);
        }else if(Fab.BEAUTY_FAB == fab){
            mArrayList.get(this.position).setMaxProgressBarBeauty(maxProgressBar);
            mArrayList.get(this.position).setFillProgressBarBeauty(fillProgressBar);
        }else{
            mArrayList.get(this.position).setMaxProgressBarHealth(maxProgressBar);
            mArrayList.get(this.position).setFillProgressBarHealth(fillProgressBar);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pet_item_name;
        private TextView pet_item_type;
        private TextView pet_item_birthday;
        private ImageView pet_item_img;
        private ProgressBar pet_item_progressbar_walking;
        private ProgressBar pet_item_progressbar_feeding;
        private ProgressBar pet_item_progressbar_beauty_care;
        private ProgressBar pet_item_progressbar_health;


        public ViewHolder(@NonNull View itemView, OnItemClickListener mListener) {
            super(itemView);
            pet_item_name = itemView.findViewById(R.id.pet_item_name);
            pet_item_img = itemView.findViewById(R.id.pet_item_img);
            pet_item_type = itemView.findViewById(R.id.pet_item_type);
            pet_item_birthday = itemView.findViewById(R.id.pet_item_birthday);
            pet_item_progressbar_walking = itemView.findViewById(R.id.pet_progressbar_walk_feed);
            pet_item_progressbar_feeding = itemView.findViewById(R.id.pet_item_progressbar_feeding);
            pet_item_progressbar_beauty_care = itemView.findViewById(R.id.pet_item_progressbar_beauty_care);
            pet_item_progressbar_health = itemView.findViewById(R.id.pet_item_progressbar_health);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

            pet_item_progressbar_walking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemCareClick(position, Fab.WALK_FAB);
                        }
                    }
                }
            });

            pet_item_progressbar_feeding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemCareClick(position, Fab.FEED_FAB);
                        }
                    }
                }
            });
            pet_item_progressbar_beauty_care.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            //mListener.onItemClick(position);
                            mListener.onItemCareClick(position, Fab.BEAUTY_FAB);
                        }
                    }
                }
            });
            pet_item_progressbar_health.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemCareClick(position, Fab.HEALTH_FAB);
                        }
                    }
                }
            });
        }
    }
}
