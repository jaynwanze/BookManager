package com.example.bookmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookmanager.R;
import com.example.bookmanager.db.dao.UserDAO;
import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.pojo.User;
import com.example.bookmanager.textchange.TextChangeHandler;
import com.google.android.material.tabs.TabLayout;


public class ProfileActivity extends AppCompatActivity
{
    private String currentUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DBHandler dbhandler;
        dbhandler = new DBHandler(this);
        dbhandler.getWritableDatabase();
        currentUserEmail = getIntent().getStringExtra("userEmail");
        UserDAO userDAO = new UserDAO(dbhandler);
        User user = userDAO.getUserLoggedIn(currentUserEmail);

        //Set up tabs
        // Create a TabLayout and add tabs
        TextView viewName = findViewById(R.id.view_name);
        viewName.setText(user.getName());
        TextView viewDOB = findViewById(R.id.view_dob);
        viewDOB.setText(user.getDob());
        TabLayout filterTabs = findViewById(R.id.profile_tabs);
        filterTabs.addTab(filterTabs.newTab().setText("View Profile"));
        filterTabs.addTab(filterTabs.newTab().setText("Update Profile"));
        filterTabs.selectTab(filterTabs.getTabAt(0));
        filterTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        filterTabs.setTabMode(TabLayout.MODE_FIXED);
        // Handle tab selection
        filterTabs.addOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Apply filter based on selected tab
                applyFilter(tab.getPosition(), user);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed for this case
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                applyFilter(tab.getPosition(), user);
            }
        });

        //add text change listener to password field
        //TextChangeHandler tch = new TextChangeHandler(this);

        Button updateProfileButton = findViewById(R.id.update_profile);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //update profile
                updateProfile();
            }
        });
    }

    public boolean validateFullName() {
        EditText editName = findViewById(R.id.name_edit);
        String fullName = editName.getText().toString();
        return fullName.isEmpty() || fullName.matches("^[a-zA-Z\\s]+$");
    }

    public boolean validateDateOfBirth() {
        EditText editDOB = findViewById(R.id.name_dob);
        String userDOB = editDOB.getText().toString();
        return userDOB.isEmpty() || userDOB.matches("\\d{2}/\\d{2}/\\d{4}");
    }

    private void updateProfile() {
        EditText editName = findViewById(R.id.edit_name);
        EditText editDOB = findViewById(R.id.dob_edit);
        String newName = editName.getText().toString();
        String newDOB = editDOB.getText().toString();

        if (!validateFullName()) {
            Toast.makeText(ProfileActivity.this, "Invalid Name: Must contain only letters and spaces", Toast.LENGTH_LONG).show();
            return;
        }
        else if (!validateDateOfBirth()) {
            Toast.makeText(ProfileActivity.this, "Invalid Date of Birth: Must be in the format DD/MM/YYYY", Toast.LENGTH_LONG).show();
            return;
        }

        else if (Integer.parseInt(newDOB.substring(0,2)) > 31)
        {
            Toast.makeText(ProfileActivity.this, "Invalid Date of Birth: Day must be between 1 and 31", Toast.LENGTH_LONG).show();
            return;
        }

        else if (Integer.parseInt(newDOB.substring(3,5)) > 12)
        {
            Toast.makeText(ProfileActivity.this, "Invalid Date of Birth: Month must be between 1 and 12", Toast.LENGTH_LONG).show();
            return;
        }

        else if( Integer.parseInt(newDOB.substring(6,10)) > 2012)
        {
            Toast.makeText(ProfileActivity.this, "Invalid Date of Birth: Must be born before year 2012", Toast.LENGTH_LONG).show();
            return;
        }
        DBHandler dbhandler = new DBHandler(this);
        UserDAO userDAO = new UserDAO(dbhandler);
        boolean userProfileUpdated = userDAO.updateUserProfile(newName,newDOB,currentUserEmail);
        User user = userDAO.getUserLoggedIn(currentUserEmail);
        if (userProfileUpdated){
            user.setName(newName);
            user.setDob(newDOB);
            applyFilter(1, user);
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Error updating profile", Toast.LENGTH_SHORT).show();
        }

    }

    private void applyFilter(int tabPosition, User currentUser) {

        Button updateProfileButton = findViewById(R.id.update_profile);
        TextView nameView = findViewById(R.id.view_name);
        TextView dobView = findViewById(R.id.view_dob);

        EditText editName = findViewById(R.id.edit_name);
        EditText editDOB = findViewById(R.id.dob_edit);
        //Initial display text

        switch (tabPosition) {
            case 0: //view profile
                editName.setVisibility(View.INVISIBLE);
                editDOB.setVisibility(View.INVISIBLE);
                nameView.setText(currentUser.getName());
                dobView.setText(currentUser.getDob());
                nameView.setVisibility(View.VISIBLE);
                dobView.setVisibility(View.VISIBLE);
                updateProfileButton.setVisibility(View.INVISIBLE);

                break;
            case 1: // update profile
                nameView.setVisibility(View.INVISIBLE);
                dobView.setVisibility(View.INVISIBLE);
                editName.setText(currentUser.getName());
                editDOB.setText(currentUser.getDob());
                editName.setVisibility(View.VISIBLE);
                editDOB.setVisibility(View.VISIBLE);
                updateProfileButton.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
