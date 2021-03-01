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
import com.example.pety.objects.Feed;
import com.example.pety.objects.Walk;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class ItemWalkFeedAdapter<T> extends RecyclerView.Adapter<ItemWalkFeedAdapter.ViewHolder> {
    ArrayList<T> mArrayList;
    Context mContext;
    private OnItemClickListener mListener;

    public ItemWalkFeedAdapter(ArrayList<T> mArrayList, Context mContext) {
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
    public ItemWalkFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_walk, parent, false);
        return new ItemWalkFeedAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemWalkFeedAdapter.ViewHolder holder, int position) {
        T generic = mArrayList.get(position);
        if (generic instanceof Walk) {
            holder.time_LBL_walk.setText(((Walk) generic).getTime());
            holder.time_piker_switch.setChecked(((Walk) generic).isActive());
        } else if (generic instanceof Feed) {
            holder.time_LBL_walk.setText(((Feed) generic).getTime());
            holder.time_piker_switch.setChecked(((Feed) generic).isActive());
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SwitchMaterial time_piker_switch;
        private TextView time_LBL_walk;

        public ViewHolder(@NonNull View itemView, OnItemClickListener mListener) {
            super(itemView);
            time_piker_switch = itemView.findViewById(R.id.time_piker_switch);
            time_LBL_walk = itemView.findViewById(R.id.time_LBL_walk);

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

            time_piker_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            T generic = mArrayList.get(position);
                            if (generic instanceof Walk) {
                                Walk walk = (Walk) generic;
                                if (walk.isActive() == true) {
                                    walk.setActive(false);
                                } else {
                                    walk.setActive(true);
                                }
                                mListener.onSwitchItemClick(walk.isActive(), position);
                            } else {
                                if (generic instanceof Feed) {
                                    Feed feed = (Feed) generic;
                                    if (feed.isActive() == true) {
                                        feed.setActive(false);
                                    } else {
                                        feed.setActive(true);
                                    }
                                    mListener.onSwitchItemClick(feed.isActive(), position);
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}

