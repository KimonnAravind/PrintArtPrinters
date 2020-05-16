package com.assigned.printart;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GreetingsActivity extends AppCompatActivity {
    String saving, sends, date;
    TextView textView;
    TextView DeliveryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("Order Placed");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Toast.makeText(this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
        saving = getIntent().getStringExtra("Savings");
        sends = getIntent().getStringExtra("Sends");
        date = getIntent().getStringExtra("date");
        textView = (TextView) findViewById(R.id.saving);
        textView.setText("You Saved " + saving + "₹ with this order");
        DeliveryDate = (TextView) findViewById(R.id.date);

        DeliveryDate.setText("Your product will be deliver on or before \n" + date);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        meth();
    }

    @Override
    public boolean onSupportNavigateUp() {
        meth();
        return super.onSupportNavigateUp();
    }

    void meth() {
        Intent intent = new Intent(GreetingsActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
