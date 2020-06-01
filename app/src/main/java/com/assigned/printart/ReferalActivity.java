package com.assigned.printart;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReferalActivity extends AppCompatActivity {
    String name, contact, temp, referalcode, coco;
    TextView welcome, skip, referafrnd;
    ImageView dpicture;
    EditText code;
    Button reg;
    int t;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referal);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("Refer and Earn");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        name = getIntent().getStringExtra("name");
        contact = getIntent().getStringExtra("contact");
        dpicture = (ImageView) findViewById(R.id.dpicture);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        welcome = (TextView) findViewById(R.id.welcome);
        skip = (TextView) findViewById(R.id.skip);
        welcome.setText("Hi " + name + ", Received a referral code?");
        referafrnd = (TextView) findViewById(R.id.referfrnds);
        code = (EditText) findViewById(R.id.code);
        reg = (Button) findViewById(R.id.reg);
    }

    @Override
    protected void onStart() {
        super.onStart();

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 8) {
                    reg.setEnabled(true);
                    reg.setTextColor(Color.WHITE);
                } else {
                    reg.setEnabled(false);
                    reg.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReferalActivity.this, HomeActivity.class);
                finish();
                startActivity(intent);
            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    referalcode = dataSnapshot.child("EndUsers").child(contact).child("Rcode").getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referafrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referlink();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coco = code.getText().toString();
                databaseReference.child("ListRcode").child(coco).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            coco = dataSnapshot.getValue().toString();

                            databaseReference.child("EndUsers").child(coco).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        t = Integer.valueOf(dataSnapshot.child("Cpoints").getValue().toString());
                                        t = t + 50;
                                        databaseReference.child("EndUsers").child(coco).child("Cpoints").setValue("" + t);
                                        databaseReference.child("EndUsers").child(contact).child("Cpoints").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    t = Integer.valueOf(dataSnapshot.getValue().toString());
                                                    Log.e("ValueofT1", "" + t);
                                                    t = t + 20;
                                                    Log.e("ValueofT2", "" + t);
                                                    databaseReference.child("EndUsers").child(contact).child("Cpoints").setValue(t);
                                                    Toast.makeText(ReferalActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(ReferalActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(ReferalActivity.this, "Invalid referral code!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }
    private void referlink() {
        Log.e("DLINK", "CreateLink");
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://printart-digital-printing-service.business.site/"))
                .setDynamicLinkDomain("printart.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        Log.e("DLINK", "" + dynamicLink.getUri());

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink.getUri())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("DLINK", "SHORT " + shortLink);
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(intent.EXTRA_TEXT, shortLink.toString() + " Referal code: " + referalcode);
                            intent.setType("text/plain");
                            startActivity(intent);


                        } else {
                            // Error
                            // ...
                            Log.e("DLINK", "ERROR " + task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(ReferalActivity.this, HomeActivity.class);
        finish();
        startActivity(intent);
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ReferalActivity.this, HomeActivity.class);
        finish();
        startActivity(intent);
    }
}
