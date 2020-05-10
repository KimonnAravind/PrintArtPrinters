package com.assigned.printart;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.assigned.printart.Adapter.HorizontalAdapter;
import com.assigned.printart.FirebListen.FirebaseViewer;
import com.assigned.printart.Model.Banners;
import com.assigned.printart.Model.DisplayCategory;
import com.assigned.printart.Model.HorizontalScroller;
import com.assigned.printart.Model.NestedCategory;
import com.assigned.printart.Paper.PaperStore;
import com.assigned.printart.Viewer.CategoryViewHolder;
import com.assigned.printart.Viewer.NestedCategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    String typepasser;
    private Timer timer;
    ImageView wpimg, mc, mcs, psk, fr, vc;
    private LinearLayout dotslayout;
    AutoCompleteTextView autoCompleteTextView;
    int currentPosition = 0;
    private DatabaseReference EndUserPortal;
    FirebaseViewer firebaseViewer;
    HorizontalAdapter adapterH;
    List<Banners> bannersList = new ArrayList<>();
    DatabaseReference reference;
    DatabaseReference banner;
    RecyclerView recyclerView, recyclerview1;
    FirebaseRecyclerAdapter<DisplayCategory, CategoryViewHolder> adapter;
    FirebaseRecyclerAdapter<NestedCategory, NestedCategoryViewHolder> adapter1;
    RecyclerView.LayoutManager manager;
    String s;
    int numbercon;
    int positionpro = 0;
    String[] products = new String[]{"Wall Poster", "Coffee Mug", "Visiting Card", "Mobile Cover", "Pop Socket", "Photo Frame"};
    CircleImageView img;
    AVLoadingIndicatorView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loader = (AVLoadingIndicatorView) findViewById(R.id.loader);
        wpimg = (ImageView) findViewById(R.id.wpimg);
        mc = (ImageView) findViewById(R.id.mc);
        mcs = (ImageView) findViewById(R.id.mcs);
        psk = (ImageView) findViewById(R.id.psk);
        fr = (ImageView) findViewById(R.id.fr);
        vc = (ImageView) findViewById(R.id.vc);
        banner = FirebaseDatabase.getInstance().getReference().child("Banner");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("PrintArt");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        toolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header_view = navigationView.getHeaderView(0);
        img = (CircleImageView) header_view.findViewById(R.id.news);
        DrawerLayout drawer = findViewById(R.id.drawer_layoutyes);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        LinearLayoutManager layoutManagerH = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        manager = new LinearLayoutManager(this);
        reference = FirebaseDatabase.getInstance().getReference("ProductCategory");
        recyclerView = findViewById(R.id.recyclerViewer);
        recyclerView.setLayoutManager(manager);
        recyclerview1 = findViewById(R.id.horizontalScroller);
        recyclerview1.setHasFixedSize(true);
        recyclerview1.setLayoutManager(layoutManagerH);
        FirebaseRecyclerOptions<HorizontalScroller> optionsb =
                new FirebaseRecyclerOptions.Builder<HorizontalScroller>()
                        .setQuery(banner, HorizontalScroller.class).build();
        adapterH = new HorizontalAdapter(optionsb);
        recyclerview1.setAdapter(adapterH);
        adapterH.startListening();
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autocompletetextview);
        autoCompleteTextView.setDropDownBackgroundDrawable(new ColorDrawable(getBaseContext().getResources().getColor(R.color.colorPrimaryDark)));
        autoCompleteTextView.setAdapter(new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_list_item_1, products));
        DatabaseReference UserPortal;
        UserPortal = FirebaseDatabase.getInstance().getReference();
        EndUserPortal = FirebaseDatabase.getInstance().getReference().child("EndUsers");
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);


        navigation.setOnNavigationItemSelectedListener(this);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, AllProductsActivity.class);
                typepasser = autoCompleteTextView.getText().toString();
                intent.putExtra("Type", typepasser);
                startActivity(intent);
            }
        });
