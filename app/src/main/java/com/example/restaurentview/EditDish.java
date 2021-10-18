package com.example.restaurentview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class EditDish extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Intent intent;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;
    private EditText name;
    private EditText desc;
    private EditText price;
    private SwitchCompat switchNewDish;
    private SwitchCompat switchRecommended;
    private SwitchCompat switchVeg;
    private ProgressBar mProgressBar;
    private EditText discount;
    private FirebaseStorage firebaseStorage;
    private ImageView image;
 Uri selectedImageUri;
 String emailchild;
    private static final int RC_PHOTO_PICKER =  2;
    private StorageTask mUploadTask;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dish);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.name);
image = findViewById(R.id.image2);
        mProgressBar = findViewById(R.id.progress_bar);
        desc = findViewById(R.id.desc);
        price = findViewById(R.id.price);
        discount = findViewById(R.id.discountPrice);
        switchNewDish = findViewById(R.id.switchNewDish);
        switchRecommended = findViewById(R.id.switchRecommended);
        switchVeg = findViewById(R.id.switchVeg);

        emailchild = getIntent().getStringExtra("emailchild");
        firebaseStorage =FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Dishes").child(emailchild);
        Button upload = findViewById(R.id.upload);
        image.setOnClickListener(view -> {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityIfNeeded(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
        });



        Button finish = findViewById(R.id.finish);
        finish.setOnClickListener(v -> {
            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(EditDish.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                upload();
            }



        });
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void upload() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (selectedImageUri != null) {
            StorageReference photoRef = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(selectedImageUri));
            mUploadTask = photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, taskSnapshot -> {
                        Handler handler = new Handler();
                        handler.postDelayed(() -> mProgressBar.setProgress(100), 200);
                        Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                        photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String str1 = null;
                            if (switchNewDish.isChecked())
                                str1 = switchNewDish.getTextOn().toString();
                            else
                                str1 = switchNewDish.getTextOff().toString();

                            String str2 = null;
                            if (switchRecommended.isChecked())
                                str2 = switchRecommended.getTextOn().toString();
                            else
                                str2 = switchRecommended.getTextOff().toString();

                            String str3 = null;
                            if (switchVeg.isChecked())
                                str3 = switchVeg.getTextOn().toString();
                            else
                                str3 = switchVeg.getTextOff().toString();
                            Dish dish = new Dish(discount.getText().toString(), desc.getText().toString(), uri.toString(), name.getText().toString(), str1, price.getText().toString(), str2, str3);
                            String id =databaseReference.push().getKey();
                            databaseReference.child(id).setValue(dish);

                        });

//
//                        Toast.makeText(EditDish.this, downloadUrl.toString()+".jpg", Toast.LENGTH_SHORT).show();
//                        image.setImageURI(Uri.parse(selectedImageUri.toString()));
                        image.setScaleType(ImageView.ScaleType.FIT_XY);

                        assert downloadUrl != null;



                        Toast.makeText(EditDish.this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.INVISIBLE);
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditDish.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK && requestCode==RC_PHOTO_PICKER && data!=null){



                selectedImageUri = data.getData();
            Toast.makeText(EditDish.this, selectedImageUri.toString(), Toast.LENGTH_SHORT).show();
                image.setImageURI(Uri.parse(selectedImageUri.toString()));
            image.setScaleType(ImageView.ScaleType.FIT_XY);


        }
        else if (resultCode==RESULT_CANCELED){
            Toast.makeText(this, "Please select the iage", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EditDish.this,MainActivity.class);
        i.putExtra("emailchild",emailchild);
        startActivity(i);

    }
}