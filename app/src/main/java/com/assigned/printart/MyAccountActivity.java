package com.assigned.printart;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.assigned.printart.Paper.PaperStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class MyAccountActivity extends AppCompatActivity {

    CircleImageView DP;
    TextView uName, pNumber, Rcode, Cpoints, orders, address, logout, wishlist;
    String AccountN, numberis;
    LinearLayout ref;
    private Uri imageUri;
    String Rc;
    String pn;
    TextView refs;
    private DatabaseReference settingReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        AccountN = getIntent().getStringExtra("AccountN");
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle(" My Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        refs = (TextView) findViewById(R.id.refs);
        settingReference = FirebaseDatabase.getInstance().getReference();
        DP = (CircleImageView) findViewById(R.id.dp);
        uName = (TextView) findViewById(R.id.UName);
        pNumber = (TextView) findViewById(R.id.UPhone);
        Rcode = (TextView) findViewById(R.id.Rcode);
        Cpoints = (TextView) findViewById(R.id.Cpoints);
        orders = (TextView) findViewById(R.id.orders);
        address = (TextView) findViewById(R.id.address);
        wishlist = (TextView) findViewById(R.id.wlist);
        logout = (TextView) findViewById(R.id.logoout);
        ref = (LinearLayout) findViewById(R.id.ref);

        try {
            settingReference.child("EndUsers").child(AccountN).
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                uName.setText("               " + dataSnapshot.child("Name").getValue().toString() + "          ");
                                pNumber.setText(dataSnapshot.child("PhoneNumber").getValue().toString());
                                numberis = dataSnapshot.child("PhoneNumber").getValue().toString();
                                Rcode.setText(dataSnapshot.child("Rcode").getValue().toString());
                                Rc = dataSnapshot.child("Rcode").getValue().toString();
                                Cpoints.setText("You can use it when ordering: " + dataSnapshot.child("Cpoints").getValue().toString());
                                address.setText(dataSnapshot.child("Address").getValue().toString() + "-" + dataSnapshot.child("Pincode").getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

        } catch (NullPointerException e) {
            Intent intent = new Intent(MyAccountActivity.this, HomeActivity.class);
            finish();
            startActivity(intent);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, AddressUpdateActivity.class);
                intent.putExtra("Number", numberis);
                startActivity(intent);
            }
        });

        ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Ready to Share!", Snackbar.LENGTH_LONG)
                        .setAction("", null).show();
                Toast.makeText(MyAccountActivity.this, "Ready to Share!", Toast.LENGTH_SHORT).show();
                createlink();
            }
        });
        refs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyAccountActivity.this, "Ready to Share!", Toast.LENGTH_SHORT).show();
                createlink();
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, TestActivity.class);
                intent.putExtra("Contact", numberis);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Paper.book().write(PaperStore.UserLoginID, "0000000000");
                Paper.book().write(PaperStore.UserLoginID, "0000000000");
                Intent intent = new Intent(MyAccountActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, WListACtivity.class);
                intent.putExtra("Contact", numberis);
                startActivity(intent);
            }
        });
    }

    private void createlink() {
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
                            intent.putExtra(intent.EXTRA_TEXT, shortLink.toString() + " Referal code: " + Rc);
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
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_SS) {
            Intent intent = new Intent(MyAccountActivity.this, CartActivity.class);
            intent.putExtra("us", numberis);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
