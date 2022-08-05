package com.example.shoppingapplication.User;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shoppingapplication.Adapter.HomeAdapter.CategoriesAdapter;
import com.example.shoppingapplication.Adapter.HomeAdapter.CategoriesHelperClass;
import com.example.shoppingapplication.Adapter.HomeAdapter.FeaturedAdapter;
import com.example.shoppingapplication.Adapter.HomeAdapter.FeaturedHelper;
import com.example.shoppingapplication.Adapter.HomeAdapter.MostViewedAdpater;
import com.example.shoppingapplication.Adapter.HomeAdapter.MostViewedHelperClass;
import com.example.shoppingapplication.Admin.AdminAddNewProductsActivity;
import com.example.shoppingapplication.Admin.AdminCategoryActivity;
import com.example.shoppingapplication.Admin.AdminMaintainProducts;
import com.example.shoppingapplication.Admin.AdminNewOrdersActivity;
import com.example.shoppingapplication.Counter;
import com.example.shoppingapplication.HomeFragment;
import com.example.shoppingapplication.MainActivity;
import com.example.shoppingapplication.Model.UserOrders;
import com.example.shoppingapplication.Prevalent.Prevalent;
import com.example.shoppingapplication.Model.Products;
import com.example.shoppingapplication.R;
import com.example.shoppingapplication.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView, featuredRecycler, mostViewedRecycler, categoriesRecycler;
    RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab;
    private String type = "";
    private FrameLayout frameLayout;
    private ImageSlider imageSlider;
    private long backPressedTime;
    private Toast backToast;
    RecyclerView.Adapter adapter;
    private GradientDrawable gradient1, gradient2, gradient3, gradient4;
    Counter counter;
    private TextView cartCounter;
    UserOrders userOrders;
    private RelativeLayout searchBar;
    private CardView cardView;
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().get("Admin").toString();
        }


        if (type.equals("Admin")) {

            bottomNavigationView = findViewById(R.id.admin_home_menu);
            bottomNavigationView.setSelectedItemId(R.id.admin_edit);
            bottomNavigationView.setVisibility(View.VISIBLE);


            BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.admin_home:
                            startActivity(new Intent(getApplicationContext(), AdminCategoryActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.admin_add:
                            startActivity(new Intent(getApplicationContext(), AdminAddNewProductsActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.admin_edit:
                            return true;
                        case R.id.admin_orders:
                            startActivity(new Intent(getApplicationContext(), AdminNewOrdersActivity.class));
                            overridePendingTransition(0, 0);
                            return true;

                    }
                    return false;
                }
            };

            bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        }

        cardView = findViewById(R.id.card_categories);

        searchBar = findViewById(R.id.search_bar_home);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchProductActivity.class);
                startActivity(intent);
            }
        });

        //Hooks
//        featuredRecycler = findViewById(R.id.featured_recycler);
        categoriesRecycler = findViewById(R.id.categories_recycler);
 //       mostViewedRecycler = findViewById(R.id.most_viewed_recycler);


        cartCounter = findViewById(R.id.cart_counter_number);

     /*   final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("User view")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child("Products");



        cartListRef.addValueEventListener(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                  if (snapshot.exists()) {
                                                      cartCounter.setText("1");

                                                  } else {
                                                      String quantity = snapshot.getValue(UserOrders.class).toString();
                                                      cartCounter.setText(quantity);
                                                  }
                                              }

                                              @Override
                                              public void onCancelled(@NonNull DatabaseError error) {

                                              }
                                          });

      */



     //   featuredRecycler();
        categoriesRecycler();
     //   mostViewedRecycler();




        imageSlider = findViewById(R.id.image_slider);




        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        Paper.init(this);




        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        if (!type.equals("Admin")) {
            userNameTextView.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }


        recyclerView = findViewById(R.id.recycler_menu);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        frameLayout = findViewById(R.id.main_frame_layout);
        setFragment(new HomeFragment());

        ArrayList<SlideModel> images = new ArrayList<>();
        images.add(new SlideModel(R.drawable.foodbanner4, null));
        images.add(new SlideModel(R.drawable.foodbanner2, null));
        images.add(new SlideModel(R.drawable.foodbanner3, null));
        images.add(new SlideModel(R.drawable.foodbanner1, null));
        images.add(new SlideModel(R.drawable.foodbanner5, null));
        images.add(new SlideModel(R.drawable.foodbanner6, null));
        images.add(new SlideModel(R.drawable.foodbanner7, null));




        imageSlider.setImageList(images, ScaleTypes.CENTER_CROP);
        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                Toast.makeText(HomeActivity.this, "Item" + i + " Selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void featuredRecycler() {
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelper> featuredLocations = new ArrayList<>();
        featuredLocations.add(new FeaturedHelper(R.drawable.oneplus9pro,"One Plus 9", "₹49,999"));
        featuredLocations.add(new FeaturedHelper(R.drawable.iphone12f,"Iphone 12", "₹76,900"));
        featuredLocations.add(new FeaturedHelper(R.drawable.samsungs21,"Samsung S21", "₹68,999"));
        featuredLocations.add(new FeaturedHelper(R.drawable.vivo,"Vivo X60", "₹37,990"));


        adapter = new FeaturedAdapter(featuredLocations);
        featuredRecycler.setAdapter(adapter);

        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[] {0xffeff400, 0xffaff600});

    }

    private void categoriesRecycler() {

        //All Gradients
        gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff4776E6, 0xff8E54E9});
        gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff857a6, 0xffff5858});
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff1CD8D2, 0xff93EDC7});
        gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff1D976C, 0xff93F9B9});


        ArrayList<CategoriesHelperClass> categoriesHelperClasses = new ArrayList<>();
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.fastfood, "Fast Food", gradient1));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.casualdining, "Casual Dining", gradient3));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.breakfast, "Breakfast", gradient2));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.lunch, "Lunch", gradient4));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.cocktail, "Beverages", gradient1));


        categoriesRecycler.setHasFixedSize(true);
        adapter = new CategoriesAdapter(categoriesHelperClasses);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(adapter);

        categoriesRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void mostViewedRecycler() {

        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<MostViewedHelperClass> mostViewedLocations = new ArrayList<>();
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.book, "The Psychology of Money"));
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.cookware, "Rachael Ray Cucina Nonstick Cookware"));
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.goli, "Goli Apple Cider Vinegar Gummies"));
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.echo, "Amazon Echo Dot"));

        adapter = new MostViewedAdpater(mostViewedLocations);
        mostViewedRecycler.setAdapter(adapter);

    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("₹" + model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent;
                                Bundle bundle = new Bundle();

                                if (type.equals("Admin")) {
                                    intent = new Intent(HomeActivity.this, AdminMaintainProducts.class);
                                } else {
                                    intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                }
                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("category", model.getCategory());
                                intent.putExtra("image", model.getImage());

                                startActivity(intent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            finishAffinity();
            super.onBackPressed();
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        menu.findItem(R.id.cart_fab).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.START);
             return true;
       }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            if (!type.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_orders) {
            if (!type.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, OrdersActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_categories) {

        } else if (id == R.id.nav_settings) {
            if (!type.equals("Admin")) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_logout) {
            if (!type.equals("Admin")) {
                Paper.book().destroy();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment (Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(frameLayout.getId(), fragment);
        ft.commit();
    }

}