//////////////////////////////////////////////
        Query sorting;
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        Log.e("RANDROM number", String.valueOf(randomNumber));
        switch (randomNumber) {
            case 0: {
                sorting = reference;
                break;
            }
            case 1: {
                sorting = reference.orderByChild("CountPost");
                break;
            }
            case 2: {
                sorting = reference.orderByChild("CountPost1");
                break;
            }
            case 3: {
                sorting = reference.orderByChild("CountPost2");
                break;
            }
            default: {
                sorting = reference;
            }
        }
        FirebaseRecyclerOptions<DisplayCategory> options = new FirebaseRecyclerOptions.Builder<DisplayCategory>()
                .setQuery(sorting, DisplayCategory.class).build();
        adapter = new FirebaseRecyclerAdapter<DisplayCategory, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position, @NonNull final DisplayCategory displayCategory) {
                //Here
                Log.e("Printing",""+numbercon++);

                if(numbercon==2)
                {
                    holder.catimgv.setVisibility(View.VISIBLE);
                    holder.catimgv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(HomeActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                CategoryViewHolder.CategoryName.setText(displayCategory.getCategoryName());
                FirebaseRecyclerOptions<NestedCategory> options1 = new FirebaseRecyclerOptions.Builder<NestedCategory>()
                        .setQuery(reference.child(displayCategory.getCategoryID()).child("About"), NestedCategory.class)
                        .build();



                adapter1 = new FirebaseRecyclerAdapter<NestedCategory, NestedCategoryViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull NestedCategoryViewHolder holder, int position, @NonNull final NestedCategory nestedCategory) {
                        Picasso.get().load(nestedCategory.getProductDescription()).into(holder.IgmV);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(HomeActivity.this, DisplayProductActivity.class);
                                intent.putExtra("Category", displayCategory.getCategoryID());
                                intent.putExtra("TypeID", nestedCategory.getType());
                                startActivity(intent);
                            }
                        });
                        loader.setVisibility(View.GONE);

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
                    public NestedCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View v2 = LayoutInflater.from(getBaseContext()).inflate
                                (R.layout.nesteddisplay, parent, false);
                        return new NestedCategoryViewHolder(v2);
                    }
                };
                adapter1.startListening();
                adapter1.notifyDataSetChanged();
                CategoryViewHolder.category_recyclerView.setAdapter(adapter1);
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
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v1 = LayoutInflater.from(getBaseContext()).inflate(R.layout.displayproducts, parent, false);
                return new CategoryViewHolder(v1);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


        ///////////////////////////////////
    }

    @Override
    protected void onStart() {
        super.onStart();

        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (autoCompleteTextView.getRight() - autoCompleteTextView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Intent intent = new Intent(HomeActivity.this, AllProductsActivity.class);
                        typepasser = autoCompleteTextView.getText().toString();
                        intent.putExtra("Type", "Default");
                        startActivity(intent);
                        return true;
                    }
                }
                return false;

            }
        });

        wpimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typepasser = "Wall Poster";
                gotonext();
            }
        });
        mc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typepasser = "Coffee Mug";
                gotonext();
            }
        });
        mcs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typepasser = "Mobile_Cover";
                gotonext();
            }
        });
        psk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typepasser = "Pop_Socket";
                gotonext();
            }
        });
        fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typepasser = "Photo_Frame";
                gotonext();
            }
        });
        vc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typepasser = "Visiting_Card";
                gotonext();
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        //  final CircleImageView userProfilePicture = headerView.findViewById(R.id.user_profile_image);
        final TextView userPhonenumber = headerView.findViewById(R.id.user_phonenumber);
        final TextView userName = headerView.findViewById(R.id.user_profile_name);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s.equals("0000000000")) {
                    taketoreg();
                } else {
                    Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
                    intent.putExtra("AccountN", s);
                    startActivity(intent);
                }
            }
        });


        Paper.init(this);
        s = Paper.book().read(PaperStore.UserLoginID);
        if (s == null || s.isEmpty()) {
            Paper.book().write(PaperStore.UserLoginID, "0000000000");
            s = Paper.book().read(PaperStore.UserLoginID);
        }
        Toast.makeText(this, "" + s, Toast.LENGTH_SHORT).show();
        EndUserPortal.child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userPhonenumber.setText(dataSnapshot.child("PhoneNumber").getValue().toString());
                    userName.setText(dataSnapshot.child("Name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void gotonext() {
        Intent intent = new Intent(HomeActivity.this, AllProductsActivity.class);
        intent.putExtra("Type", typepasser);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Logout:
                if (s.equals("0000000000")) {
                    taketoreg();
                } else {

                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    intent.putExtra("us", s);
                    startActivity(intent);
                }
                return true;
            case R.id.action:
                Paper.book().destroy();
                Paper.book().write(PaperStore.UserLoginID, "0000000000");
                Paper.book().write(PaperStore.UserLoginID, "0000000000");
                Intent intes = new Intent(HomeActivity.this, MainActivity.class);
                intes.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intes);
                return true;
            case R.id.profile:

                if (s.equals("0000000000")) {
                    taketoreg();
                } else {
                    Intent intentis = new Intent(HomeActivity.this, MyAccountActivity.class);
                    intentis.putExtra("AccountN", s);
                    startActivity(intentis);
                }
                return true;
            case R.id.Help:
                Intent intenti = new Intent(HomeActivity.this, CustomerSupportActivity.class);
                startActivity(intenti);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void taketoreg() {
        Toast.makeText(this, "Register here to explore more with us!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            case R.id.printart_sivakasi: {
                Intent intent = new Intent(HomeActivity.this, PrintArtActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.your_designs: {
                //pending
                break;
            }
            case R.id.businessorbulk: {
                //pending
                break;
            }
            case R.id.viewall: {
                Intent intent = new Intent(HomeActivity.this, AllProductsActivity.class);
                intent.putExtra("Type", "Default");
                startActivity(intent);
                break;
            }
            case R.id.profile: {
                if (s.equals("0000000000")) {
                    taketoreg();
                } else {
                    Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
                    intent.putExtra("AccountN", s);
                    startActivity(intent);
                }
                break;
            }
            case R.id.cart: {
                if (s.equals("0000000000")) {
                    taketoreg();
                } else {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    intent.putExtra("us", s);
                    startActivity(intent);
                }
                break;
            }
            case R.id.wishlist: {
                if (s.equals("0000000000")) {
                    taketoreg();
                } else {
                    Intent intent = new Intent(HomeActivity.this, WListACtivity.class);
                    intent.putExtra("Contact", s);
                    startActivity(intent);
                }
                break;
            }
            case R.id.myorders: {
                if (s.equals("0000000000")) {
                    taketoreg();
                } else {
                    Intent intent = new Intent(HomeActivity.this, TestActivity.class);
                    intent.putExtra("Contact", s);
                    startActivity(intent);
                }
                break;
            }
            case R.id.referandearn: {
                if (s.equals("0000000000")) {
                    taketoreg();
                } else {
                    Intent intent = new Intent(HomeActivity.this, ReferandEarnActivity.class);
                    intent.putExtra("Contact", s);
                    startActivity(intent);
                }
                break;
            }
            case R.id.helpandsupport: {
                if (s.equals("0000000000")) {
                    taketoreg();
                } else {
                    Intent intent = new Intent(HomeActivity.this, CustomerSupportActivity.class);
                    startActivity(intent);
                }
                break;
            }
            case R.id.signout: {
                Paper.book().destroy();
                Paper.book().write(PaperStore.UserLoginID, "0000000000");
                Paper.book().write(PaperStore.UserLoginID, "0000000000");
                Intent intes = new Intent(HomeActivity.this, MainActivity.class);
                intes.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intes);
                break;
            }
            case R.id.action_cart: {
                if (s.equals("0000000000")) {
                    taketoreg();
                } else {

                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    intent.putExtra("us", s);
                    startActivity(intent);
                }
                break;
            }
            case R.id.action_wishlist: {
                if (s.equals("0000000000")) {
                    taketoreg();
                } else {
                    Intent intent = new Intent(HomeActivity.this, WListACtivity.class);
                    intent.putExtra("Contact", s);
                    startActivity(intent);
                }
                break;
            }
            case R.id.action_Myorders: {
                if (s.equals("0000000000")) {
                    taketoreg();
                } else {
                    Intent intent = new Intent(HomeActivity.this, TestActivity.class);
                    intent.putExtra("Contact", s);
                    startActivity(intent);
                }
                break;
            }

            case R.id.action_Support: {
                Intent intenti = new Intent(HomeActivity.this, CustomerSupportActivity.class);
                startActivity(intenti);

                break;
            }
            case R.id.action_settings: {
                if (s.equals("0000000000")) {
                    taketoreg();
                } else {
                    Intent intent = new Intent(HomeActivity.this, MyAccountActivity.class);
                    intent.putExtra("AccountN", s);
                    startActivity(intent);
                }
                break;
            }


        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutyes);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
