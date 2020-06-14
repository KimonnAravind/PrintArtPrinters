package com.assigned.printart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.assigned.printart.Adapter.HorizontalAdapter;
import com.assigned.printart.Adapter.YourDesignsPage;
import com.assigned.printart.Model.HorizontalScroller;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class YourDesignsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    YourDesignsPage adapterH;
    DatabaseReference banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_designs);
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("PrintArt");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        banner = FirebaseDatabase.getInstance().getReference().child("Banner");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        FirebaseRecyclerOptions<HorizontalScroller> optionsb =
                new FirebaseRecyclerOptions.Builder<HorizontalScroller>()
                        .setQuery(banner, HorizontalScroller.class).build();
        adapterH = new YourDesignsPage(optionsb);
        recyclerView.setAdapter(adapterH);
        adapterH.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}