package com.example.bookmanager.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.pojo.Book;

import java.util.ArrayList;

public class BookDAO {
    private final DBHandler dbhandler;
    public static final String TABLE_BOOK = "Book";
    public static final String COL_ID = "ID";
    public static final String COL_USER_ID = "UserID";
    public static final String COL_TITLE = "Title";
    public static final String COL_AUTHOR = "Author";
    public static final String COL_CATEGORY = "Category";
    public static final String COL_START_DATE = "StartDate";
    public static final String COL_REVIEW = "Review";
    public static final String COL_STATUS = "Status";

    public BookDAO(DBHandler dbhandler) {
        this.dbhandler = dbhandler;
    }

    public boolean createBook(String title, String author, String category, String startDate, String review, String status, int userId) {
        String sql = "INSERT INTO " + TABLE_BOOK + " (" +
                COL_USER_ID + ", " +
                COL_TITLE + ", " +
                COL_AUTHOR + ", " +
                COL_CATEGORY + ", " +
                COL_START_DATE + ", " +
                COL_REVIEW + ", " +
                COL_STATUS +
                ") VALUES ('" +
                userId + "','" +
                title + "','" +
                author + "','" +
                category + "','" +
                startDate + "','" +
                review + "','" +
                status + "')";

        try (SQLiteDatabase db = dbhandler.getWritableDatabase()) {
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            dbhandler.close();
        }
    }

    public ArrayList<Book> getAllBooks(int userId) {
        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_BOOK + " WHERE " + COL_USER_ID + " = " + userId;
        try (SQLiteDatabase db = dbhandler.getReadableDatabase()) {
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
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            dbhandler.close();
        }
        return books;
    }


    public boolean removeBook(int id) {
        String sql = "DELETE FROM " + TABLE_BOOK + " WHERE " + COL_ID + " = " + id;
        try (SQLiteDatabase db = dbhandler.getWritableDatabase()) {
            db.execSQL(sql);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            dbhandler.close();

        }
    }

    public Book getBook(int id) {
        String sql = "SELECT * FROM " + TABLE_BOOK + " WHERE " + COL_ID + " = " + id;
        Book book = null;

        try (SQLiteDatabase db = dbhandler.getReadableDatabase()) {
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
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            return book;
        } finally {
            dbhandler.close();
        }
        return book;
    }

    public boolean updateBook(int id, int userId, String review, String status) {
        String sql = "UPDATE " + TABLE_BOOK + " SET " +
                COL_REVIEW + " = '" + review + "', " +
                COL_STATUS + " = '" + status + "' " +
                "WHERE " + COL_ID + " = " + id;

        try (SQLiteDatabase db = dbhandler.getWritableDatabase()) {
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            dbhandler.close();
        }
    }
}
