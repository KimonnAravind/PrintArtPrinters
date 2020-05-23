package com.assigned.printart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TrackingDetailsActivity extends AppCompatActivity {
    TextView orderedDate;
    String ordereddate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_details);

        orderedDate = (TextView) findViewById(R.id.orderedDate);
        ordereddate =
    }
}
