package com.example.bookmanager.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.pojo.Book;

import java.util.ArrayList;

public class BookDAO {
    public static final String TABLE_BOOK = "Book";
    public static final String COL_ID = "ID";
    public static final String COL_USER_ID = "UserID";
    public static final String COL_TITLE = "Title";
    public static final String COL_AUTHOR = "Author";
    public static final String COL_CATEGORY = "Category";
    public static final String COL_START_DATE = "StartDate";
    public static final String COL_REVIEW = "Review";
    public static final String COL_STATUS = "Status";
    private static BookDAO instance;
    private final DBHandler dbHandler;

    // Private constructor
    private BookDAO(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    // Singleton getInstance method
    public static synchronized BookDAO getInstance(DBHandler dbHandler) {
        if (instance == null) {
            instance = new BookDAO(dbHandler);
        }
        return instance;
    }

    public boolean createBook(String title, String author, String category, String startDate, String review, String status, int userId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_BOOK + " (" +
                COL_USER_ID + ", " +
                COL_TITLE + ", " +
                COL_AUTHOR + ", " +
                COL_CATEGORY + ", " +
                COL_START_DATE + ", " +
                COL_REVIEW + ", " +
                COL_STATUS +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            db.execSQL(sql, new Object[]{
                    userId, title, author, category, startDate, review, status
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public ArrayList<Book> getAllBooks(int userId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_BOOK + " WHERE " + COL_USER_ID + " = " + userId;
        try  {
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Book book = new Book();
                book.setId(cursor.getInt(0));
                book.setUserId(cursor.getInt(1));
                book.setTitle(cursor.getString(2));
                book.setAuthor(cursor.getString(3));
                book.setCategory(cursor.getString(4));
                book.setStartDate(cursor.getString(5));
                book.setReview(cursor.getString(6));
                book.setStatus(cursor.getString(7));
                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return books;
    }


    public boolean removeBook(int id) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_BOOK + " WHERE " + COL_ID + " = " + id;
        try  {
            db.execSQL(sql);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public Book getBook(int id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_BOOK + " WHERE " + COL_ID + " = " + id;
        Book book = null;

        try {
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                 book = new Book();
                book.setId(cursor.getInt(0));
                book.setUserId(cursor.getInt(1));
                book.setTitle(cursor.getString(2));
                book.setAuthor(cursor.getString(3));
                book.setCategory(cursor.getString(4));
                book.setStartDate(cursor.getString(5));
                book.setReview(cursor.getString(6));
                book.setStatus(cursor.getString(7));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return book;
        }
        return book;
    }

    public boolean updateBook(int id, int userId, String review, String status) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String sqlUpdate = "UPDATE " + TABLE_BOOK + " SET " +
                COL_REVIEW + " = ?, " +
                COL_STATUS + " = ? " +
                "WHERE " + COL_ID + " = ?" + " AND " + COL_USER_ID + " = ?";
        try  {
            db.execSQL(sqlUpdate, new Object[]{review, status, id, userId});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
