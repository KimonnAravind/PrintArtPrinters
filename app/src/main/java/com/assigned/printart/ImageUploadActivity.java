package com.assigned.printart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.assigned.printart.Paper.PaperStore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.wang.avi.AVLoadingIndicatorView;

import io.paperdb.Paper;

public class ImageUploadActivity extends AppCompatActivity {
    ImageView imageView, imgview;
    Button button;
    EditText textView;
    Uri imageuri;
    AVLoadingIndicatorView loader;
    StorageReference storageReference;
    StorageTask storageTask;
    String Pnumber, Description = "Empty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("Choose Image");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loader = (AVLoadingIndicatorView) findViewById(R.id.loader);
        textView = (EditText) findViewById(R.id.textv);
        imageView = (ImageView) findViewById(R.id.imageview);
        imgview = (ImageView) findViewById(R.id.imgview);
        button = (Button) findViewById(R.id.button);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Pnumber = Paper.book().read(PaperStore.UserLoginID);
        storageReference = FirebaseStorage.getInstance().getReference("PrintUpload").child(Pnumber);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagechooser();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storageTask != null && storageTask.isInProgress()) {
                    Toast.makeText(ImageUploadActivity.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    fileUploader();
                    loader.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }
            }
        });


    }

    private void fileUploader() {
        Description = textView.getText().toString();
        StorageReference storageReference1 = storageReference.child(Description + "__" + System.currentTimeMillis() + "." + getextofimage(imageuri));

        storageTask = storageReference1.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(ImageUploadActivity.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("");
                        imgview.setImageResource(R.drawable.uploadedsuccessfully);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(ImageUploadActivity.this, "Image upload failed, Please try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getextofimage(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void imagechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            imgview.setImageURI(imageuri);
            button.setTextColor(Color.WHITE);
            textView.setVisibility(View.VISIBLE);
            button.setEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}