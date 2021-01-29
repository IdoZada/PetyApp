package com.example.pety.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pety.R;
import com.example.pety.objects.Family;
import com.example.pety.objects.itemData;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_row,parent,false);
        return new ViewHolder(view , mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtLine1.setText(mArrayList.get(position).getF_name());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtLine1;

        public ViewHolder(@NonNull View itemView, OnItemClickListener mListener) {
            super(itemView);
            txtLine1 = itemView.findViewById(R.id.txtline1);
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
