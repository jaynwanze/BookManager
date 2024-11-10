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

import com.example.bookmanager.R;
import com.example.bookmanager.db.dao.UserDAO;
import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.textchange.TextChangeHandler;

public class SignUpActivity extends AppCompatActivity {
    private DBHandler dbhandler;

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
        dbhandler = new DBHandler(this);
        dbhandler.getWritableDatabase();

        //add text change listener to password field
        TextChangeHandler tch = new TextChangeHandler(this);
        EditText editPassword = findViewById(R.id.password_toggle);
        editPassword.addTextChangedListener(tch);
        TextView nameLabel = findViewById(R.id.name_label);
        TextView dobLabel = findViewById(R.id.DOB_label);

        EditText editName = findViewById(R.id.name_edit);
        EditText editDOB = findViewById(R.id.name_dob);


        //add click listener to sign up button
        Button signUpBtn = findViewById(R.id.sign_up_button);
        Button signInBtn = findViewById(R.id.sign_in_button);

        //add click listener to signup button
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                editName.setText("");
                editDOB.setText("");
                nameLabel.setVisibility(View.VISIBLE);
                dobLabel.setVisibility(View.VISIBLE);
                editName.setVisibility(View.VISIBLE);
                editDOB.setVisibility(View.VISIBLE);
                signInBtn.setVisibility(View.VISIBLE);
                signUpBtn.setVisibility(View.INVISIBLE);
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                editName.setText("");
                editDOB.setText("");
                nameLabel.setVisibility(View.INVISIBLE);
                dobLabel.setVisibility(View.INVISIBLE);
                editName.setVisibility(View.INVISIBLE);
                editDOB.setVisibility(View.INVISIBLE);
                signUpBtn.setVisibility(View.VISIBLE);
                signInBtn.setVisibility(View.INVISIBLE);
            }
        });

        Button submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editName.getVisibility() == View.INVISIBLE && editDOB.getVisibility() == View.INVISIBLE){
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
        EditText editPassword = findViewById(R.id.password_toggle);
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
        EditText editPassword = findViewById(R.id.password_toggle);
            return editPassword.length() >= 6 && !editPassword.getText().toString().contains(" ") && editPassword.getText().toString().matches(".*\\d.*");
    }

    //check if email is valid
    private boolean validateEmail() {
        EditText editEmail = findViewById(R.id.edit_email);
        // Get the string and remove any extra spaces
        String email = editEmail.getText().toString().trim();
        //Validation
        return email.contains("@") && !email.contains(" ");
    }

    //sign up user
    private void signUp() {
        if (!validateEmail()) {
            Toast.makeText(SignUpActivity.this, "Invalid Email: Must contain @ and no spaces", Toast.LENGTH_LONG).show();
            return;
        }

        else if (!validatePassword()) {
            Toast.makeText(SignUpActivity.this, "Invalid Password: Must be at least 6 characters, have no spaces and contain at least 1 digits", Toast.LENGTH_LONG).show();
            return;
        }

        EditText editEmail = findViewById(R.id.edit_email);
        String userEmail = editEmail.getText().toString();
        EditText editPassword = findViewById(R.id.password_toggle);
        EditText editName = findViewById(R.id.name_edit);
        EditText editDOB = findViewById(R.id.name_dob);
        String fullName = editName.getText().toString();
        String userDOB = editDOB.getText().toString();
        String userPassword = editPassword.getText().toString();
        UserDAO userDAO = new UserDAO(dbhandler);
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
        //Intent intent = new Intent(SignUpActivity.this, HomepageActivity.class );
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

        EditText editEmail = findViewById(R.id.edit_email);
        String userEmail = editEmail.getText().toString();
        EditText editPassword = findViewById(R.id.password_toggle);
        String userPassword = editPassword.getText().toString();
        UserDAO userDAO = new UserDAO(dbhandler);
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
        //Intent intent = new Intent(SignUpActivity.this, HomepageActivity.class);
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