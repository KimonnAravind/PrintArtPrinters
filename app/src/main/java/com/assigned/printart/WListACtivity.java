package com.assigned.printart;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assigned.printart.Model.DisplayProducts;
import com.assigned.printart.Viewer.DisplayProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class WListACtivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout relativeLayout;
    DatabaseReference databaseReference;
    String contact;
    FloatingActionButton floating_action_button;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlist_activity);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        contact = getIntent().getStringExtra("Contact");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("WishList")
                .child(contact);
        relativeLayout=(RelativeLayout)findViewById(R.id.relative);
        Toolbar toolbar = findViewById(R.id.toolbare);
        toolbar.setTitle(" Wishlist");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Toolbar toolbars = findViewById(R.id.toolbarsS);
        toolbars.setTitle("Empty cart");
        setSupportActionBar(toolbars);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        floating_action_button = (FloatingActionButton) findViewById(R.id.floating_action_button);
        FirebaseRecyclerOptions<DisplayProducts> options =
                new FirebaseRecyclerOptions.Builder<DisplayProducts>().setQuery(databaseReference, DisplayProducts.class)
                        .build();
        FirebaseRecyclerAdapter<DisplayProducts, DisplayProductViewHolder> adapter = new FirebaseRecyclerAdapter<DisplayProducts, DisplayProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DisplayProductViewHolder holder, int position, @NonNull final DisplayProducts model) {

                holder.Pname.setText(model.getPame());
                holder.PSPrice.setText("₹" + model.getPsp());
                holder.POPrice.setText("₹" + model.getPpriceO() + " ");
                holder.Pdes.setText(model.getPdes());
                holder.Seller.setText(" "+model.getSeller()+" ");
                holder.POPrice.setPaintFlags(holder.POPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.b1.setText(" "+model.getType()+" ");
                holder.b2.setText(" "+model.getType1()+" ");
                holder.b3.setText(" "+model.getType2()+" ");
                int percent =Integer.valueOf(model.getPsp())  * 100 / Integer.valueOf(model.getPpriceO());
                holder.discount.setText(""+(100-percent)+"%offer");
                Picasso.get().load(model.getPro()).into(holder.imgv);  holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WListACtivity.this, ShowDetailsActivity.class);
                        intent.putExtra("Display", model.getProID());
                        intent.putExtra("Category", model.getCategory());
                        intent.putExtra("Time", "Fourth");
                        startActivity(intent);
                    }
                });
                holder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(model.getProID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(WListACtivity.this, "Removed from Wishlist!" + model.getProID(), Toast.LENGTH_SHORT).show();
                                }
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
            public DisplayProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View views = LayoutInflater.from(parent.getContext()).inflate(R.layout.wlistdesign, parent, false);
                DisplayProductViewHolder holder = new DisplayProductViewHolder(views);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
           relativeLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    relativeLayout.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onStart();
        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WListACtivity.this, CartActivity.class);
                intent.putExtra("us", contact);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
