package com.assigned.printart;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CustomerSupportActivity extends AppCompatActivity {

    TextView one, two;
    int x;
    Boolean installed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle(" Help and Support");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        one = (TextView) findViewById(R.id.one);
        two = (TextView) findViewById(R.id.two);


    }

    private boolean isPackinstalled(PackageManager packageManager) {
        if (x == 1) {
            try {
                packageManager.getPackageInfo("com.whatsapp", 0);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        } else if (x == 2) {
            try {
                packageManager.getPackageInfo("com.google.android.gm", 0);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        }

        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = 1;
                installed = isPackinstalled(getPackageManager());
                if (installed) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + one.getText().toString() + "&text=" + "Kimonn"));
                    startActivity(intent);
                } else {
                    Toast.makeText(CustomerSupportActivity.this, "You don't have whatsapp in your device! Please install whatsapp and try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = 2;
                installed = isPackinstalled(getPackageManager());
                if (installed) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "wearepostermakers@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "PrintArt Sivakasi!");
                    startActivity(Intent.createChooser(emailIntent, null));
                } else {
                    Toast.makeText(CustomerSupportActivity.this, "Gmail App is not in your device, Please install and try again!", Toast.LENGTH_SHORT).show();
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
