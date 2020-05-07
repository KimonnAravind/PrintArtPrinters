package com.assigned.printart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddressUpdateActivity extends AppCompatActivity {
    EditText address, pincode;
    DatabaseReference addressreference;
    TextView ads;
    String number;
    Button Submit;
    HashMap<String, Object> EndUsers = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_update);

        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("Address");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        number = getIntent().getStringExtra("Number");
        address = (EditText) findViewById(R.id.address);
        pincode = (EditText) findViewById(R.id.picode);
        addressreference = FirebaseDatabase.getInstance().getReference();
        ads = (TextView) findViewById(R.id.adds);
        Submit = (Button) findViewById(R.id.submits);
    }


    @Override
    protected void onStart() {
        super.onStart();
        addressreference.child("EndUsers").child(number).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ads.setText(dataSnapshot.child("Address").getValue().toString() + " - " + dataSnapshot.child("Pincode").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 15) {

                } else {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    Submit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
Submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
if(address.getText().toString().length()<15)
{
    address.setError("Invalid Address");
}
else
{
    EndUsers.put("Address",""+address.getText().toString());
    EndUsers.put("Pincode",""+pincode.getText().toString());
    addressreference.child("EndUsers").child(number).updateChildren(EndUsers).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
       if(task.isSuccessful())
       {
           Toast.makeText(AddressUpdateActivity.this, "Address Updated Successfully", Toast.LENGTH_SHORT).show();
            address.setText("");
            pincode.setText("");
       }
        }
    });
}
    }
});
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
