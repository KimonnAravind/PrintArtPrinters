package com.assigned.printart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class PrintArtActivity extends AppCompatActivity{
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_art);

        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("PrintArt Sivakasi");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imageView= (ImageView)findViewById(R.id.imageView);
    }


    @Override
    protected void onStart() {
        super.onStart();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ispackageInstalled(getPackageManager()))
                {
                    String uri = "http://maps.google.com/maps?saddr=" + 9.462977 + "," + 77.790732 + "&daddr=" + 9.462977 + "," + 77.790732;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
                else
                {
                }
            }
        });
    }

    private boolean ispackageInstalled(PackageManager packageManager) {

        try {
            packageManager.getPackageInfo("com.google.android.apps.maps",0);
            return true;
        }
        catch (Exception e)
        {Toast.makeText(PrintArtActivity.this, "Google map, App not found", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
