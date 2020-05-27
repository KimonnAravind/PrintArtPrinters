package com.assigned.printart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackingDetailsActivity extends AppCompatActivity {
    TextView orderedDate;
    String ordereddate, Status, Contact;
    DatabaseReference databaseReference;
    TextView deliveryDate;
    TextView one, two, three, four, five, six,seven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_details);

        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("Track Status");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        orderedDate = (TextView) findViewById(R.id.orderedDate);
        ordereddate = getIntent().getStringExtra("Date");
        Status = getIntent().getStringExtra("Status");
        Contact = getIntent().getStringExtra("Contact");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        initiateall();

        Toast.makeText(this, ""+Status, Toast.LENGTH_SHORT).show();
    }

    private void initiateall() {
        one = (TextView) findViewById(R.id.orderapproved);
        two = (TextView) findViewById(R.id.orderedstatus);
        three = (TextView) findViewById(R.id.orderpacked);
        four = (TextView) findViewById(R.id.orderedreadytodispatch);
        five = (TextView) findViewById(R.id.ordershipped);
        six = (TextView) findViewById(R.id.ordersent);
        seven = (TextView)findViewById(R.id.orderdelivered);
    }

    @Override
    protected void onStart() {
        super.onStart();
        orderedDate.setText(ordereddate);


        databaseReference.child("PlacedOrders").child(Contact).child(ordereddate).child("DeliveryDate").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            deliveryDate = (TextView) findViewById(R.id.deliveryDate);
                            deliveryDate.setText(dataSnapshot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        displaystatus();
    }

    private void displaystatus() {
    switch (Status)
    {
        case "Approved":
        {
            one.setEnabled(true);
            two.setEnabled(true);
            break;
        }
        case "Packed":
        {
            Toast.makeText(this, "Packed", Toast.LENGTH_SHORT).show();

            one.setEnabled(true);
            two.setEnabled(true);
            three.setEnabled(true);
            four.setEnabled(true);
            break;
        }
        case "Dispatched":
        {

            one.setEnabled(true);
            two.setEnabled(true);
            three.setEnabled(true);
            four.setEnabled(true);
            five.setEnabled(true);
            six.setEnabled(true);
            break;
        }
        case "Delivered":
        {

            one.setEnabled(true);
            two.setEnabled(true);
            three.setEnabled(true);
            four.setEnabled(true);
            five.setEnabled(true);
            six.setEnabled(true);
            seven.setEnabled(true);
            seven.setText("Delivered on");
            break;
        }

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
