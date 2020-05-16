package com.assigned.printart;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.assigned.printart.Paper.PaperStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.HashMap;

import io.paperdb.Paper;

public class RegisterActivity extends AppCompatActivity {
    TextView name, password1;
    String n, p1, Contact;
    String formatted, code;
    Button reg;
    RelativeLayout relativeLayout;
    DatabaseReference databaseReference;
    HashMap<String, Object> EndUsers = new HashMap<>();
    HashMap<String, Object> EndUserscart = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("Personal Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Contact = getIntent().getStringExtra("Contact");
        name = (TextView) findViewById(R.id.Uname);
        password1 = (TextView) findViewById(R.id.Upword);
        reg = (Button) findViewById(R.id.reg);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.child("EndUsers").child(Contact).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    Paper.book().write(PaperStore.UserLoginID, Contact);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3) {
                    n = name.getText().toString();
                    password1.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() >= 1) {
                                p1 = password1.getText().toString();
                                reg.setEnabled(true);
                            } else {
                                reg.setEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecureRandom randomnumber = new SecureRandom();
                int num = randomnumber.nextInt(100000);
                formatted = String.format("%05d", num);
                code = n.substring(0, 3);
                code = code.toUpperCase();
                EndUsers.put("Name", n + p1);
                EndUsers.put("PhoneNumber", Contact);
                EndUsers.put("Rcode", code + formatted);
                EndUsers.put("Address", "Delivery Address");
                EndUsers.put("Pincode", "xxxxxx");
                EndUsers.put("Cpoints", "50");
                EndUserscart.put("Total", "0");
                EndUserscart.put("Totalo", "0");

                databaseReference.child("EndUsers").child(Contact).updateChildren(EndUsers)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    databaseReference.child("ListRcode").child(code + formatted).setValue(Contact);
                                    Paper.book().write(PaperStore.UserLoginID, Contact);

                                    databaseReference.child("UserCart").child(Contact).updateChildren(EndUserscart).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(RegisterActivity.this, ReferalActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.putExtra("name", "" + n + " " + p1);
                                                intent.putExtra("contact", Contact);
                                                startActivity(intent);
                                                Toast.makeText(RegisterActivity.this, "Account Created Successfully! Thanks for Join with US!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }
                        });
            }
        });
    }


}
