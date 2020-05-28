package com.assigned.printart;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerSupportActivity extends AppCompatActivity {

    int x;
    ImageView clickheretochat;
    DatabaseReference databaseReference;
    Boolean installed;
    String one;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle(" Help and Support");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        clickheretochat = (ImageView)findViewById(R.id.clickheretostartchat);
        databaseReference  = FirebaseDatabase.getInstance().getReference().child("AdminControl");

    }

    private boolean isPackinstalled(PackageManager packageManager) {

            try {
                packageManager.getPackageInfo("com.whatsapp", 0);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }

    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    one=dataSnapshot.child("PhoneNumber").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        clickheretochat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installed = isPackinstalled(getPackageManager());
                if (installed) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + one+ "&text=" + "PrintArt: "));
                    startActivity(intent);
                } else {
                    Toast.makeText(CustomerSupportActivity.this, "You don't have whatsapp in your device! Please install whatsapp and try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
