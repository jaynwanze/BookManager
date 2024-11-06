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
import com.example.bookmanager.dao.UserDAO;
import com.example.bookmanager.textchange.TextChangeHandler;


import java.util.List;

public class SignUpActivity extends AppCompatActivity {

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



        //add text change listener to password field
        TextChangeHandler tch = new TextChangeHandler(this);
        EditText editPassword = findViewById(R.id.password_toggle);
        editPassword.addTextChangedListener(tch);


        //add click listener to sign up button
        Button signUpBtn = findViewById(R.id.sign_up_button);
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            }
        });

        //add click listener to sign in button
        Button signInBtn = findViewById(R.id.sign_in_button);
        signInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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
        String userPassword = editPassword.getText().toString();
        UserDAO userDAO = new UserDAO();
        userDAO.createUserWithEmailAndPassword(userEmail, userPassword);
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
        UserDAO userDAO = new UserDAO();
        userDAO.signInWithEmailAndPassword(userEmail, userPassword);
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