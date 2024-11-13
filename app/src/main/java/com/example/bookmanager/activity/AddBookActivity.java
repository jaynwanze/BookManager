package com.example.bookmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookmanager.R;
import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.pojo.Book;
import com.example.bookmanager.pojo.User;

import java.util.ArrayList;

public class AddBookActivity extends AppCompatActivity {
    private DBHandler dbhandler;
    private String currentUserEmail;

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
               //
            }
        });

    }
}
