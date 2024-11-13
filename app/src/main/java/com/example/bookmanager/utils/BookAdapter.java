package com.example.bookmanager.utils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmanager.R;
import com.example.bookmanager.activity.UpdateBookActivity;
import com.example.bookmanager.pojo.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private ArrayList<Book> mylistvalues;
    private String currentUserEmail;
    // Provide a reference to the views for each data item
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView; //refer to the text view of row layout
        public BookViewHolder(View itemView) {
            super(itemView); //itemView corresponds to all views defined in row layout
            txtView = (TextView) itemView.findViewById(R.id.row_element);

        }
    }
    //Book Adapter Constructor
    public BookAdapter(ArrayList<Book> myDataset, String currentUserEmail) {
        mylistvalues = myDataset;
        this.currentUserEmail = currentUserEmail;
    }
    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
// create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row_layout, parent, false); //false: inflate the row

        BookViewHolder viewHolder = new BookViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        int finalPosition = position;
        Book book = mylistvalues.get(finalPosition);
        holder.txtView.setText(book.toString());
        // Set click listener for the text view
        holder.txtView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(book.getTitle() + " by: " + book.getAuthor());
                if (book.getReview() == null) {
                    builder.setMessage("No review available - please update to add a review");
                }
                else {
                    builder.setMessage("Review: " + book.getReview());
                }

                builder.setPositiveButton("Update", (dialog, which) -> {
                   Intent intent = new Intent(v.getContext(), UpdateBookActivity.class);
                   intent.putExtra("userEmail", currentUserEmail);
                   intent.putExtra("bookId", book.getId());
                   v.getContext().startActivity(intent);
                });
                builder.setNegativeButton("Remove", (dialog, which) -> {
                    remove(finalPosition);
                });
                builder.show();
            }
        });
    }
    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return mylistvalues.size();
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
