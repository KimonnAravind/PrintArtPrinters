package com.assigned.printart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

public class TrackingDetailsActivity extends AppCompatActivity {
    TextView orderedDate;
    String ordereddate,Status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_details);

        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("Order Status");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        orderedDate = (TextView) findViewById(R.id.orderedDate);
        ordereddate =getIntent().getStringExtra("Date");
        Status = getIntent().getStringExtra("Status");


    }

    @Override
    protected void onStart() {
        super.onStart();
        orderedDate.setText(ordereddate);
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
