package com.example.bookmanager.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.bookmanager.db.handler.DBHandlerSingleton;
import com.example.bookmanager.pojo.Book;
import com.example.bookmanager.pojo.User;

public class UpdateBookActivity extends AppCompatActivity {
    private DBHandler dbHandler;

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

        dbHandler = DBHandlerSingleton.getInstance(this);

        // Retrieve Intent extras
        String currentUserEmail = getIntent().getStringExtra("userEmail");
        String bookId = getIntent().getStringExtra("bookId");

        if (bookId == null || bookId.isEmpty()) {
            Toast.makeText(this, "Error: Book ID is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        BookDAO bookDAO = BookDAO.getInstance(this.dbHandler);
        UserDAO userDAO = UserDAO.getInstance(this.dbHandler);
        User user = userDAO.getUserLoggedIn(currentUserEmail);
        Book book = bookDAO.getBook(Integer.parseInt(bookId));

        if (book != null) {
            TextView titleView = findViewById(R.id.view_title);
            titleView.setText(book.getTitle());

            TextView authorView = findViewById(R.id.view_author);
            authorView.setText(book.getAuthor());

            EditText reviewEdit = findViewById(R.id.review_edit);
            reviewEdit.setText(book.getReview());

            Spinner spinner = findViewById(R.id.status_dropdown);
            spinner.setSelection(book.getStatus().equals("In progress") ? 0 : 1);
        } else {
            Log.d("UpdateBookActivity", "Book not found with ID: " + bookId);
            Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
            this.finish(); // Close the activity if the book is not found
            return;
        }

        Button updateBookBtn = findViewById(R.id.update_book);
        updateBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBook(bookId, user.getId());
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void updateBook(String bookId, int userId) {
        EditText reviewEdit = findViewById(R.id.review_edit);
        String review = reviewEdit.getText().toString();
        Spinner spinner = findViewById(R.id.status_dropdown);
        String status = spinner.getSelectedItem().toString();
        BookDAO bookDAO = BookDAO.getInstance(this.dbHandler);
        boolean bookUpdated = bookDAO.updateBook(Integer.parseInt(bookId) ,userId, review, status);
        if (bookUpdated){
            Toast.makeText(this, "Book updated successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Error updating book", Toast.LENGTH_SHORT).show();
        }
    }
}
