package com.example.bookmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private String currentUserEmail;
    private ActionBarDrawerToggle toggle;
    private BookAdapter mAdapter;
    private ArrayList<Book> books;
    private String currentSelectedCategory;
    private String currentSelectedStatus;
    private boolean isReturningFromAnotherActivity = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_menu);
        setNavigationView();// Set the Navigation Drawer layout
        // Apply window insets to the content view (activity_home_page)
        View contentView = findViewById(R.id.activity_home_page); //
        ViewCompat.setOnApplyWindowInsetsListener(contentView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setHeaderView();// set header view
        setFilterTabs();// set filter tabs
        setRecyclerView();// set recycler view


        //set up search view and on query text listener
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setQueryHint("Search book...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true; // Handle search submission
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes (optional)
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        if (item.getItemId() == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("userEmail", this.currentUserEmail);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_preferences) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            intent.putExtra("userEmail", this.currentUserEmail);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_info) {
            new AlertDialog.Builder(this)
                    .setTitle("Information")
                    .setMessage("BookManager 1.0")
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void applyFilter(int tabPosition) {
        //Initial display text
        TextView textView = findViewById(R.id.filter_display_text);

        switch (tabPosition) {
            case 0: // All
                // Show all books
                textView.setText(R.string.displaying_all_books);
                mAdapter.updateDataSet(this.books); // Assuming myDataset holds all books
                break;
            case 1: // Category
                View categoryDropdown = getLayoutInflater().inflate(R.layout.dropdown_category, null);
                Spinner categorySpinner = categoryDropdown.findViewById(R.id.categorySpinner);
                // Display the dropdown in a dialog
                new AlertDialog.Builder(this)
                        .setTitle("Select Category")
                        .setView(categoryDropdown)
                        .setPositiveButton("OK", null)
                        .show();

                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        currentSelectedCategory = parent.getItemAtPosition(position).toString();
                        textView.setText(getString(R.string.displaying_books_in_category) + currentSelectedCategory);
                        ArrayList<Book> filteredBooksByCategory = new ArrayList<>();
                        for (Book book : books) {
                            if (book.getCategory().equals(currentSelectedCategory)) {
                                // Filter books by category
                                filteredBooksByCategory.add(book);
                            }
                        }
                        mAdapter.updateDataSet(filteredBooksByCategory);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        textView.setText(R.string.please_select_a_category);
                    }
                });

                break;
            case 2: // Status
                // Filter by status
                View statusDropdown = getLayoutInflater().inflate(R.layout.dropdown_status, null);
                Spinner statusSpinner = statusDropdown.findViewById(R.id.statusSpinner);
                // Display the dropdown in a dialog
                new AlertDialog.Builder(this)
                        .setTitle("Select Status")
                        .setView(statusDropdown)
                        .setPositiveButton("OK", null)
                        .show();

                statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        currentSelectedStatus = parent.getItemAtPosition(position).toString();
                        textView.setText(getString(R.string.displaying_books_with_status) + currentSelectedStatus);
                        ArrayList<Book> filteredBooksByStatus = new ArrayList<>();

                        for (Book book : books) {
                            if (book.getCategory().equals(currentSelectedStatus)) { // Replace with your category logic
                                filteredBooksByStatus.add(book);
                            }
                        }
                        mAdapter.updateDataSet(filteredBooksByStatus);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        textView.setText(R.string.please_select_a_status);
                    }
                });

                break;
        }
    }

    private void setHeaderView() {
        //get user email from intent
        User user = null;
        Intent intent = getIntent();
        this.currentUserEmail = intent.getStringExtra("userEmail");
        try{ this.dbHandler = new DBHandler(this);
            UserDAO userDAO = new UserDAO(dbHandler);
            user = userDAO.getUserLoggedIn(this.currentUserEmail);
            dbHandler.close();
            Toast.makeText(HomePageActivity.this, "Welcome " + user.getName(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(HomePageActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        TextView userNameTextView = findViewById(R.id.userName);
        if (user != null) {
            userNameTextView.setText(user.getName());
            TextView userEmailTextView = findViewById(R.id.userEmail);
            userEmailTextView.setText(user.getEmail());

        } else {
            userNameTextView.setText("Homepage");
        }
    }

    private void setNavigationView(){
        // Get references to DrawerLayout and NavigationView
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set up ActionBarDrawerToggle (for the hamburger icon)
        this.toggle = new ActionBarDrawerToggle(
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
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                else if (id == R.id.nav_add_book) {
                    Intent intent = new Intent(HomePageActivity.this, AddBookActivity.class);
                    intent.putExtra("userEmail", currentUserEmail);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }

                else if (id == R.id.nav_profile) {
                    // Handle profile navigation
                    Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
                    intent.putExtra("userEmail", currentUserEmail);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                else if (id == R.id.nav_preferences) {
                    // Handle preferences navigation
                    Intent intent = new Intent(HomePageActivity.this, PreferencesActivity.class);
                    intent.putExtra("userEmail", currentUserEmail);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void setFilterTabs() {

        //Set up tabs
        TabLayout filterTabs = findViewById(R.id.filterTabs);
        filterTabs.addTab(filterTabs.newTab().setText("All"));
        filterTabs.addTab(filterTabs.newTab().setText("Category"));
        filterTabs.addTab(filterTabs.newTab().setText("Status"));
        filterTabs.selectTab(filterTabs.getTabAt(0));
        filterTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        filterTabs.setTabMode(TabLayout.MODE_FIXED);
        TextView textView = findViewById(R.id.filter_display_text);
        textView.setText(R.string.displaying_all_books);
        currentSelectedCategory = null;
        currentSelectedStatus = null;
        // Handle tab selection
        filterTabs.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Apply filter based on selected tab
                applyFilter(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed for this case
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                applyFilter(tab.getPosition());
            }
        });
    }

    private void setRecyclerView(){
        // Set up RecyclerView
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        this.books = new ArrayList<>();
        this.mAdapter = new BookAdapter(this.books, this.currentUserEmail); // Initialize mAdapter here
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        //Mock data
        for (int i = 0; i < 20; i++) {
            Book book = new Book(i, "Title " + i, "Author " + i, "Category " + i, "Start Date " + i, "Review " + i, "Status " + i);
            this.books.add(book);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHandler.close();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (isReturningFromAnotherActivity) {
            //refresh view
            setHeaderView();
            //refresh dataset of books
            //TODO
            isReturningFromAnotherActivity = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isReturningFromAnotherActivity = true;
    }


    //go back to sign up activity
    private void logout() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        this.finish();
    }

}
