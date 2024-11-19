package com.example.bookmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.bookmanager.R;
import com.example.bookmanager.db.dao.UserDAO;
import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.db.handler.DBHandlerSingleton;
import com.example.bookmanager.fragment.SignInFragment;
import com.example.bookmanager.fragment.SignUpFragment;
import com.example.bookmanager.textchange.TextChangeHandler;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

public class SignUpActivity extends AppCompatActivity {
    private Fragment signInFragment;
    private Fragment signUpFragment;
    private View signInView;
    private View signUpView;
    private DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_sign_up), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Create db
        dbHandler = DBHandlerSingleton.getInstance(this);
        dbHandler.getWritableDatabase();
        UserDAO userDAO = UserDAO.getInstance(dbHandler);

        this.signInFragment = new SignInFragment();
        this.signUpFragment = new SignUpFragment();


        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, signInFragment, "signInFragment")
                .add(R.id.frameLayout, signUpFragment, "signUpFragment")
                .hide(signUpFragment)
                .commit();

        getSupportFragmentManager().executePendingTransactions();

        // Add text change listener to password fields
        signInFragment.getViewLifecycleOwnerLiveData().observe(this, lifecycleOwner -> {
            signInView = signInFragment.getView();
            TextChangeHandler tch = new TextChangeHandler(this);
            EditText editPassword = signInView.findViewById(R.id.password_toggle);
            editPassword.addTextChangedListener(tch);


        });

        signUpFragment.getViewLifecycleOwnerLiveData().observe(this, lifecycleOwner -> {
            TextChangeHandler tch = new TextChangeHandler(this);
            signUpView = signUpFragment.getView();
            EditText editPassword = signUpView.findViewById(R.id.password_toggle);
            editPassword.addTextChangedListener(tch);
        });

        //Set up tabs
        TabLayout filterTabs = findViewById(R.id.sign_in_tab);
        filterTabs.addTab(filterTabs.newTab().setText("Sign In"));
        filterTabs.addTab(filterTabs.newTab().setText("Sign Up"));
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


        Button submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signInFragment.isVisible()){
                    signIn();
                }
                else {
                    signUp();
                }
            }
        });

    }

    //check if password is strong enough
    public void validateWeakOrStrongPassword() {
        View currentView = this.signInFragment.isVisible() ? this.signInView : this.signUpView;
        EditText editPassword = currentView.findViewById(R.id.password_toggle);
        if (editPassword.length() >= 6 && editPassword.length() < 8) {
            TextView passwordValidation = findViewById(R.id.password_validation);
            passwordValidation.setText(R.string.weak_password);
        }
        else if (editPassword.length() >= 7) {
            TextView passwordValidation = findViewById(R.id.password_validation);
            passwordValidation.setText(R.string.strong_password);
        }
        else{
            TextView passwordValidation = findViewById(R.id.password_validation);
            passwordValidation.setText("");
        }
    }

    //check if password is valid
    private boolean validatePassword() {
        View currentView = this.signInFragment.isVisible() ? this.signInView : this.signUpView;
        EditText editPassword = currentView.findViewById(R.id.password_toggle);
            return editPassword.length() >= 6 && !editPassword.getText().toString().contains(" ") && editPassword.getText().toString().matches(".*\\d.*");
    }

    //check if email is valid
    private boolean validateEmail() {
        View currentView = this.signInFragment.isVisible() ? this.signInView : this.signUpView;
        EditText editEmail = currentView.findViewById(R.id.edit_email);
        // Get the string and remove any extra spaces
        String email = editEmail.getText().toString().trim();
        //Validation
        return email.contains("@") && !email.contains(" ");
    }

    public boolean validateFullName() {
        View currentView = this.signInFragment.isVisible() ? this.signInView : this.signUpView;
        EditText editName = currentView.findViewById(R.id.name_edit);
        String fullName = editName.getText().toString();
       return !fullName.isEmpty() || fullName.matches("^[a-zA-Z\\s]+$");
    }

    public boolean validateDateOfBirth() {
        View currentView = this.signInFragment.isVisible() ? this.signInView : this.signUpView;
        EditText editDOB = currentView.findViewById(R.id.name_dob);
        String userDOB = editDOB.getText().toString();

        return !userDOB.isEmpty() || userDOB.matches("\\d{2}/\\d{2}/\\d{4}");
    }

    //sign up user
    private void signUp() {
        if (!validateEmail()) {
            Toast.makeText(SignUpActivity.this, "Invalid Email: Must contain @ and no spaces", Toast.LENGTH_LONG).show();
            return;
        }

        else if (!validatePassword()) {
            Toast.makeText(SignUpActivity.this, "Invalid Password: Must be at least 6 characters, have no spaces and contain at least 1 digit", Toast.LENGTH_LONG).show();
            return;
        }

        else if (!validateFullName()) {
            Toast.makeText(SignUpActivity.this, "Invalid Name: Must contain only letters and spaces", Toast.LENGTH_LONG).show();
            return;
        }
        else if (!validateDateOfBirth()) {
            Toast.makeText(SignUpActivity.this, "Invalid Date of Birth: Must be in the format DD/MM/YYYY", Toast.LENGTH_LONG).show();
            return;
        }
        EditText editDOB = signUpView.findViewById(R.id.name_dob);
        String userDOB = editDOB.getText().toString();
        if (userDOB.length() != 10) {
            Toast.makeText(SignUpActivity.this, "Invalid Date of Birth: Must be in the format DD/MM/YYYY", Toast.LENGTH_LONG).show();
            return;
        }

        if (Integer.parseInt(userDOB.substring(0,2)) > 31)
             {
             Toast.makeText(SignUpActivity.this, "Invalid Date of Birth: Day must be between 1 and 31", Toast.LENGTH_LONG).show();
             return;
         }

        else if (Integer.parseInt(userDOB.substring(3,5)) > 12)
        {
            Toast.makeText(SignUpActivity.this, "Invalid Date of Birth: Month must be between 1 and 12", Toast.LENGTH_LONG).show();
            return;
        }

        else if( Integer.parseInt(userDOB.substring(6,10)) > 2012)
        {
            Toast.makeText(SignUpActivity.this, "Invalid Date of Birth: Must be born before year 2012", Toast.LENGTH_LONG).show();
            return;
        }

        EditText editEmail = signUpView.findViewById(R.id.edit_email);
        String userEmail = editEmail.getText().toString();
        EditText editPassword = signUpView.findViewById(R.id.password_toggle);
        EditText editName = signUpView.findViewById(R.id.name_edit);
        editDOB = findViewById(R.id.name_dob);
        String fullName = editName.getText().toString();
        userDOB = editDOB.getText().toString();
        String userPassword = editPassword.getText().toString();
        UserDAO userDAO = UserDAO.getInstance(this.dbHandler);
        //check if user already exists
        boolean userExists = userDAO.checkIfUserExists(userEmail);
        if (userExists) {
            Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_LONG).show();
            return;
        }
        //check if user was created
        boolean userCreated = userDAO.createUser(userEmail, userPassword, fullName, userDOB);
        if (!userCreated) {
            Toast.makeText(SignUpActivity.this, "Failed to create user", Toast.LENGTH_LONG).show();
            return;
        }

        //clear fields
        editEmail.setText("");
        editPassword.setText("");
        editName.setText("");
        editDOB.setText("");
        Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class );
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);
        this.finish();

    }

    //sign in user
    private void signIn() {
        if (!validateEmail()) {
            Toast.makeText(SignUpActivity.this, "Invalid Email: Must contain @ and no spaces", Toast.LENGTH_LONG).show();
            return;
        }

        else if (!validatePassword()) {
            Toast.makeText(SignUpActivity.this, "Invalid Password: Must be at least 6 characters, have no spaces and contain at least 1 digits", Toast.LENGTH_LONG).show();
            return;
        }

        EditText editEmail = signInView.findViewById(R.id.edit_email);
        String userEmail = editEmail.getText().toString();
        EditText editPassword = signInView.findViewById(R.id.password_toggle);
        String userPassword = editPassword.getText().toString();
        UserDAO userDAO = UserDAO.getInstance(this.dbHandler);
        //check if user exists
        boolean userExists = userDAO.checkIfUserExists(userEmail);
        if (!userExists) {
            Toast.makeText(SignUpActivity.this, "User does not exist", Toast.LENGTH_LONG).show();
            return;
        }
        //check if user was signed in
        boolean userSignedIn = userDAO.signInUser(userEmail, userPassword);
        if (!userSignedIn) {
            Toast.makeText(SignUpActivity.this, "Password is incorrect", Toast.LENGTH_LONG).show();
            return;
        }
        //clear fields
        editEmail.setText("");
        editPassword.setText("");
        Toast.makeText(SignUpActivity.this, "User Signed In", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class );
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);
        this.finish();
    }


    private void applyFilter(int tabPosition) {
        switch (tabPosition) {
            case 0: // Sign In
                getSupportFragmentManager().beginTransaction()
                        .show(signInFragment)
                        .hide(signUpFragment)
                        .commit();
                break;
            case 1: // Sign Up
                getSupportFragmentManager().beginTransaction()
                        .show(signUpFragment)
                        .hide(signInFragment)
                        .commit();
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("myActivity", "App is restoring");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("myActivity", "App is starting");

    }

    @Override
    protected void onStop () {
        super.onStop();
        Log.i("myActivity", "App is stopping");
    }

    @Override
    protected void onPause () {
        super.onPause();
        Log.i("myActivity", "Switching to another app ");
    }
}