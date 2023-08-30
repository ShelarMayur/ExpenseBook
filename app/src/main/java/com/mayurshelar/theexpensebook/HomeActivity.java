package com.mayurshelar.theexpensebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView bnView;
    FrameLayout frameLayout;
    private DashboardFragment dashboardFragment;
    private IncomeFragment incomeFragment;
    private ExpenseFragment expenseFragment;

    private ToolsFragment toolsFragment;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dashboardFragment = new DashboardFragment();
        incomeFragment = new IncomeFragment();
        expenseFragment = new ExpenseFragment();
        toolsFragment = new ToolsFragment();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("ExpenseBook");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bnView = findViewById(R.id.bnView);
        frameLayout = findViewById(R.id.container);

        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if(itemId == R.id.dashborad){
                    bnView.setItemBackgroundResource(R.color.dashboard_color);
                    loadFragment(dashboardFragment);
                } else if(itemId == R.id.income){
                    bnView.setItemBackgroundResource(R.color.income_color);
                    loadFragment(incomeFragment);
                } else if(itemId == R.id.expense){
                    bnView.setItemBackgroundResource(R.color.expense_color);
                    loadFragment(expenseFragment);
                } else {
                    //open our dashboard as default
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.container, dashboardFragment);
                    ft.commit();
                }
                return true;
            }
        });

        bnView.setSelectedItemId(R.id.dashborad);
    }

    public void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        firebaseAuth = FirebaseAuth.getInstance();

        int itemId = item.getItemId();

        if(itemId == R.id.logout){
            if(firebaseAuth.getCurrentUser()!=null){
                firebaseAuth.signOut();
            }
            ImageView view = findViewById(R.id.myLogo);
            Toast.makeText(getApplicationContext(), "See you soon!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else if(itemId == R.id.tools){
            loadFragment(toolsFragment);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}