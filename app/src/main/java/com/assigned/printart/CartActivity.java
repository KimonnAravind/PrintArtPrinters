package com.assigned.printart;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assigned.printart.Model.DisplayProducts;
import com.assigned.printart.Viewer.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    RecyclerView.LayoutManager layoutManager;
    String users = "";
    int sends = 0;
    private TextView deliveryAdd;
    Button changeAdd, updateadd;
    private EditText deliveryless, pinc;
    int couty = 0;
    int temp, pay, as;
    String totalo;
    TextView total;
    String pins;
    int credit, original;
    Button nxt;
    String AddressPassing;
    DatabaseReference pricereference;
    int xx = 0, yy = 0;
    String ss;
    TextView TotalAmount, Discount, SellingPrice, OriginalPriceIS, Apoints;
    CheckBox checkBox;
    DatabaseReference databaseReference, AddressReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        deliveryAdd = (TextView) findViewById(R.id.deliveryA);
        deliveryless = (EditText) findViewById(R.id.deliveryB);
        pinc = (EditText) findViewById(R.id.deliveryPin);
        users = getIntent().getStringExtra("us");
        changeAdd = (Button) findViewById(R.id.changeAdds);
        updateadd = (Button) findViewById(R.id.addorub);
        total = (TextView) findViewById(R.id.total);
        //users = Paper.book().read(PaperStore.UserLoginID);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("Cart");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Toolbar toolbars = findViewById(R.id.toolbarsS);
        toolbars.setTitle("Empty cart");
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        nxt = (Button) findViewById(R.id.nxt);
        TotalAmount = (TextView) findViewById(R.id.totalamount);
        Discount = (TextView) findViewById(R.id.discount);
        SellingPrice = (TextView) findViewById(R.id.sellingprice);
        OriginalPriceIS = (TextView) findViewById(R.id.originalpriceIS);
        Apoints = (TextView) findViewById(R.id.apoints);
        checkBox = (CheckBox) findViewById(R.id.creditbox);
        AddressReference = FirebaseDatabase.getInstance().getReference().child("EndUsers").child(users);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserCart")
                .child(users);
        pricereference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child("01").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    relativeLayout.setVisibility(View.INVISIBLE);
                } else {
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        AddressReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    credit = Integer.valueOf(dataSnapshot.child("Cpoints").getValue().toString()) / 2;
                    original = Integer.valueOf(dataSnapshot.child("Cpoints").getValue().toString());
                    Apoints.setText("Available Points: " + original);
                    pins = dataSnapshot.child("Pincode").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        pricereference.child("UserCart").child(users).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    OriginalPriceIS.setText("" + dataSnapshot.child("Totalo").getValue().toString());
                    OriginalPriceIS.setPaintFlags(OriginalPriceIS.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    totalo = dataSnapshot.child("Totalo").getValue().toString();
                    SellingPrice.setText("" + dataSnapshot.child("Total").getValue().toString());
                    pay = Integer.valueOf(dataSnapshot.child("Total").getValue().toString());
                    TotalAmount.setText("" + pay + "₹");
                    total.setText("PROCEED TO PLACE AN ORDER: " + pay);
                    temp = Integer.valueOf(dataSnapshot.child("Totalo").getValue().toString()) - Integer.valueOf(dataSnapshot.child("Total").getValue().toString());
                    Discount.setText("" + temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (credit >= pay) {
                        int ten;
                        ten = pay;
                        credit = credit - ten;
                        ten = credit + ten;
                        credit = ten - credit;

                        sends = credit;
                        temp = temp + credit;
                        pay = pay - credit;
                        Discount.setText("" + temp);
                        TotalAmount.setText("" + pay);
                        total.setText("PROCEED TO PLACE AN ORDER" + pay);
                        as = original - credit;
                        Apoints.setText("Available Points: " + as);
                    } else {
                        sends = credit;
                        temp = temp + credit;
                        pay = pay - credit;
                        Discount.setText("" + temp);
                        TotalAmount.setText("" + pay);
                        total.setText("PROCEED TO PLACE AN ORDER" + pay);
                        as = original - credit;
                        Apoints.setText("Available Points: " + as);
                    }

                } else {
                    if (credit >= pay) {
                        temp = temp - credit;
                        sends = 0;
                        pay = pay + credit;
                        as = original;
                        Apoints.setText("Available Points: " + as);
                        TotalAmount.setText("" + pay);
                        total.setText("PROCEED TO PLACE AN ORDER" + pay);
                        Discount.setText("" + temp);

                    } else {
                        temp = temp - credit;
                        sends = 0;
                        pay = pay + credit;
                        as = original;
                        Apoints.setText("Available Points: " + as);
                        TotalAmount.setText("" + pay);
                        total.setText("PROCEED TO PLACE AN ORDER" + pay);
                        Discount.setText("" + temp);
                    }


                }
            }
        });


        AddressReference = FirebaseDatabase.getInstance().getReference().child("EndUsers").child(users);
        FirebaseRecyclerOptions<DisplayProducts> options =
                new FirebaseRecyclerOptions.Builder<DisplayProducts>().setQuery(databaseReference.child("01"), DisplayProducts.class)
                        .build();

        FirebaseRecyclerAdapter<DisplayProducts, CartViewHolder> adapter = new FirebaseRecyclerAdapter<DisplayProducts, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, final int position, @NonNull final DisplayProducts model) {
                Picasso.get().load(model.getPro()).into(holder.imgvs);
                holder.d.setText(model.getPdes());
                holder.t.setText(model.getPame());
                holder.o.setText("₹" + model.getPpriceO() + " ");
                holder.o.setPaintFlags(holder.o.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.s.setText("₹" + model.getPsp());
                int x = Integer.valueOf(model.getPsp());
                xx = xx + x;
                Log.e("EXEs", "" + xx);
                holder.sn.setText(model.getSeller());
                holder.tt.setText("QTY: " + model.getQuantity());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CartActivity.this, ShowDetailsActivity.class);
                        intent.putExtra("Display", model.getProID());
                        intent.putExtra("Category", model.getCategory());
                        intent.putExtra("Time", "Second");
                        if (checkBox.isChecked()) {
                            checkBox.toggle();
                        }
                        startActivity(intent);
                    }
                });
                holder.trash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    int te = Integer.valueOf(dataSnapshot.child("01").child(model.getProID()).child("Psp").getValue().toString());
                                    int mp = Integer.valueOf(dataSnapshot.child("01").child(model.getProID()).child("quantity").getValue().toString());
                                    int ms = Integer.valueOf(dataSnapshot.child("01").child(model.getProID()).child("PpriceO").getValue().toString());
                                    int ex = Integer.valueOf(dataSnapshot.child("Total").getValue().toString());
                                    int xe = Integer.valueOf(dataSnapshot.child("Totalo").getValue().toString());

                                    te = te * mp;   //total selling price
                                    ms = ms * mp;   //totalo original price

                                    ex = ex - te;
                                    xe = xe - ms;

                                    databaseReference.child("Total").setValue("" + ex);
                                    databaseReference.child("Totalo").setValue("" + xe);
                                    databaseReference.child("01").child(model.getProID()).removeValue().addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });
            }


            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlistdesign, parent, false);
                //recyclerView.smoothScrollToPosition(5);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        AddressReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Address").exists()) {

                    deliveryAdd.setVisibility(View.VISIBLE);
                    changeAdd.setVisibility(View.VISIBLE);
                    deliveryAdd.setText("Shipping Details: " + "" + dataSnapshot.child("Name").getValue().toString() + ", "
                            + dataSnapshot.child("Address").getValue().toString()
                            + "-" + dataSnapshot.child("Pincode").getValue().toString() + "\n Phone Number: " + users);

                    AddressPassing = dataSnapshot.child("Name").getValue().toString() + ", "
                            + dataSnapshot.child("Address").getValue().toString()
                            + "-" + dataSnapshot.child("Pincode").getValue().toString() + "\n Phone Number: " + users;
                    changeAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deliveryless.setVisibility(View.VISIBLE);
                            updateadd.setVisibility(View.VISIBLE);
                            deliveryAdd.setVisibility(View.INVISIBLE);
                            pinc.setVisibility(View.VISIBLE);
                            deliveryless.setError("Please enter you Address here");
                            deliveryless.requestFocus();

                            changeAdd.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    deliveryless.setVisibility(View.VISIBLE);
                    updateadd.setVisibility(View.VISIBLE);
                    pinc.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        updateadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!deliveryless.getText().toString().isEmpty() && !pinc.getText().toString().isEmpty()) {
                    HashMap<String, Object> DeliveryMap = new HashMap<>();
                    DeliveryMap.put("Address", deliveryless.getText().toString());
                    DeliveryMap.put("Pincode", pinc.getText().toString());
                    AddressReference.updateChildren(DeliveryMap);
                    //      Toast.makeText(CartActivity.this, "Address updated successfully", Toast.LENGTH_SHORT).show();
                    deliveryless.setVisibility(View.INVISIBLE);
                    updateadd.setVisibility(View.INVISIBLE);
                    pinc.setVisibility(View.INVISIBLE);
                    deliveryAdd.setVisibility(View.VISIBLE);
                    changeAdd.setVisibility(View.VISIBLE);
                    AddressReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            deliveryAdd.setText("Deliver to: " + "" + dataSnapshot.child("Name").getValue().toString() + ", "
                                    + dataSnapshot.child("Address").getValue().toString()
                                    + "-" + dataSnapshot.child("Pincode").getValue().toString() + "\n Phone Number: " + users);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    pinc.setError("Update both Address and Pincode");
                }

            }
        });
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pins.equals("xxxxxx")) {
                    Toast.makeText(CartActivity.this, "Please update your delivery address details!", Toast.LENGTH_SHORT).show();
                    changeAdd.callOnClick();
                    //  pins = "0";
                } else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("01").exists()) {
                                passing();
                            } else {
                                Toast.makeText(CartActivity.this, "Select your products before going to Payment step!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }
        });
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pins.equals("xxxxxx")) {
                    Toast.makeText(CartActivity.this, "Please update your delivery address details!", Toast.LENGTH_SHORT).show();
                    changeAdd.callOnClick();
                    //       pins = "0";
                } else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("01").exists()) {
                                passing();
                            } else {
                                Toast.makeText(CartActivity.this, "Select your products before going to Payment step!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });
    }

    private void passing() {
        Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
        intent.putExtra("Cost", "" + pay);
        intent.putExtra("Credit", "" + credit);
        intent.putExtra("us", "" + users);
        intent.putExtra("Address", AddressPassing);
        intent.putExtra("Totalo", "" + totalo);
        intent.putExtra("Savings", "" + temp);
        intent.putExtra("Sends", "" + sends);
        if (checkBox.isChecked()) {
            checkBox.toggle();
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "close", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
        finish();
        if (checkBox.isChecked()) {
            checkBox.toggle();
        }
        startActivity(intent);
        return true;
    }
}