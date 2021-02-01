package com.example.pety.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pety.R;
import com.example.pety.objects.Family;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    ArrayList<Family> mArrayList;
    Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ItemAdapter(ArrayList<Family> mArrayList, Context mContext) {
        this.mContext = mContext;
        if(mArrayList != null){
            this.mArrayList = mArrayList;
        }else{
            this.mArrayList = new ArrayList<>();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_family,parent,false);
        return new ViewHolder(view , mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.family_item_title.setText(mArrayList.get(position).getF_name());
        //holder.family_item_img.setImageURI(mArrayList.get(position).getImageUri());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView family_item_title;
        private ImageView family_item_img;

        public ViewHolder(@NonNull View itemView, OnItemClickListener mListener) {
            super(itemView);
            family_item_title = itemView.findViewById(R.id.family_item_title);
            family_item_img = itemView.findViewById(R.id.family_item_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        Log.d("ttttt", "onClick: position:" + position );
                    }
                }
            });
        }
    }
}
