package com.example.pety.interfaces;

import com.example.pety.objects.Fab;

public interface OnItemClickListener {
    void onItemClick(int position);
    void onItemCareClick(int position, Fab chose_fab);
    void onSwitchItemClick(boolean isChecked,int position);
}
