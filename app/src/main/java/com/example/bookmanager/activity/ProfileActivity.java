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
    private DBHandler dbhandler;
    private String currentUserEmail;
    private User currentUser ;


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
        dbhandler = new DBHandler(this);
        dbhandler.getWritableDatabase();
        currentUserEmail = getIntent().getStringExtra("userEmail");
        UserDAO userDAO = new UserDAO(dbhandler);
        currentUser = userDAO.getUserLoggedIn(currentUserEmail);

        //Set up tabs
        // Create a TabLayout and add tabs
        TextView viewName = findViewById(R.id.view_name);
        viewName.setText(currentUser.getName());
        TextView viewDOB = findViewById(R.id.view_dob);
        viewDOB.setText(currentUser.getDob());
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

    private void updateProfile() {
        EditText editName = findViewById(R.id.edit_name);
        EditText editDOB = findViewById(R.id.dob_edit);
        String newName = editName.getText().toString();
        String newDOB = editDOB.getText().toString();
        UserDAO userDAO = new UserDAO(dbhandler);
        boolean userProfileUpdated = userDAO.updateUserProfile(newName,newDOB,currentUserEmail);
        if (userProfileUpdated){
            currentUser.setName(newName);
            currentUser.setDob(newDOB);
            applyFilter(1);
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Error updating profile", Toast.LENGTH_SHORT).show();
        }

    }

    private void applyFilter(int tabPosition) {

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
        dbhandler.close();
    }


}
