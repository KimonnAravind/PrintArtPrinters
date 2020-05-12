package com.assigned.printart;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assigned.printart.Model.DisplayProducts;
import com.assigned.printart.Paper.PaperStore;
import com.assigned.printart.Viewer.DisplayProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class DisplayProductActivity extends AppCompatActivity {
    public RecyclerView recyclerViewdisplay;
    RecyclerView.LayoutManager layoutManager;
    //  GridLayoutManager gridLayoutManager;
    private String TypeID;
    Query sorting;
    private String CategoryID;

    private DatabaseReference DisplayReference, wishListReference;
    String str1;
    FirebaseRecyclerAdapter<DisplayProducts, DisplayProductViewHolder> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_product);
        str1 = Paper.book().read(PaperStore.UserLoginID);
        Toast.makeText(this, "" + str1, Toast.LENGTH_SHORT).show();
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("PrintArt");
        toolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerViewdisplay = (RecyclerView) findViewById(R.id.recyclerViewDisplay);
        recyclerViewdisplay.setHasFixedSize(false);
        //gridLayoutManager=new GridLayoutManager(getApplicationContext(),2);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        CategoryID = getIntent().getStringExtra("Category");
        TypeID = getIntent().getStringExtra("TypeID");
        DisplayReference = FirebaseDatabase.getInstance().getReference().child("ShowingProducts").child(CategoryID);
        wishListReference = FirebaseDatabase.getInstance().getReference().child("WishList").child(str1);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewdisplay.setLayoutManager(layoutManager);
        Toast.makeText(this, "" + TypeID, Toast.LENGTH_SHORT).show();
        if (TypeID.equals("01")) {
            sorting = DisplayReference.orderByChild("sort1");
        } else if (TypeID.equals("02")) {
            sorting = DisplayReference.orderByChild("sort2");
        } else if (TypeID.equals("03")) {
            sorting = DisplayReference.orderByChild("sort3");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<DisplayProducts> options =
                new FirebaseRecyclerOptions.Builder<DisplayProducts>().setQuery(sorting, DisplayProducts.class)
                        .build();


        adapter2 = new FirebaseRecyclerAdapter<DisplayProducts, DisplayProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DisplayProductViewHolder holder, int position, @NonNull final DisplayProducts model) {
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
                Picasso.get().load(model.getPro()).into(holder.imgv);
                holder.locl_buttons.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Snackbar.make(v, "Added to Wish List", Snackbar.LENGTH_LONG)
                                .setAction("", null).show();

                        if (!str1.equals("0000000000")) {
                            copyrecord(DisplayReference.child(model.getProID()), wishListReference.child(model.getProID()));
                        } else {
                            Toast.makeText(DisplayProductActivity.this, "Cannot Add", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DisplayProductActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(DisplayProductActivity.this, ShowDetailsActivity.class);
                        intent.putExtra("Display", model.getProID());
                        intent.putExtra("Category", CategoryID);
                        intent.putExtra("Time", "First");

                        Log.e("ISARE", model.getProID());
                        Log.e("ISARE+", CategoryID);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public DisplayProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.displayproductactivitydesign, parent, false);
                DisplayProductViewHolder holder = new DisplayProductViewHolder(view);

                return holder;
            }

        };
        recyclerViewdisplay.setAdapter(adapter2);
        adapter2.startListening();


    }

    private void copyrecord(DatabaseReference displayReference, final DatabaseReference wishListReference) {

        displayReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                wishListReference.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(DisplayProductActivity.this, "Added to wishlist!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
