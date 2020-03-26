package com.example.autobot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class EditProfilePage extends Fragment {
    private EditText firstName;
    private EditText lastName;
    private EditText emailAddress;
    private EditText homeAddress;
    private EditText eContact;
    private TextView userName;
    private CircleImageView pPhoto;
    private Button btn;
    private EditProfilePageListener listener;
    private Database db;
    private TextView changePhoto;
    private File file;
    private TextView getLibrary, takePhoto, cancel;
    private BottomSheetDialog bottomSheetDialog;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int RESULT_LOAD_IMAGE = 2;
    private String currentPhotoPath;
    public Bitmap bitmap;
    public Uri imageUri;


    public interface EditProfilePageListener {
        void updateInformation(String FirstName, String LastName, String EmailAddress, String HomeAddress, String emergencyContact, Bitmap bitmap);

        String getUsername();

        Bitmap getBitmap();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditProfilePageListener) {
            listener = (EditProfilePageListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EditProfilePageListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_contact_infor_activity, container,false);

        pPhoto = view.findViewById(R.id.imageView2);
        bitmap = listener.getBitmap();
        if (bitmap != null) pPhoto.setImageBitmap(bitmap);
        pPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.bottom_options,null);
                getLibrary = view1.findViewById(R.id.get_library);
                takePhoto = view1.findViewById(R.id.take_photo);
                cancel = view1.findViewById(R.id.cancel);

                getLibrary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openLibrary();
                        bottomSheetDialog.dismiss();
                    }
                });
                takePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dispatchTakePictureIntent();
                        //galleryAddPic();
                        bottomSheetDialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();

            }
        });

        userName = view.findViewById(R.id.Username);
        firstName = view.findViewById(R.id.editTextFirstName);
        lastName = view.findViewById(R.id.editTextLastName);
        emailAddress = view.findViewById(R.id.editTextEmail);
        homeAddress = view.findViewById(R.id.editTextHomeAddress);
        eContact = view.findViewById(R.id.editTextEmergencyContact);
        changePhoto = view.findViewById(R.id.change_photo);
        changePhoto.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // set underline
        changePhoto.setOnClickListener(new View.OnClickListener() { // set onClick listener for change photo
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.bottom_options,null);
                getLibrary = view1.findViewById(R.id.get_library);
                takePhoto = view1.findViewById(R.id.take_photo);
                cancel = view1.findViewById(R.id.cancel);

                getLibrary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openLibrary();
                        bottomSheetDialog.dismiss();
                    }
                });
                takePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dispatchTakePictureIntent();
                        //galleryAddPic();
                        bottomSheetDialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
            }
        });

        //db = BaseActivity.db;
        User user = LoginActivity.user;

        userName.setText(user.getUsername());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        emailAddress.setText(user.getEmailAddress());
        homeAddress.setText(user.getHomeAddress());
        eContact.setText(user.getEmergencyContact());
//        Uri Load = Uri.parse(user.getUri());
//        try {
//            InputStream imageLoadStream = getContext().getContentResolver().openInputStream(Load);
//            bitmap = BitmapFactory.decodeStream(imageLoadStream);
//            pPhoto.setImageBitmap(bitmap);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


        btn = view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tempo = 0;
                String fName = firstName.getText().toString();
                if(fName.replace(" " , "").length() == 0){
                    tempo = 1;
                    Toast.makeText(getContext(), "First Name could not be empty", Toast.LENGTH_SHORT).show();
                }
                String lName = lastName.getText().toString();
                if(lName.replace(" " , "").length() == 0){
                    tempo = 1;
                    Toast.makeText(getContext(), "Last Name could not be empty", Toast.LENGTH_SHORT).show();
                }
                String eAddress = emailAddress.getText().toString();
                String hAddress = homeAddress.getText().toString();
                String econtact = eContact.getText().toString();

                if (tempo == 0){
                    listener.updateInformation(fName,lName,eAddress,hAddress,econtact,bitmap);
                    Toast.makeText(getContext(), "Changes saved", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                pPhoto.setImageBitmap(bitmap);
            }else if (requestCode == 2){
                try {
                    imageUri = data.getData();
                    InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(imageStream);
                    pPhoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    //Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(getContext(),
                        "com.example.autobot.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
    }

    private void openLibrary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }
}