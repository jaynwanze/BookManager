package com.example.bookmanager.utils;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmanager.R;
import com.example.bookmanager.activity.HomePageActivity;
import com.example.bookmanager.activity.UpdateBookActivity;
import com.example.bookmanager.db.dao.BookDAO;
import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.db.handler.DBHandlerSingleton;
import com.example.bookmanager.pojo.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private ArrayList<Book> mylistvalues;
    private String currentUserEmail;
    private HomePageActivity activity;
    private DBHandler dbHandler;

    // Provide a reference to the views for each data item
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView; //refer to the text view of row layout
        public BookViewHolder(View itemView) {
            super(itemView); //itemView corresponds to all views defined in row layout
            txtView = (TextView) itemView.findViewById(R.id.row_element);

        }
    }
    //Book Adapter Constructor
    public BookAdapter(ArrayList<Book> myDataset, String currentUserEmail, HomePageActivity activity) {
        this.mylistvalues = myDataset;
        this.currentUserEmail = currentUserEmail;
        this.activity = activity;
    }
    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row_layout, parent, false);

        BookViewHolder viewHolder = new BookViewHolder(itemView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = mylistvalues.get(position);
        holder.txtView.setText(book.toString());
        holder.txtView.setOnClickListener(v -> {
            // Always get the current position dynamically
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            Book clickedBook = mylistvalues.get(currentPosition);

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(clickedBook.getTitle() + " by: " + clickedBook.getAuthor());
            String reviewMessage = (clickedBook.getReview() == null || clickedBook.getReview().isEmpty())
                    ? "No review available - please update to add a review"
                    : "Review: " + clickedBook.getReview();
            builder.setMessage(reviewMessage);

            builder.setPositiveButton("Update", (dialog, which) -> {
                Intent intent = new Intent(v.getContext(), UpdateBookActivity.class);
                intent.putExtra("userEmail", currentUserEmail);
                intent.putExtra("bookId", String.valueOf(clickedBook.getId()));
                v.getContext().startActivity(intent);
                activity.setRecyclerView();
            });

            builder.setNegativeButton("Remove", (dialog, which) -> {
                if (currentPosition >= 0 && currentPosition < mylistvalues.size()) {
                     dbHandler = DBHandlerSingleton.getInstance(v.getContext());
                    BookDAO bookDAO = BookDAO.getInstance(this.dbHandler);
                    boolean bookRemoved = bookDAO.removeBook(clickedBook.getId());

                    if (bookRemoved) {
                        remove(currentPosition);
                        Toast.makeText(v.getContext(), "Book removed", Toast.LENGTH_SHORT).show();
                        activity.setRecyclerView();
                    } else {
                        Toast.makeText(v.getContext(), "Book not removed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.show();
        });
    }




    // Return the size of your dataset
    @Override
    public int getItemCount() {
        if (mylistvalues != null) {
            return  mylistvalues.size();
        }
        else {
            return 0;
        }
    }

    public void add(int position, Book item)
    {
        mylistvalues.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position)
    {
        mylistvalues.remove(position);
        notifyItemRemoved(position);
    }
    public void update(int position, Book item)  {
        mylistvalues.set(position, item);
        notifyItemChanged(position);
    }

    public void updateDataSet(ArrayList<Book> newData) {
        this.mylistvalues = newData;
        notifyDataSetChanged();
    }
}
