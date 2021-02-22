package com.example.pety.objects;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.pety.R;
import com.example.pety.interfaces.InsertDialogInterface;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InsertTimeDialog extends AppCompatDialogFragment {

    InsertDialogInterface insertDialogInterface;
    TextInputLayout insertDialog_LAY_walkTime;
    final Calendar myCalendar = Calendar.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.insert_time_dialog, null);


        findViews(view);

        insertDialog_LAY_walkTime.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),true).show();
            }
        });


        builder.setView(view)
                .setTitle("Add new time")
                .setNegativeButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String timeWalking = insertDialog_LAY_walkTime.getEditText().getText().toString();
                        Log.d("TAG", "onClick: " + timeWalking);
                        if(timeWalking.isEmpty()){
                            Log.d("TAG", "onClick:  STRING IS EMPTY");
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Error")
                                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    })
                                    .setMessage("You can't let the fields empty")
                                    .show();
                        }else{
                            insertDialogInterface.applyTime(timeWalking);
                        }
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        insertDialog_LAY_walkTime.getEditText().setText(sdf.format(myCalendar.getTime()));
    }

    void findViews(View view){
        insertDialog_LAY_walkTime = view.findViewById(R.id.insertDialog_LAY_walkTime);
    }

    public void setInsertDialogInterface(InsertDialogInterface insertDialogInterface){
        this.insertDialogInterface = insertDialogInterface;
    }
}
