package com.example.bookmanager.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmanager.R;
import com.example.bookmanager.pojo.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private ArrayList<Book> mylistvalues;
    // Provide a reference to the views for each data item
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView; //refer to the text view of row layout
        public BookViewHolder(View itemView) {
            super(itemView); //itemView corresponds to all views defined in row layout
            txtView = (TextView) itemView.findViewById(R.id.row_element);

        }
    }
    // constructor - Provide the dataset to the Adapter
//myDataset is passed when called to create an adapter object
    public BookAdapter(ArrayList<Book> myDataset) {
        mylistvalues = myDataset;
    }
    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
// create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.row_layout, parent, false); //false: inflate the row
//layout to parent and return view, if true return parent+view
        BookViewHolder viewHolder = new BookViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        int finalPosition = position;
        Book book = mylistvalues.get(finalPosition);
        holder.txtView.setText(book.toString());

        holder.txtView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Book book = mylistvalues.get(finalPosition);
                remove(finalPosition);
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
}
