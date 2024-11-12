package com.example.bookmanager.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.pojo.User;

public class UserDAO {
    private final DBHandler dbhandler;
    public static final String TABLE_USER="User";
    public static final String COL_EMAIL="Email";
    public static final String COL_PASSWORD="Password";
    public static final String COL_DOB="DOB";
    public static final String COL_NAME="Name";


    public UserDAO (DBHandler dbhandler) {
        this.dbhandler = dbhandler;
    }

    public boolean createUser(String email , String password , String name , String dob)  {
        String sqlInsertStatement = "INSERT INTO " + TABLE_USER + " (" + COL_EMAIL + ", " + COL_PASSWORD + ", " + COL_NAME + ", " + COL_DOB + ") VALUES ('" + email.toLowerCase() + "','" + password + "','" + name + "','" + dob + "')";
        try (SQLiteDatabase db = dbhandler.getWritableDatabase()) {
            db.execSQL(sqlInsertStatement);
            return true;
        } catch (Exception e) {
            return false;
        }
        finally {
            dbhandler.close();
        }
    }

    public boolean signInUser(String userEmail , String userPassword)   {
        SQLiteDatabase db = dbhandler.getReadableDatabase();
        String sqlSelect = "SELECT * FROM " + TABLE_USER + " WHERE " + COL_EMAIL + " = '" + userEmail + "' AND " + COL_PASSWORD + " = '" + userPassword + "'";
        //using rawQuery when return a cursor
        Cursor cursor = db.rawQuery(sqlSelect,null);
        if (cursor.moveToFirst()) return cursor.getString(1).equalsIgnoreCase(userEmail) && cursor.getString(2).equals(userPassword);
        dbhandler.close();
        return false;


    }

    public boolean checkIfUserExists (String userEmail){
        SQLiteDatabase db = dbhandler.getReadableDatabase();
        String sqlSelect = "SELECT * FROM " + TABLE_USER + " WHERE " + COL_EMAIL + " = '" + userEmail + "'";
        //using rawQuery when return a cursor
        Cursor cursor = db.rawQuery(sqlSelect,null);
        if (cursor.moveToFirst()) return cursor.getString(1).equalsIgnoreCase(userEmail);

        dbhandler.close();
        return false;

    }

    public User getUserLoggedIn(String userEmail){
        SQLiteDatabase db = dbhandler.getReadableDatabase();
        String sqlSelect = "SELECT * FROM " + TABLE_USER + " WHERE " + COL_EMAIL + " = '" + userEmail + "'";
        //using rawQuery when return a cursor
        Cursor cursor = db.rawQuery(sqlSelect,null);
        User user = new User();
            if (cursor.moveToFirst()) {
                user.setId(cursor.getInt(0));
                user.setEmail(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setName(cursor.getString(3));
                user.setDob(cursor.getString(4));
                return user;
            }
        dbhandler.close();

        return null;

    }

}
