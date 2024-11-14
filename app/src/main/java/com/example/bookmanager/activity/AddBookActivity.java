package com.example.bookmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookmanager.R;
import com.example.bookmanager.db.dao.BookDAO;
import com.example.bookmanager.db.dao.UserDAO;
import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.pojo.Book;
import com.example.bookmanager.pojo.User;

import java.util.ArrayList;

public class AddBookActivity extends AppCompatActivity {
    private DBHandler dbhandler;
    private String currentUserEmail;
    private User currentUser ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_add_book), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //Create database handler
        dbhandler = new DBHandler(this);
        dbhandler.getWritableDatabase();
        //get user email from intent
        Intent intent = getIntent();
        this.currentUserEmail = intent.getStringExtra("userEmail");
        UserDAO userDAO = new UserDAO(dbhandler);
        currentUser = userDAO.getUserLoggedIn(currentUserEmail);

        //add text change listener to password field
        //Cmake this a callback
        //TextChangeHandler tch = new TextChangeHandler(this);

        Spinner status_dropdown = findViewById(R.id.status_dropdown);
        status_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String currentSelectedStatus = parent.getItemAtPosition(position).toString();
                TextView reviewLabel = findViewById(R.id.review_label);
                EditText reviewEdit = findViewById(R.id.review_edit);
                if (currentSelectedStatus.equalsIgnoreCase("In progress")) {
                    reviewLabel.setVisibility(View.INVISIBLE);
                    reviewEdit.setVisibility(View.INVISIBLE);
                }
                else{
                    reviewLabel.setVisibility(View.VISIBLE);
                    reviewEdit.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // ... your logic here ...
            }
        });

        Button addButton = findViewById(R.id.add_book);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });

    }
    private void addBook() {
        EditText titleEdit = findViewById(R.id.edit_title);
        EditText authorEdit = findViewById(R.id.author_edit);
        EditText startDateEdit = findViewById(R.id.start_date_edit);
        EditText reviewEdit = findViewById(R.id.review_edit);
        Spinner status_dropdown = findViewById(R.id.status_dropdown);
        Spinner category_dropdown = findViewById(R.id.category_dropdown);

        String title = titleEdit.getText().toString();
        String author = authorEdit.getText().toString();
        String category =  category_dropdown.getSelectedItem().toString();
        String startDate = startDateEdit.getText().toString();
        String review = reviewEdit.getText().toString();
        String status = status_dropdown.getSelectedItem().toString();
        BookDAO bookDAO = new BookDAO(dbhandler);
        boolean bookAdded = false;
        if (review.isEmpty() | review.isBlank()){
            review = "";
        }
        bookAdded  = bookDAO.createBook(title,author, category,startDate,review,status,currentUser.getId());
        if (bookAdded){
            Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Error adding book", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbhandler.close();
    }


}
