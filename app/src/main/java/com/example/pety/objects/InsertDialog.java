package com.example.pety.objects;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.pety.R;
import com.example.pety.utils.FirebaseDB;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.net.URI;

public class InsertDialog extends AppCompatDialogFragment {

    insert_DialogInterface insert_dialogInterface;
    TextInputLayout insertDialog_LAY_familyName;
    FloatingActionButton fab_add_photo;
    AppCompatImageView imgProfile;

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
                        //Log.d("ttst", "familyName: " + familyName);
                        insert_dialogInterface.applyTexts(familyName);
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        insertDialog_LAY_familyName = view.findViewById(R.id.insertDialog_LAY_familyName1);
        fab_add_photo = view.findViewById(R.id.fab_add_photo);
        imgProfile = view.findViewById(R.id.imgProfile);

        fab_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(InsertDialog.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            File file = ImagePicker.Companion.getFile(data);

            FirebaseStorage storage = FirebaseDB.getInstance().getFirebaseStorage();
            StorageReference storageRef = storage.getReference();
            StorageReference imagesRef = storageRef.child("images");

//            imagesRef.child(file.toString()).ge;

            //ImagePicker.Companion.
            Log.d("ttst", "onActivityResult:  " + file);
            imgProfile.setImageURI(fileUri);
            imgProfile.setScaleType(ImageView.ScaleType.FIT_XY);
            //insertDialog.setImagePicker(fileUri);

//            //You can get File object from intent
//            File file = new File();
//            file = ImagePicker.(data);
//
//                    //You can also get File Path from intent
//                    val filePath:String = ImagePicker.getFilePath(data)!!
//        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
//        }
        }
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
