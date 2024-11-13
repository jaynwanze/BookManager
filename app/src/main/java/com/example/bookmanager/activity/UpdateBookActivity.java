package com.example.bookmanager.activity;

import android.os.Bundle;
import android.view.View;
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
import com.example.bookmanager.db.dao.BookDAO;
import com.example.bookmanager.db.dao.UserDAO;
import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.pojo.Book;
import com.example.bookmanager.pojo.User;
import com.google.android.material.tabs.TabLayout;

public class UpdateBookActivity extends AppCompatActivity {
    private DBHandler dbhandler;
    private String currentUserEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_update_book), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbhandler = new DBHandler(this);
        dbhandler.getWritableDatabase();
        currentUserEmail = getIntent().getStringExtra("userEmail");
        int bookId = getIntent().getIntExtra("bookId", -1);
        BookDAO bookDAO = new BookDAO(dbhandler);
        UserDAO userDAO = new UserDAO(dbhandler);
        User user = userDAO.getUserLoggedIn(currentUserEmail);
        Book book = getIntent().getParcelableExtra("book");
        if (book != null) {
        TextView titleView = findViewById(R.id.view_title);
        titleView.setText(book.getTitle());
        TextView authorView = findViewById(R.id.view_author);
        authorView.setText(book.getAuthor());
        EditText reviewEdit = findViewById(R.id.review_edit);
        reviewEdit.setText(book.getReview());
        Spinner statusTab = findViewById(R.id.status_dropdown);
        if (book.getStatus().equals("In Progress")) {
            statusTab.setSelection(0);
        } else {
            statusTab.setSelection(1);
        }
    }
        Button updateBookBtn = findViewById(R.id.update_book);
        updateBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update book
                updateBook();
            }
        });

    }

    private void updateBook() {
        //update book

    }
}
