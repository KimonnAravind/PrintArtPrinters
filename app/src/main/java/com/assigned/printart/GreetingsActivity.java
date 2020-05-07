package com.assigned.printart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GreetingsActivity extends AppCompatActivity {
    String saving, sends;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("THANK YOU");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Toast.makeText(this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
        saving = getIntent().getStringExtra("Savings");
        sends = getIntent().getStringExtra("Sends");
        textView=(TextView)findViewById(R.id.saving);
        textView.setText("You Saved "+ saving+"₹ with this order");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GreetingsActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(GreetingsActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        return super.onSupportNavigateUp();
    }
}