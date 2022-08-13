package com.example.modernizedshapp.User;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modernizedshapp.Helperclasses.HomeAdapter.CategoriesAdapter;
import com.example.modernizedshapp.Helperclasses.HomeAdapter.CategoriesHelperClass;
import com.example.modernizedshapp.Helperclasses.HomeAdapter.FeaturedAdapter;
import com.example.modernizedshapp.Helperclasses.HomeAdapter.FeaturedHealperClass;
import com.example.modernizedshapp.Helperclasses.HomeAdapter.MostViewedAdapter;
import com.example.modernizedshapp.Helperclasses.HomeAdapter.MostViewedHelperClass;
import com.example.modernizedshapp.R;
import com.example.modernizedshapp.common.LoginSignup.StartUpScreen;
import com.example.modernizedshapp.doctor.HealthNews.HealthMainActivity;
import com.example.modernizedshapp.doctor.UserDashboard;
import com.example.modernizedshapp.doctor.foodtip.SplashActivity;
import com.example.modernizedshapp.doctor.mapbox.MapBoxMainActivity;
import com.example.modernizedshapp.doctor.therapy.TheTherapist;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;

    RecyclerView featuredRecycler, mostViewedRecycler, categoriesRecycler;
    RecyclerView.Adapter adapter;
    private GradientDrawable gradient1, gradient2, gradient3, gradient4;
    ImageView menuIcon;
    LinearLayout contentView;
    RelativeLayout search;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.buttom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.buttom_nav_home);
        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.buttom_nav_dashboard:
                    startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                    overridePendingTransition(0, 0);
                    return;
                case R.id.buttom_nav_home:
                    return;
                case R.id.buttom_nav_profle:
                    startActivity(new Intent(getApplicationContext(), Emergencies.class));
                    overridePendingTransition(0, 0);
                    return;
            }
        });

        //Hooks
        featuredRecycler = findViewById(R.id.featured_recycler);
        search = findViewById(R.id.searchRelativeLayout);
        mostViewedRecycler = findViewById(R.id.motive_recycler);
        categoriesRecycler = findViewById(R.id.cat_recycler);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationDrawer();

        //Functions will be executed automatically when this activity will be created
        featuredRecycler();
        mostViewedRecycler();
        categoriesRecycler();
    }

    private void navigationDrawer() {
        //navigation_drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                //scale based on current slide
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleX(offsetScale);

                //Translate the view
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);

            }

        });
    }

    public void callSearchLocation(View view) {
        startActivity(new Intent(getApplicationContext(), MapBoxMainActivity.class));
    }

    public void callStartUpScreen(View view) {
        startActivity(new Intent(getApplicationContext(), StartUpScreen.class));
    }

    public void callTherapy(View view) {
        startActivity(new Intent(getApplicationContext(), TheTherapist.class));
    }

    public void callDoctor(View view) {
        startActivity(new Intent(getApplicationContext(), com.example.modernizedshapp.doctor.Diagnosis.SplashActivity.class));
    }

    public void callLocation(View view) {
        startActivity(new Intent(getApplicationContext(), MapBoxMainActivity.class));
    }

    public void callFood(View view) {
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
    }

    public void callCatV(View view) {
        startActivity(new Intent(getApplicationContext(), AllCategories.class));
    }

    public void callNews(View view) {
        startActivity(new Intent(getApplicationContext(), HealthMainActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    private void categoriesRecycler() {
        //All Gradients
        gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffd4cbe5, 0xffd4cbe5});
        gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xff7adccf, 0xff7adccf});
        gradient3 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xfff7c59f, 0xFFf7c59f});
        gradient4 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffb8d7f5, 0xffb8d7f5});


        ArrayList<CategoriesHelperClass> categoriesHelperClasses = new ArrayList<>();
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.therapy, "AI Therapy", gradient1));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.disease_prediction, "D-Predictions", gradient2));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.prediction, "Disease Info", gradient3));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.map, "GeoMap", gradient4));
        categoriesHelperClasses.add(new CategoriesHelperClass(R.drawable.food, "Food Tips", gradient1));


        categoriesRecycler.setHasFixedSize(true);
        adapter = new CategoriesAdapter(categoriesHelperClasses);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(adapter);
    }

    private void mostViewedRecycler() {
        mostViewedRecycler.setHasFixedSize(true);
        mostViewedRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<MostViewedHelperClass> mostViewedLocations = new ArrayList<>();
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.not_alone, "You are loved and cherished. You can push through this."));
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.dark_knight, "light awaits"));
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.sunrise, "The Sunset"));
        mostViewedLocations.add(new MostViewedHelperClass(R.drawable.depressed, "Time heals everything"));

        adapter = new MostViewedAdapter(mostViewedLocations);
        mostViewedRecycler.setAdapter(adapter);
    }

    private void featuredRecycler() {
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHealperClass> featuredLocation = new ArrayList<>();

        featuredLocation.add(new FeaturedHealperClass(R.drawable.heart_disease, "Heart Disease", "8.9 million die each year"));
        featuredLocation.add(new FeaturedHealperClass(R.drawable.pneumonia, "Pneumonia", "2.9 million die from Lower Respiratory infections"));
        featuredLocation.add(new FeaturedHealperClass(R.drawable.depression, "Depression", "800,000 People attempt suicide every year"));

        adapter = new FeaturedAdapter(featuredLocation);
        featuredRecycler.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_cart:
                Intent intent = new Intent(getApplicationContext(), AllCategories.class);
                startActivity(intent);
                break;
            case R.id.nav_bmi:
                Intent intent1 = new Intent(getApplicationContext(), Bmi_calculator.class);
                startActivity(intent1);
                break;
            case R.id.nav_login:
            case R.id.nav_logout:
                Intent intent2 = new Intent(getApplicationContext(), StartUpScreen.class);
                startActivity(intent2);
                break;
            case R.id.nav_profile:
                Intent intent4 = new Intent(getApplicationContext(), UserDashboard.class);
                startActivity(intent4);
                break;
            case R.id.nav_home:
                Intent intent3 = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent3);
                break;
            case R.id.nav_therapy:
                Intent intent5 = new Intent(getApplicationContext(), TheTherapist.class);
                startActivity(intent5);
                break;
            case R.id.nav_food_tip:
                Intent intent6 = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent6);
                break;
            case R.id.nav_disease_prediction:
                Intent intent7 = new Intent(getApplicationContext(), com.example.modernizedshapp.doctor.Diagnosis.SplashActivity.class);
                startActivity(intent7);
                break;
            case R.id.nav_geomap:
                Intent intent8 = new Intent(getApplicationContext(), MapBoxMainActivity.class);
                startActivity(intent8);
                break;
            case R.id.nav_disease_info:
                Intent intent9 = new Intent(getApplicationContext(), HealthMainActivity.class);
                startActivity(intent9);
        }

        return true;
    }
}