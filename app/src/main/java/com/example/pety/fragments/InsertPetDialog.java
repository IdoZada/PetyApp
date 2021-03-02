package com.example.pety.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.pety.R;
import com.example.pety.interfaces.InsertDialogInterface;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;;

public class InsertPetDialog extends AppCompatDialogFragment {

    final Calendar myCalendar = Calendar.getInstance();
    String imageFileName;
    Uri fileUri;

    InsertDialogInterface insertDialogInterface;
    TextInputLayout insertDialog_LAY_petName;
    TextInputLayout insertDialog_LAY_petType;
    TextInputLayout insertDialog_LAY_petBirthday;
    FloatingActionButton fab_add_photo;
    AppCompatImageView imgPetProfile;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.insert_pet_dialog, null);
        findViews(view);
        initViews();

        updateEditText();

        builder.setView(view)
                .setTitle(R.string.add_pet)
                .setNegativeButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String petName = insertDialog_LAY_petName.getEditText().getText().toString();
                        String petType = insertDialog_LAY_petType.getEditText().getText().toString();
                        String birthday = insertDialog_LAY_petBirthday.getEditText().getText().toString();
                        if (petName.isEmpty() || fileUri == null || petType.isEmpty() || birthday.isEmpty()) {
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
                            fileUri = null;
                        } else {
                            insertDialogInterface.applyAttPet(petName, petType, birthday, imageFileName, fileUri);
                        }
                    }
                })
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        return builder.create();
    }

    /**
     * Get the date data from date picker dialog
     */
    public void updateEditText() {
        insertDialog_LAY_petBirthday.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        insertDialog_LAY_petBirthday.getEditText().setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            fileUri = data.getData(); //Image Uri will not be null for RESULT_OK
            File file = ImagePicker.Companion.getFile(data);
            imgPetProfile.setImageURI(fileUri);
            imgPetProfile.setScaleType(ImageView.ScaleType.FIT_XY);
            imageFileName = file.getName();
        }
    }

    public void setInsertDialogInterface(InsertDialogInterface insertDialogInterface) {
        this.insertDialogInterface = insertDialogInterface;
    }

    void initViews() {
        imgPetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fab_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(InsertPetDialog.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    void findViews(View view) {
        insertDialog_LAY_petName = view.findViewById(R.id.insertDialog_LAY_petName);
        insertDialog_LAY_petType = view.findViewById(R.id.insertDialog_LAY_petType);
        insertDialog_LAY_petBirthday = view.findViewById(R.id.insertDialog_LAY_petBirthday);
        fab_add_photo = view.findViewById(R.id.fab_add_photo);
        imgPetProfile = view.findViewById(R.id.imgPetProfile);
    }
}
