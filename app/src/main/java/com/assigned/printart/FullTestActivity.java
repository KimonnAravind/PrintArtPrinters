package com.assigned.printart;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assigned.printart.Model.DisplayProducts;
import com.assigned.printart.Viewer.MyordersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FullTestActivity extends AppCompatActivity {
    String keyV;
    TextView currentStatus,placedDate, current;
    String Orders;
    ImageView ims;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private int[] imageViewAr;
    DatabaseReference DisplayReference;
    TextView trackID, trackLink, track;
    FirebaseRecyclerAdapter<DisplayProducts, MyordersViewHolder> adapter;
    String Contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_test);
        keyV = getIntent().getStringExtra("KeyV");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Contact = getIntent().getStringExtra("Contact");
        Orders = getIntent().getStringExtra("status");
        DisplayReference = FirebaseDatabase.getInstance().getReference().child("PlacedOrders").child(Contact).child(keyV);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        placedDate = (TextView)findViewById(R.id.placedDate);
        current = (TextView) findViewById(R.id.current);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("Order Status");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ims = (ImageView) findViewById(R.id.ims);
        imageViewAr = new int[]{
                R.drawable.bb,
                R.drawable.cc,
                R.drawable.d,
                R.drawable.e};
        trackID = (TextView) findViewById(R.id.trackID);
        trackLink = (TextView) findViewById(R.id.tracklink);
        track = (TextView) findViewById(R.id.track);
        currentStatus = (TextView) findViewById(R.id.currentStauts);
        currentStatus.setText("" + Orders);
    }

    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<DisplayProducts> options = new FirebaseRecyclerOptions.Builder<DisplayProducts>()
                .setQuery(DisplayReference.child("01"), DisplayProducts.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<DisplayProducts, MyordersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyordersViewHolder holder, int position, @NonNull final DisplayProducts model) {
                holder.Pname.setText(model.getPame());
                holder.PSPrice.setText("₹" + model.getPsp());
                holder.POPrice.setText("₹" + model.getPpriceO() + " ");
                holder.Pdes.setText(model.getPdes());
                holder.Seller.setText(" " + model.getSeller() + " ");
                holder.POPrice.setPaintFlags(holder.POPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.b1.setText(" " + model.getType() + " ");
                holder.b2.setText(" " + model.getType1() + " ");
                holder.b3.setText(" " + model.getType2() + " ");
                int percent = Integer.valueOf(model.getPsp()) * 100 / Integer.valueOf(model.getPpriceO());
                holder.discount.setText("" + (100 - percent) + "%offer");
                Picasso.get().load(model.getPro()).into(holder.imgv);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FullTestActivity.this, DisplayProductActivity.class);
                        intent.putExtra("Category", model.getCategory());
                        intent.putExtra("TypeID", "01");

                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MyordersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_viewholder, parent, false);
                MyordersViewHolder holder = new MyordersViewHolder(view);

                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        placedDate.setText("Ordered on: \n"+keyV);
    }

    private void showTrackingDetails() {
        track.setVisibility(View.VISIBLE);
        trackLink.setVisibility(View.VISIBLE);
        trackID.setVisibility(View.VISIBLE);
        DisplayReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("TLink").exists()) {
                    trackLink.setText(dataSnapshot.child("TLink").getValue().toString());
                    trackID.setText(dataSnapshot.child("TId").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return super.onNavigateUp();
    }
}
