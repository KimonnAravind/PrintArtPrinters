package com.assigned.printart;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phonepe.intent.sdk.api.PhonePe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PaymentActivity extends AppCompatActivity {
    TextView cost;
    final int UPI_PAYMENT = 0;
    String Totalo;
    RadioButton three, four, five;
    int numberIs = 0;
    String Delivery_Date;
    String Address;
    DatabaseReference creditreduce;
    TextView w, x, y, z, v;
    TextView onel;
    String Savings, sends;
    String paste;
    private String savecurrentdate, savecurrenttime, productrandomkey;
    String one, two;
    String credit;
    DatabaseReference placeorderReference, AdminRef;
    DatabaseReference cartReference;
    Button android_pay;
    String idi = "pl.7904168617@icici";
    Button Gpay, Ppay, Ptm;
    int Ptm_Request = 123;
    int k;
    String users;
    int n, m;
    String DAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        cost = (TextView) findViewById(R.id.cost);
        Gpay = (Button) findViewById(R.id.gpay);
        PhonePe.init(this);
        Ppay = (Button) findViewById(R.id.ppay);
        Ptm = (Button) findViewById(R.id.ptm);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("PAYMENT");
        setSupportActionBar(toolbar);
        AdminRef = FirebaseDatabase.getInstance().getReference().child("AdminManager");
        three = (RadioButton) findViewById(R.id.withF);
        four = (RadioButton) findViewById(R.id.withoutF);
        five = (RadioButton) findViewById(R.id.withoutFm);
        Address = getIntent().getStringExtra("Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        onel = (TextView) findViewById(R.id.one);
        android_pay = (Button) findViewById(R.id.android_pay);
        Totalo = getIntent().getStringExtra("Totalo");
        cost.setText(getIntent().getStringExtra("Cost"));
        credit = getIntent().getStringExtra("Credit");
        users = getIntent().getStringExtra("us");
        Savings = getIntent().getStringExtra("Savings");
     //   Toast.makeText(this, ""+Savings, Toast.LENGTH_SHORT).show();


        sends = getIntent().getStringExtra("Sends");
        n = Integer.valueOf(sends);
        m = Integer.valueOf(getIntent().getStringExtra("Cost"));
        k = m + n;
        Toast.makeText(this, "" + m, Toast.LENGTH_SHORT).show();
        cartReference = FirebaseDatabase.getInstance().getReference().child("UserCart").child(users);
        placeorderReference = FirebaseDatabase.getInstance().getReference().child("PlacedOrders").child(users);
        Calendar date = Calendar.getInstance();
        Date currentDates = new Date();
        date.setTime(currentDates);
        date.add(Calendar.DATE, 7);
        Date currentDatePlusOne = date.getTime();
        DateFormat dateFormat = new SimpleDateFormat("MMM dd");
        Log.e("DATEIS", "" + dateFormat.format(currentDatePlusOne));
        Delivery_Date = dateFormat.format(currentDatePlusOne).toString();
        onel.setText("" + Address + ".\n\n Delivery expected before: " + Delivery_Date);
        DAddress = onel.getText().toString();
        w = (TextView) findViewById(R.id.totalprice);
        x = (TextView) findViewById(R.id.sellingpricesis);
        y = (TextView) findViewById(R.id.totalis);
        z = (TextView) findViewById(R.id.shippingchargesis);
        v = (TextView) findViewById(R.id.totalmoneyis);
        z.setText("65");
        w.setText("" + Totalo);
        w.setPaintFlags(w.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        int is = n + k;
        y.setText("" + Savings);
        x.setText("" + m);
        is = m + 65;
        v.setText("" + is + "₹");
        paste = String.valueOf(is);
        creditreduce = FirebaseDatabase.getInstance().getReference().child("EndUsers").child(users);
    }

    public void OnRadioChecked(View view) {
        boolean isSelected = ((AppCompatRadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.withF: {
                numberIs = 1;
                four.setChecked(false);
                five.setChecked(false);
                break;
            }
            case R.id.withoutF: {
                numberIs = 2;
                three.setChecked(false);
                five.setChecked(false);
                break;

            }
            case R.id.withoutFm: {
                numberIs = 3;
                three.setChecked(false);
                four.setChecked(false);

                break;

            }
        }
    }

    private void payusingPTM(String idi, String to_printArt, String toStrings) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(urimethod(to_printArt, idi, toStrings));
        if (isPackageInstalled(getPackageManager())) {
            intent.setPackage("net.one97.paytm");
            try {
                startActivityForResult(intent, 123);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(PaymentActivity.this, "Unable to open this Application, Please try another payment method", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PaymentActivity.this, "Selected App is not available in your Device", Toast.LENGTH_SHORT).show();
        }
    }

    private void payusingPPAYUPI(String idi, String to_printArt, String toStringe) {
        PhonePe.init(this);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(urimethod(to_printArt, idi, toStringe));
        if (isPackageInstalled(getPackageManager())) {
            Toast.makeText(PaymentActivity.this, "yes", Toast.LENGTH_SHORT).show();
            i.setPackage("com.phonepe.app");
            try {
                this.startActivityForResult(i, 123);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(PaymentActivity.this, "Unable to open this Application, Please try another payment method", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PaymentActivity.this, "Selected App is not available in your Device", Toast.LENGTH_SHORT).show();
        }
    }

    private void payusingGPAYUPI(String idi, String to_printArt, String toStringo) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(urimethod(to_printArt, idi, toStringo));
        if (isPackageInstalled(getPackageManager())) {
            intent.setPackage("com.google.android.apps.nbu.paisa.user");
            try {
                startActivityForResult(intent, 123);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(PaymentActivity.this, "Unable to open this Application, Please try another payment method", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PaymentActivity.this, "Selected App is not available in your Device", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri urimethod(String idi, String to_printArt, String toStringe) {


        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", idi)
                .appendQueryParameter("pn", to_printArt)
                .appendQueryParameter("tn", toStringe)
                .appendQueryParameter("am", toStringe)
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("tr", "261433")
                .appendQueryParameter("tid", "" + String.valueOf(System.currentTimeMillis()))
                .build();
        return uri;
    }


    protected void onActivityResult(int requestCode, int resultcode, Intent data) {
        super.onActivityResult(requestCode, resultcode, data);
        if (data == null) {
            Toast.makeText(this, "Incomplet Transaction", Toast.LENGTH_SHORT).show();
        } else {
            String trnsID = data.getStringExtra("response");
            if (trnsID.contains("SUCCESS") || trnsID.contains("Success")) {

                Calendar date = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                savecurrentdate = currentDate.format(date.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat(" HH:mm:ss a");
                savecurrenttime = currentTime.format(date.getTime());

                productrandomkey = savecurrentdate + savecurrenttime;
                trnsID = null;
                placeorders(cartReference, placeorderReference);
            } else {
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
                Calendar date = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                savecurrentdate = currentDate.format(date.getTime());
                SimpleDateFormat currentTime = new SimpleDateFormat(" HH:mm:ss a");
                savecurrenttime = currentTime.format(date.getTime());
                productrandomkey = savecurrentdate + savecurrenttime;
                trnsID = null;
                placeorders(cartReference, placeorderReference);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Gpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payusingGPAYUPI("To PrintArt", idi, paste);
            }
        });
        Ppay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payusingPPAYUPI("To PrintArt", idi, paste);
            }
        });
        Ptm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                payusingPTM("To PrintArt", idi, paste);
            }
        });
        android_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberIs == 1) {
                    Gpay.callOnClick();
                } else if (numberIs == 2) {
                    Ppay.callOnClick();
                } else if (numberIs == 3) {
                    Ptm.callOnClick();
                } else {
                    Toast.makeText(PaymentActivity.this, "Select your method of Payment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void placeorders(final DatabaseReference cartReference, final DatabaseReference placeorderReference) {
        cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                one = dataSnapshot.child("Total").getValue().toString();
                placeorderReference.child(productrandomkey).child("01").setValue(dataSnapshot.child("01").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (Integer.valueOf(one) == (k)) {
                                //Toast.makeText(PaymentActivity.this, "Perfect", Toast.LENGTH_SHORT).show();
                                placeorderReference.child(productrandomkey).child("Status").setValue("Fine");
                                placing();

                            } else {
                                placing();
                                placeorderReference.child(productrandomkey).child("Status").setValue("Suspecious");
                                //Toast.makeText(PaymentActivity.this, "Fraud", Toast.LENGTH_SHORT).show();
                            }

                            creditreduce.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        int teens = Integer.valueOf(dataSnapshot.child("Cpoints").getValue().toString());
                                        teens = teens - n;
                                        creditreduce.child("Cpoints").setValue("" + teens);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Intent intent = new Intent(PaymentActivity.this, GreetingsActivity.class);
                            intent.putExtra("Savings", "" + Savings);
                            intent.putExtra("Sends", "" + n);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                    private void placing() {
                        placeorderReference.child(productrandomkey).child("DeliveryDate").setValue("" + Delivery_Date);
                        placeorderReference.child(productrandomkey).child("Original").setValue("" + paste);
                        placeorderReference.child(productrandomkey).child("Address").setValue(DAddress);
                        placeorderReference.child(productrandomkey).child("Credit").setValue("" + n);
                        placeorderReference.child(productrandomkey).child("Total").setValue("" + one);
                        placeorderReference.child(productrandomkey).child("KeyValue").setValue("" + productrandomkey);
                        placeorderReference.child(productrandomkey).child("orderstatus").setValue("Placed Successfully");
                        placeorderReference.child(productrandomkey).child("Phone").setValue(""+users);
                        cartReference.child("Total").setValue("0");
                        cartReference.child("Totalo").setValue("0");
                        cartReference.child("01").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(PaymentActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                    adminaccess(placeorderReference.child(productrandomkey), AdminRef);
                                }
                            }

                            private void adminaccess(DatabaseReference child, final DatabaseReference adminRef) {
                            child.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    adminRef.child(productrandomkey
                                    ).setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(PaymentActivity.this, "We are Started working on it!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private boolean isPackageInstalled(PackageManager packageManager) {
        try {
            if (numberIs == 1) {
                packageManager.getPackageInfo("com.google.android.apps.nbu.paisa.user", 0);
                return true;
            } else if (numberIs == 2) {
                packageManager.getPackageInfo("com.phonepe.app", 0);
                return true;
            } else if (numberIs == 3) {
                packageManager.getPackageInfo("net.one97.paytm", 0);
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
