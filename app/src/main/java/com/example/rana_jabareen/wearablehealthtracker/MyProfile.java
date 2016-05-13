package com.example.rana_jabareen.wearablehealthtracker;


import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfile extends Fragment {
    EditText user;
    EditText personIncharge;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("MyProfile");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);
    }

    EditText BirthDate;
    Switch Gender;
    ImageView UserPic;
    EditText weight;
    public MyProfile() {
        // Required empty public constructor



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        UserPic=(ImageView)view.findViewById(R.id.imageView2);
        user=(EditText)view.findViewById(R.id.user);
        weight=(EditText)view.findViewById(R.id.weight);
        user.setCompoundDrawablesWithIntrinsicBounds(R.drawable.account, 0, 0, 0);

        personIncharge=(EditText)view.findViewById(R.id.personincharge);
        personIncharge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.personincharge, 0, 0, 0);
        BirthDate=(EditText)view.findViewById(R.id.birthdate);
        BirthDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.birthdate, 0, 0, 0);
        weight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.weight, 0, 0, 0);
        Gender=(Switch)view.findViewById(R.id.gender1);
        Gender.setCompoundDrawablesWithIntrinsicBounds(R.drawable.gender, 0, 0, 0);
        Gender.setTextOff("Male");
        Gender.setTextOn("Female");

        UserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
       if(!SaveSharedPreference.getPatientPIC(getActivity()).equals(""))
       {
           File imgFile = new  File(SaveSharedPreference.getPatientPIC(getActivity()));

           if(imgFile.exists()){
               Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
               UserPic.setImageBitmap(myBitmap);

           }

       }

        setInfo();


        return view;
    }

    public void setInfo()
    {
        Patient patient=MainActivity.patient;
        user.setText(patient.getFirstName() + " " + patient.getLastName());
        BirthDate.setText(patient.getBirthDate());
        weight.setText(patient.getWeight()+"");
        Gender.setChecked(true);
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SaveSharedPreference.setPatientPIC(getActivity(),destination.getPath());
        UserPic.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        UserPic.setImageBitmap(bm);
      SaveSharedPreference.setPatientPIC(getActivity(),selectedImagePath);

    }



}
