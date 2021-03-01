package com.example.pety.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.pety.R;
import com.example.pety.adapters.ItemBeautyHealthAdapter;
import com.example.pety.interfaces.InsertDialogInterface;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InsertTimeDateDialog extends AppCompatDialogFragment {
    public static final String UPDATE = "update";
    public static final String INSERT = "insert";
    public static final String YES = "yes";
    public static final String NO = "no";
    int position;
    int day, month, year;

    InsertDialogInterface insertDialogInterface;
    TextInputLayout insertDialog_LAY_Time_Date;
    final Calendar myCalendar = Calendar.getInstance();

    ItemBeautyHealthAdapter itemBeautyHealthAdapter;
    String op = INSERT; //Option to update or insert (beauty / health)
    String Click_Cancel = YES;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.insert_time_date_dialog, null);
        findViews(view);

        insertDialog_LAY_Time_Date.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), date, year, month, day);
                datePickerDialog.show();
            }
        });


        builder.setView(view)
                .setTitle(R.string.set_time_date)
                .setNegativeButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String timeBeauty = insertDialog_LAY_Time_Date.getEditText().getText().toString();
                        if (timeBeauty.isEmpty()) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle(R.string.error)
                                    .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    })
                                    .setMessage(R.string.empty_field)
                                    .show();
                        } else {
                            if (op.equals(UPDATE)) {
                                insertDialogInterface.applyTimeDate(timeBeauty, InsertTimeDateDialog.UPDATE);
                                op = INSERT;
                            } else {
                                insertDialogInterface.applyTimeDate(timeBeauty, InsertTimeDateDialog.INSERT);
                            }
                        }
                    }
                })
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Click_Cancel == NO) {
                            itemBeautyHealthAdapter.notifyItemChanged(position);
                        }
                    }
                });

        return builder.create();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        }
    };

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        insertDialog_LAY_Time_Date.getEditText().setText(sdf.format(myCalendar.getTime()));
    }

    public void setInsertOrUpdate(String op, ItemBeautyHealthAdapter itemBeautyHealthAdapter, int position) {
        this.op = op;
        this.Click_Cancel = NO;
        this.itemBeautyHealthAdapter = itemBeautyHealthAdapter;
        this.position = position;
    }

    void findViews(View view) {
        insertDialog_LAY_Time_Date = view.findViewById(R.id.insertDialog_LAY_Time_Date);
    }

    public void setInsertDialogInterface(InsertDialogInterface insertDialogInterface) {
        this.insertDialogInterface = insertDialogInterface;
    }
}
