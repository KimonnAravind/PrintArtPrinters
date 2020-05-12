package com.assigned.printart;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.assigned.printart.Adapter.ProductAdapter;
import com.assigned.printart.FirebListen.ProductFirebaseViewer;
import com.assigned.printart.Model.DisplayProducts;
import com.assigned.printart.Model.ProductBanners;
import com.assigned.printart.Paper.PaperStore;
import com.assigned.printart.Transform.Transformer;
import com.assigned.printart.Viewer.Bottom;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;

public class ShowDetailsActivity extends AppCompatActivity implements ProductFirebaseViewer, BottomNavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
    String DisplayID, CategoryID;
    ViewPager products;
    ProductAdapter aptr;
    int tot, tot1, tot2;
    String qty = "1";
    DatabaseReference databaseReferenceforwl;
    String times;
    ProductFirebaseViewer productFirebaseViewer;
    DatabaseReference Productbanner, wishListReference;
    private List<ProductBanners> productBannersList = new ArrayList<>();
    private int currentposition = 0;
    private DatabaseReference ProductDetailsRef;
    TextView t1, t2, t3, t4, t5, t6,offs;

    DatabaseReference databaseReference;
    private TextView Pname, PDes, POprice, PSprice, Sellers;
    RecyclerView recyclerView;

    String strs;
    Spinner spin;
    RecyclerView.LayoutManager manager;
    int x = 10, y, z;
    String h = "0";
    HashMap<String, Object> PriceMap = new HashMap<>();
    ImageView addtowishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        DisplayID = getIntent().getStringExtra("Display");
        CategoryID = getIntent().getStringExtra("Category");
        Toast.makeText(this, "" + CategoryID, Toast.LENGTH_SHORT).show();
        times = getIntent().getStringExtra("Time");
        h = getIntent().getStringExtra("IDs");
        offs = (TextView)findViewById(R.id.offerp);
        t1 = (TextView) findViewById(R.id.type1);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        strs = Paper.book().read(PaperStore.UserLoginID);
        databaseReferenceforwl = FirebaseDatabase.getInstance().getReference().child("WishList").child(strs);
        wishListReference = FirebaseDatabase.getInstance().getReference().child("UserCart").child(strs);
        //    Toast.makeText(this, "" + DisplayID, Toast.LENGTH_SHORT).show();
        Toolbar toolbar = findViewById(R.id.toolbarS);
        toolbar.setTitle("PrintArt Sivakasi");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        t2 = (TextView) findViewById(R.id.type2);
        t3 = (TextView) findViewById(R.id.type3);
        t4 = (TextView) findViewById(R.id.type4);
        spin = (Spinner) findViewById(R.id.spinningID);
        spin.setOnItemSelectedListener(this);
        spin.setPrompt("QTY");
        t5 = (TextView) findViewById(R.id.type5);
        t6 = (TextView) findViewById(R.id.type6);

        addtowishlist = (ImageView) findViewById(R.id.addtowishlist);

        productFirebaseViewer = this;
        manager = new LinearLayoutManager(this);

        Productbanner = FirebaseDatabase.getInstance().getReference().child("ShowingProducts");
        BottomNavigationView navigation = findViewById(R.id.bottom_navigationes);
        navigation.setOnNavigationItemSelectedListener(this);

        ProductDetailsRef = FirebaseDatabase.getInstance().getReference().child("ShowingProducts");

        products = (ViewPager) findViewById(R.id.productviewerpage);
        loadimages();
        products.setPageTransformer(true, new Transformer());
        Pname = (TextView) findViewById(R.id.ProductName);
        PDes = (TextView) findViewById(R.id.ProductDescription);
        POprice = (TextView) findViewById(R.id.originalPrice);
        PSprice = (TextView) findViewById(R.id.sellingprice);
        Sellers = (TextView) findViewById(R.id.sell);

        //   Toast.makeText(this, "" + times, Toast.LENGTH_SHORT).show();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewin);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Productbanner.child(CategoryID).child(DisplayID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Pname.setText(dataSnapshot.child("Pame").getValue().toString());
                    PDes.setText(dataSnapshot.child("Pdes").getValue().toString());
                    POprice.setText("₹" + dataSnapshot.child("PpriceO").getValue().toString());
                    y = Integer.parseInt(dataSnapshot.child("Psp").getValue().toString());
                    z = Integer.parseInt(dataSnapshot.child("PpriceO").getValue().toString());
                    POprice.setPaintFlags(POprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    PSprice.setText("₹" + dataSnapshot.child("Psp").getValue().toString());
                    Sellers.setText(" "+dataSnapshot.child("Seller").getValue().toString()+" ");
                    t1.setText(dataSnapshot.child("type").getValue().toString());
                    t2.setText(dataSnapshot.child("type1").getValue().toString());
                    t3.setText(dataSnapshot.child("type2").getValue().toString());
                    t4.setText(dataSnapshot.child("type3").getValue().toString());
                    t5.setText(dataSnapshot.child("type4").getValue().toString());
                    t6.setText(dataSnapshot.child("type5").getValue().toString());
                    int percent =y  * 100 / z;
                    offs.setText(""+(100-percent)+"%offer");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.other, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<DisplayProducts> options =
                new FirebaseRecyclerOptions.Builder<DisplayProducts>().setQuery(ProductDetailsRef.child(CategoryID), DisplayProducts.class)
                        .build();
        FirebaseRecyclerAdapter<DisplayProducts, Bottom> optionis = new FirebaseRecyclerAdapter<DisplayProducts, Bottom>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Bottom holder, int position, @NonNull final DisplayProducts model) {
                /*holder.name.setText(model.getPame());
                holder.description.setText(model.getPdes());
                holder.pop.setText(model.getPpriceO());
                holder.pop.setPaintFlags(holder.pop.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.psp.setText("₹" + model.getPsp());
                Picasso.get().load(model.getPro()).into(holder.pics);
*/
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

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ShowDetailsActivity.this, ShowDetailsActivity.class);
                        intent.putExtra("Display", model.getProID());
                        intent.putExtra("Category", CategoryID);
                        intent.putExtra("Time", "Third");
                        startActivity(intent);
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
            public Bottom onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.displayproductactivitydesign, parent, false);
                Bottom holder = new Bottom(view);
                return holder;
            }
        };
        recyclerView.setAdapter(optionis);
        optionis.startListening();


        addtowishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtoWL(ProductDetailsRef.child(CategoryID).child(DisplayID), databaseReferenceforwl);
            }

            private void addtoWL(DatabaseReference child, final DatabaseReference databaseReferenceforwl) {
                child.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        databaseReferenceforwl.child(DisplayID).setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ShowDetailsActivity.this, "Added to Wishlist!", Toast.LENGTH_SHORT).show();
                                }
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

    @Override
    public void Loadsuccess(List<ProductBanners> productBannersList) {
        aptr = new ProductAdapter(this, productBannersList);
        products.setAdapter(aptr);
    }

    @Override
    public void Loadfailed(String string) {
    }

    private void loadimages() {
        Productbanner.child(CategoryID).child(DisplayID).child("images").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot productbannersnapshot : dataSnapshot.getChildren())
                    productBannersList.add(productbannersnapshot.getValue(ProductBanners.class));
                productFirebaseViewer.Loadsuccess(productBannersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                productFirebaseViewer.Loadfailed(databaseError.getMessage());
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //   Toast.makeText(this, "Repeating", Toast.LENGTH_SHORT).show();
        final int id = menuItem.getItemId();

        if (id == R.id.action_settings) {

            if (!strs.equals("0000000000")) {
                {
                    wishListReference.child(DisplayID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(ShowDetailsActivity.this, "Yes", Toast.LENGTH_SHORT).show();


                                wishListReference.child(DisplayID).child("quantity").setValue(qty);
                                Log.e("YESSS", "one");


                            } else {

                                addtocart(ProductDetailsRef.child(CategoryID).child(DisplayID), wishListReference);
                                Log.e("YESSS", "two");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            } /*else if (times.equals("Second")) {
                wishListReference.child(DisplayID).child("quantity").setValue(qty);
                Toast.makeText(this, "Clickl" + qty, Toast.LENGTH_SHORT).show();
            } */ else {

                Toast.makeText(ShowDetailsActivity.this, "Cannot Add", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShowDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.action_settingsa) {

            Intent intent = new Intent(ShowDetailsActivity.this, CartActivity.class);
            addtocart(ProductDetailsRef.child(CategoryID).child(DisplayID), wishListReference);
            intent.putExtra("us", strs);
            startActivity(intent);
        }
        return true;
    }

    private void addtocart(final DatabaseReference child, final DatabaseReference child1) {

        wishListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("01").child(DisplayID).exists()) {


                    int qw = Integer.valueOf(dataSnapshot.child("01").child(DisplayID).child("quantity").getValue().toString());


                    int qe = Integer.valueOf(dataSnapshot.child("01").child(DisplayID).child("Psp").getValue().toString());

                    int ae = Integer.valueOf(dataSnapshot.child("01").child(DisplayID).child("PpriceO").getValue().toString());

                    int existo = qw * ae;
                    int exist = qw * qe;

                    int ar = Integer.valueOf(dataSnapshot.child("Totalo").getValue().toString());
                    int qr = Integer.valueOf(dataSnapshot.child("Total").getValue().toString());


                    ar = ar - existo;
                    qr = qr - exist;

                    wishListReference.child("Totalo").setValue("" + ar);
                    wishListReference.child("Total").setValue("" + qr);


                    wishListReference.child("01").child(DisplayID).child("quantity").setValue(qty);

                    int newv = (qe * Integer.valueOf(qty)) + qr;
                    int newo = (ae * Integer.valueOf(qty) + ar);

                    wishListReference.child("Totalo").setValue("" + newo);
                    wishListReference.child("Total").setValue("" + newv);


                } else {
                    Toast.makeText(ShowDetailsActivity.this, "New Entry", Toast.LENGTH_SHORT).show();
                    if (times.equals("First")) {
                        child.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                child1.child("01").child(DisplayID).setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ShowDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                            child1.child("01").child(DisplayID).child("quantity").setValue("" + qty);
                                            tot = Integer.valueOf(qty);
                                            wishListReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        tot1 = Integer.valueOf(dataSnapshot.child("01").child(DisplayID).child("Psp").getValue().toString());
                                                        tot2 = Integer.valueOf(dataSnapshot.child("01").child(DisplayID).child("PpriceO").getValue().toString());
                                                        int exist = Integer.valueOf(dataSnapshot.child("Total").getValue().toString());
                                                        int existo = Integer.valueOf(dataSnapshot.child("Totalo").getValue().toString());

                                                        wishListReference.child("Totalo").setValue(existo + (tot * tot2));
                                                        wishListReference.child("Total").setValue(exist + (tot * tot1));
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

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else if (times.equals("Second")) {
                        //    Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
                        times = "First";
                        addtocart(ProductDetailsRef.child(CategoryID).child(DisplayID), wishListReference);
                    } else if (times.equals("Third")) {
                        //    Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
                        times = "First";
                        addtocart(ProductDetailsRef.child(CategoryID).child(DisplayID), wishListReference);
                    } else if (times.equals("Fourth")) {
                        //  Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
                        times = "First";
                        addtocart(ProductDetailsRef.child(CategoryID).child(DisplayID), wishListReference);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * <p>Callback method to be invoked when an item in this view has been
     * selected. This callback is invoked only when the newly selected
     * position is different from the previously selected position or if
     * there was no selected item.</p>
     * <p>
     * Implementers can call getItemAtPosition(position) if they need to access the
     * data associated with the selected item.
     *
     * @param parent   The AdapterView where the selection happened
     * @param view     The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        qty = parent.getSelectedItem().toString();
        Toast.makeText(this, "Val is" + parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }


    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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
