package com.example.pety.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pety.R;
import com.example.pety.interfaces.OnItemClickListener;
import com.example.pety.objects.Beauty;
import com.example.pety.objects.Health;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class ItemBeautyHealthAdapter<T> extends RecyclerView.Adapter<ItemBeautyHealthAdapter.ViewHolder> {
    ArrayList<T> mArrayList;
    Context mContext;
    private OnItemClickListener mListener;

    public ItemBeautyHealthAdapter(ArrayList<T> mArrayList, Context mContext){
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
    public ItemBeautyHealthAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_beauty, parent, false);

        return new ItemBeautyHealthAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemBeautyHealthAdapter.ViewHolder holder, int position) {
        T generic = mArrayList.get(position);
        if(generic instanceof Beauty){
            holder.time_date_LBL_beauty.setText(((Beauty) generic).getTimeDate());
            holder.time_date_piker_switch.setChecked(((Beauty)generic).isActive());
        }else if (generic instanceof Health){
            holder.time_date_LBL_beauty.setText(((Health) generic).getTimeDate());
            holder.time_date_piker_switch.setActivated(((Health)generic).isActive());
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SwitchMaterial time_date_piker_switch;
        private TextView time_date_LBL_beauty;


        public ViewHolder(@NonNull View itemView, OnItemClickListener mListener) {
            super(itemView);
            time_date_piker_switch = itemView.findViewById(R.id.time_date_piker_switch);
            time_date_LBL_beauty = itemView.findViewById(R.id.time_date_LBL_beauty);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

            time_date_piker_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("TAG", "onCheckedChanged: " + isChecked);
                    if (mListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            T generic = mArrayList.get(position);
                            if(generic instanceof Beauty){
                                Beauty beauty = (Beauty) generic;
                                if(beauty.isActive() == true){
                                    beauty.setActive(false);

                                }else{
                                    beauty.setActive(true);
                                }
                                mListener.onSwitchItemClick(beauty.isActive(),position);
                            }else{
                                if(generic instanceof Health){
                                    Health health = (Health) generic;
                                    if(health.isActive() == true){
                                        health.setActive(false);

                                    }else{
                                        health.setActive(true);
                                    }
                                    mListener.onSwitchItemClick(health.isActive(),position);
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}

