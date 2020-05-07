package com.assigned.printart;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.assigned.printart.Paper.PaperStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    EditText inputPhonenumber, inputOtp;
    Button getOTP, putOTP;
    TextView one, two, three,tandc;
    DatabaseReference databaseReference;
    String temp, codeIS, codeIN;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("Main");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        inputPhonenumber = (EditText) findViewById(R.id.inputPHN);
        inputOtp = (EditText) findViewById(R.id.inputOTP);
        getOTP = (Button) findViewById(R.id.btn_getotp);
        putOTP = (Button) findViewById(R.id.btn_putotp);
        one = (TextView) findViewById(R.id.one);
        two = (TextView) findViewById(R.id.two);
        tandc=(TextView)findViewById(R.id.tandc);
        three = (TextView) findViewById(R.id.three);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        tandc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TermsandConditionsActivity.class);
                startActivity(intent);
            }
        });
        inputOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    putOTP.setEnabled(true);
                } else if (s.length() < 6) {
                    putOTP.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputPhonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 10) {
                    getOTP.setEnabled(true);
                } else if (s.length() < 10) {
                    getOTP.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = inputPhonenumber.getText().toString();
                inputPhonenumber.setText("");
                one.setText("OTP \n verification:");
                two.setText("Enter the OTP sent to " + temp);
                three.setText("OTP:");
                firebaseOTP(temp);


            }
        });
        putOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyOTP();
            }
        });
    }

    private void VerifyOTP() {
        codeIN = inputOtp.getText().toString();
        inputOtp.setText("");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeIS, codeIN);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                            intent.putExtra("Contact", "" + temp);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void firebaseOTP(String temp) {
        temp = "+91" + temp;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                temp,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new
            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    databaseReference.child("EndUsers").child(temp).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                Paper.book().write(PaperStore.UserLoginID, temp);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                                intent.putExtra("Contact", "" + temp);
                                startActivity(intent);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(MainActivity.this, "You have exceeded the maximum number of try! Please try after 24 hours, or Contact support team!", Toast.LENGTH_SHORT).show();
                    Log.e("Errortxt", "" + e);
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    getOTP.setVisibility(View.INVISIBLE);
                    putOTP.setVisibility(View.VISIBLE);
                    inputPhonenumber.setVisibility(View.INVISIBLE);
                    inputOtp.setVisibility(View.VISIBLE);
                    inputOtp.requestFocus();
                    codeIS = s;


                }
            };
}
