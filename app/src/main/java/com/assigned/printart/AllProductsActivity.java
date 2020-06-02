package com.assigned.printart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assigned.printart.Model.DisplayCategory;
import com.assigned.printart.Viewer.AllProductsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class AllProductsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String passer;
    String teen;
    DatabaseReference DisplayReference;
    FirebaseRecyclerAdapter<DisplayCategory, AllProductsViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("Explore More!");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        passer = getIntent().getStringExtra("Type");
        DisplayReference = FirebaseDatabase.getInstance().getReference().child("ProductCategory");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        Log.e("ONE++", "" + passer);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query sorter=DisplayReference;

        if(passer.equals("Default"))
        {
            sorter=DisplayReference;

        }
        else {
            sorter = DisplayReference.orderByChild("Art").startAt(this.passer);
        }

        FirebaseRecyclerOptions<DisplayCategory> options = new FirebaseRecyclerOptions.Builder<DisplayCategory>()
                .setQuery(sorter, DisplayCategory.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<DisplayCategory, AllProductsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AllProductsViewHolder holder, int position, @NonNull final DisplayCategory model) {
                holder.textis.setText(model.getArt());
                String temp = model.getArtPic();
                Picasso.get().load(temp).into(holder.imageis);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AllProductsActivity.this, DisplayProductActivity.class);
                        intent.putExtra("TypeID", "02");
                        intent.putExtra("Category", "" + model.getCategoryID());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public AllProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allproducts, parent, false);
                AllProductsViewHolder holder = new AllProductsViewHolder(view);

                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
