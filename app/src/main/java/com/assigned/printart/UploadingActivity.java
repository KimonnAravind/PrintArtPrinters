package com.assigned.printart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UploadingActivity extends AppCompatActivity {


    ImageView whatsapp, gmail, cloud;
    int Choice;
    TextView one, two, three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("Upload");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        whatsapp = (ImageView) findViewById(R.id.whatsapp);
        gmail = (ImageView) findViewById(R.id.gmail);
        cloud = (ImageView) findViewById(R.id.cloud);
        one = (TextView) findViewById(R.id.one);
        two = (TextView) findViewById(R.id.twos);
        three = (TextView) findViewById(R.id.three);

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice = 1;
                openapplication(Choice, getPackageManager());

            }
        });
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice = 2;
                openapplication(Choice, getPackageManager());
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice = 1;
                openapplication(Choice, getPackageManager());
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choice = 2;
                openapplication(Choice, getPackageManager());
            }
        });

        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadinfdb();
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadinfdb();
            }
        });
    }

    private void uploadinfdb() {
        Intent intent = new Intent(UploadingActivity.this, ImageUploadActivity.class);
        startActivity(intent);
    }

    private int openapplication(int choice, PackageManager packageManager) {

        if (choice == 1) {
            try {
                packageManager.getPackageInfo("com.whatsapp", 0);
                startWhatsapp();
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(this, "App not found!", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                packageManager.getPackageInfo("com.google.android.gm", 0);
                startGmail();
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(this, "App not found!", Toast.LENGTH_SHORT).show();
            }

        }
        return 1;

    }

    private void startGmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "wearepostermakers@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "PrintArt Sivakasi!");
        startActivity(Intent.createChooser(emailIntent, null));
    }

    private void startWhatsapp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + "+918870958617" + "&text=" + "PrintArt: "));
        startActivity(intent);
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