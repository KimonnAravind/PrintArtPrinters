package com.assigned.printart;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assigned.printart.Model.Test;
import com.assigned.printart.Viewer.TeseViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class TestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference DisplayReference;
    FirebaseRecyclerAdapter<Test, TeseViewHolder> adapter;
    String Contact;

    private int[] textureArrayWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        DisplayReference = FirebaseDatabase.getInstance().getReference().child("PlacedOrders");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        Contact = getIntent().getStringExtra("Contact");
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("My Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textureArrayWin = new int[]{
                R.drawable.sta2,
                R.drawable.sta3,
                R.drawable.sta4,
                R.drawable.sta5
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Test> options = new FirebaseRecyclerOptions.Builder<Test>()
                .setQuery(DisplayReference.child(Contact), Test.class)
                .build();
        /*adapter = new FirebaseRecyclerAdapter<Test,TeseViewHolder>(options)*/
        adapter = new FirebaseRecyclerAdapter<Test, TeseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TeseViewHolder holder, int position, @NonNull final Test model) {
                holder.testtexview.setText("Order ID: " + model.getKeyValue());
                holder.deliverydate.setText("Expected delivery date:\n" + model.getDeliveryDate());

                holder.credit.setText("Paid: ₹" + model.getOriginal());
                Picasso.get().load(model.getThumN()).into(holder.statuspic);

                int temp =Integer.valueOf( model.getNOP());
                temp = temp-1;

                if (model.getNOP().equals("1")) {
                    holder.cost.setText(model.getNameop());
                } else if (model.getNOP().equals("2")) {
                    holder.cost.setText(model.getNameop() + " + 1 other product");
                } else {
                    holder.cost.setText(model.getNameop() + " + " + temp + " other products");
                }
                switch (model.getOrderstatus()) {
                    case "Approved": {
                        holder.orderstatusbar.setImageResource(textureArrayWin[0]);
                        break;
                    }
                    case "Packed": {
                        holder.orderstatusbar.setImageResource(textureArrayWin[1]);
                        break;
                    }
                    case "Dispatched": {
                        holder.orderstatusbar.setImageResource(textureArrayWin[2]);
                        break;
                    }
                    case "Delivered": {
                        holder.orderstatusbar.setImageResource(textureArrayWin[3]);
                        break;
                    }
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TestActivity.this, FullTestActivity.class);
                        intent.putExtra("Contact", Contact);
                        intent.putExtra("KeyV", "" + model.getKeyValue());
                        intent.putExtra("status", "" + model.getOrderstatus());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public TeseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.testingviewholder, parent, false);
                TeseViewHolder holder = new TeseViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
