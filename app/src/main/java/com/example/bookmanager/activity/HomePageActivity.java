package com.example.bookmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmanager.R;
import com.example.bookmanager.db.dao.UserDAO;
import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.pojo.Book;
import com.example.bookmanager.pojo.User;
import com.example.bookmanager.utils.BookAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_menu); // Set the Navigation Drawer layout

        // Get references to DrawerLayout and NavigationView
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Set up ActionBarDrawerToggle (for the hamburger icon)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    // Handle home navigation
                } else if (id == R.id.nav_profile) {
                    // Handle profile navigation
                } // ... handle other menu items ...

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Apply window insets to the content view (activity_home_page)
        View contentView = findViewById(R.id.activity_home_page); //
        ViewCompat.setOnApplyWindowInsetsListener(contentView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //get user email from intent
        Intent intent = getIntent();
        this.currentUserEmail = intent.getStringExtra("userEmail");
        try{ DBHandler dbHandler = new DBHandler(this);
            UserDAO userDAO = new UserDAO(dbHandler);
            User user = userDAO.getUserLoggedIn(this.currentUserEmail);
            dbHandler.close();
            Toast.makeText(HomePageActivity.this, "Welcome " + user.getName(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(HomePageActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<Book> myDataset = new ArrayList<Book>();
        BookAdapter mAdapter = new BookAdapter(myDataset);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

       for (int i = 0; i < 10; i++) {
           Book book = new Book(i, "Title " + i, "Author " + i, "Category " + i, "Start Date " + i, "Review " + i, "Status " + i);
           myDataset.add(book);
       }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info) {
            // Create and show the AlertDialog
            new AlertDialog.Builder(HomePageActivity.this)
                    .setTitle("Information")
                    .setMessage("BookManager App 1.0")
                    .setPositiveButton("OK", null) // Null listener for OK button
                    .show();
        }

        if (id == R.id.action_profile) {
            Toast.makeText(HomePageActivity.this,"Profile Details for user: " + this.currentUserEmail, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
            intent.putExtra("userEmail", this.currentUserEmail);
            startActivity(intent);
            this.finish();
        }
        if (id == R.id.action_logout) {
            logout();

        }
        if (id == R.id.action_preferences) {
            Toast.makeText(HomePageActivity.this, "Preferences", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(HomePageActivity.this, PreferencesActivity.class);
            startActivity(intent);
            this.finish();
        }

        return super.onOptionsItemSelected(item);

    }

    //go back to sign up activity
    private void logout() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        this.finish();
    }

}
