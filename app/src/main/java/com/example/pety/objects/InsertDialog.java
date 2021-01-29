package com.example.pety.objects;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.pety.R;
import com.google.android.material.textfield.TextInputLayout;

public class InsertDialog extends AppCompatDialogFragment {

    insert_DialogInterface insert_dialogInterface;
    TextInputLayout insertDialog_LAY_familyName;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.insert_dialog, null);

        builder.setView(view)
                .setTitle("Add New Family")
                .setNegativeButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String familyName = insertDialog_LAY_familyName.getEditText().getText().toString();
                        Log.d("ttst", "familyName: " + familyName);
                        insert_dialogInterface.applyTexts(familyName);
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        insertDialog_LAY_familyName = view.findViewById(R.id.insertDialog_LAY_familyName1);

        return builder.create();
    }

    public void setInsertDialogInterface(insert_DialogInterface insert_dialogInterface){
        this.insert_dialogInterface = insert_dialogInterface;
    }

//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        insert_dialogInterface = (insert_DialogInterface) context;
//        super.onAttach(context);
//    }


//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if( context instanceof insert_DialogInterface){
//            // if our activity implements this interface
//            insert_dialogInterface = (insert_DialogInterface) context;
//
//        }else{
//            throw new RuntimeException(context.toString() + "must implements insert_DialogInterface");
//        }
//    }

    public interface insert_DialogInterface {
        void applyTexts(String familyName);
    }
}
