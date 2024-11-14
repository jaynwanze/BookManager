package com.example.bookmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.bookmanager.db.dao.BookDAO;
import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.pojo.Book;
import com.example.bookmanager.pojo.User;
import com.example.bookmanager.utils.BookAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {
    private String currentUserEmail;
    private ActionBarDrawerToggle toggle;
    private BookAdapter mAdapter;
    private ArrayList<Book> books;
    private String currentSelectedCategory;
    private String currentSelectedStatus;
    private boolean isReturningFromAnotherActivity = false;
    private User currentUser;


        public static ArrayList<Book> getMockBooks() {
            ArrayList<Book> books = new ArrayList<>();
                String[] categories = {"Fantasy", "Sci-fi", "Technology", "Life Style", "Romance"};
                String[] statuses = {"In progress", "Finished"};

                books.add(new Book(1, "The Name of the Wind", "Patrick Rothfuss", categories[0], "12/10/2023", "A captivating fantasy tale.", statuses[0], 1));
                books.add(new Book(2, "Dune", "Frank Herbert", categories[1], "12/10/2023", "A sprawling epic of politics and ecology.", statuses[1], 1));
                books.add(new Book(3, "Clean Code", "Robert C. Martin", categories[2], "12/10/2023", "A guide to writing readable and maintainable code.", statuses[0], 1));
                books.add(new Book(4, "Atomic Habits", "James Clear", categories[3], "12/10/2023", "A practical framework for improving habits.", statuses[1], 1));
                books.add(new Book(5, "The Notebook", "Nicholas Sparks", categories[4], "12/10/2023", "A heartwarming love story.", statuses[0], 1));
                books.add(new Book(6, "The Way of Kings", "Brandon Sanderson", categories[0], "12/10/2023", "An epic fantasy with a complex magic system.", statuses[1], 1));
                books.add(new Book(7, "The Martian", "Andy Weir", categories[1], "12/10/2023", "A survival story set on Mars.", statuses[0], 1));
                books.add(new Book(8, "Eloquent JavaScript", "Marijn Haverbeke", categories[2], "12/10/2023", "A modern introduction to programming.", statuses[1], 1));
                books.add(new Book(9, "Digital Minimalism", "Cal Newport", categories[3], "12/10/2023", "A philosophy for a focused life in a digital age.", statuses[0], 1));
                books.add(new Book(10, "Me Before You", "Jojo Moyes", categories[4], "12/10/2023", "A tearjerker romance.", statuses[1], 1));
                books.add(new Book(11, "A Game of Thrones", "George R.R. Martin", categories[0], "12/10/2023", "The first book in the epic fantasy series.", statuses[0], 1));
                books.add(new Book(12, "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", categories[1], "12/10/2023", "A hilarious and thought-provoking sci-fi classic.", statuses[1], 1));
                books.add(new Book(13, "Code Complete", "Steve McConnell", categories[2], "12/10/2023", "A comprehensive guide to software construction.", statuses[0], 1));
                books.add(new Book(14, "The Power of Habit", "Charles Duhigg", categories[3], "12/10/2023", "An exploration of the science of habit formation.", statuses[1], 1));
                books.add(new Book(15, "The Love Hypothesis", "Ali Hazelwood", categories[4], "12/10/2023", "A STEMinist romance.", statuses[0], 1));
                books.add(new Book(16, "Mistborn: The Final Empire", "Brandon Sanderson", categories[0], "12/10/2023", "A fantasy novel with a unique magic system.", statuses[1], 1));
                books.add(new Book(17, "Project Hail Mary", "Andy Weir", categories[1], "12/10/2023", "A sci-fi thriller about saving humanity.", statuses[0], 1));
                books.add(new Book(18, "You Don't Know JS", "Kyle Simpson", categories[2], "12/10/2023", "A deep dive into the JavaScript language.", statuses[1], 1));
                books.add(new Book(19, "Indistractable", "Nir Eyal", categories[3], "12/10/2023", "Strategies for mastering your attention.", statuses[0], 1));
                books.add(new Book(20, "Red, White & Royal Blue", "Casey McQuiston", categories[4], "12/10/2023", "A LGBTQ+ romance.", statuses[1], 1));

                return books;
            }

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
        this.currentUser = null;
        DBHandler dbHandler;
        Intent intent = getIntent();
        this.currentUserEmail = intent.getStringExtra("userEmail");
        try{ dbHandler = new DBHandler(this);
            UserDAO userDAO = new UserDAO(dbHandler);
            this.currentUser = userDAO.getUserLoggedIn(this.currentUserEmail);
            dbHandler.close();
            Toast.makeText(HomePageActivity.this, "Welcome " + this.currentUser .getName(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(HomePageActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
                        if (books != null) {
                            ArrayList<Book> filteredBooksByCategory = new ArrayList<>();
                            for (Book book : books) {
                                if (book.getCategory().equals(currentSelectedCategory)) {
                                    // Filter books by category
                                    filteredBooksByCategory.add(book);
                                }
                            }
                            mAdapter.updateDataSet(filteredBooksByCategory);
                        }
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
                        if (books != null){
                            ArrayList<Book> filteredBooksByStatus = new ArrayList<>();
                            for (Book book : books) {
                                if (book.getCategory().equals(currentSelectedStatus)) { // Replace with your category logic
                                    filteredBooksByStatus.add(book);
                                }
                            }
                        mAdapter.updateDataSet(filteredBooksByStatus);
                    }
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
        DBHandler dbHandler;
        Intent intent = getIntent();
        this.currentUserEmail = intent.getStringExtra("userEmail");
        try{ dbHandler = new DBHandler(this);
            UserDAO userDAO = new UserDAO(dbHandler);
            user = userDAO.getUserLoggedIn(this.currentUserEmail);
            dbHandler.close();
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
        if (this.books == null) {
            this.books = new ArrayList<>();
        }
        DBHandler dbHandler = new DBHandler(this);
        BookDAO bookDAO = new BookDAO(dbHandler);
        /*ArrayList<Book> mockBooks = getMockBooks();
        for (int i = 0; i < 20; i++){
            bookDAO.createBook(mockBooks.get(i).getTitle(), mockBooks.get(i).getAuthor(), mockBooks.get(i).getCategory(), mockBooks.get(i).getStartDate(), mockBooks.get(i).getReview(), mockBooks.get(i).getStatus(), mockBooks.get(i).getUserId());
        }*/
        this.books = bookDAO.getAllBooks(this.currentUser.getId());
        if (this.books == null){
            Toast.makeText(this, "Book list is empty", Toast.LENGTH_SHORT).show();
        }
        this.mAdapter = new BookAdapter(this.books, this.currentUserEmail); // Initialize mAdapter here
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (isReturningFromAnotherActivity) {
            //refresh view
            setHeaderView();
            setRecyclerView();
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
