package com.example.pety.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pety.R;
import com.example.pety.interfaces.OnItemClickListener;
import com.example.pety.objects.Family;

import java.util.ArrayList;

public class ItemMyFavFamilyAdapter extends RecyclerView.Adapter<ItemMyFavFamilyAdapter.ViewHolder> {
    ArrayList<Family> mArrayList;
    Context mContext;
    private OnItemClickListener mListener;
    int checkedPosition = 0;

    public ItemMyFavFamilyAdapter(ArrayList<Family> mArrayList, Context mContext) {
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
    public ItemMyFavFamilyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_fav_family, parent, false);


        return new ItemMyFavFamilyAdapter.ViewHolder(view, mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemMyFavFamilyAdapter.ViewHolder holder, int position) {
        holder.bind(mArrayList.get(position));
        holder.family_name_LBL.setText(mArrayList.get(position).getF_name());
//        if (mArrayList.get(position).isPresentHome()) {
//            holder.family_IMG_home.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView family_name_LBL;
        private ImageView family_IMG_home;

        public ViewHolder(@NonNull View itemView, OnItemClickListener mListener) {
            super(itemView);
            family_name_LBL = itemView.findViewById(R.id.family_name_LBL);
            family_IMG_home = itemView.findViewById(R.id.family_IMG_home);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    family_IMG_home.setVisibility(View.VISIBLE);
                    int position = getAdapterPosition();
                    if(checkedPosition != position){
                        notifyItemChanged(checkedPosition);
                        checkedPosition = position;

                    }
                    if (mListener != null) {
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }


        public void bind(final Family family) {
            if (checkedPosition == -1) {
                family_IMG_home.setVisibility(View.GONE);
            }else{
                if(checkedPosition == getAdapterPosition()){
                    family_IMG_home.setVisibility(View.VISIBLE);
                }else {
                    family_IMG_home.setVisibility(View.GONE);
                }
            }
        }

        public Family getSelected(){
            if(checkedPosition == -1){
                return mArrayList.get(checkedPosition);
            }
            return null;
        }

    }
}
