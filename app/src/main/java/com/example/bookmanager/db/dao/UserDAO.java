package com.example.bookmanager.db.dao;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookmanager.db.handler.DBHandler;
import com.example.bookmanager.pojo.User;

public class UserDAO {
    private static UserDAO instance;
    private final DBHandler dbHandler;
    public static final String TABLE_USER="User";
    public static final String COL_EMAIL="Email";
    public static final String COL_PASSWORD="Password";
    public static final String COL_DOB="DOB";
    public static final String COL_NAME="Name";


    private UserDAO(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    public static synchronized UserDAO getInstance(DBHandler dbHandler) {
        if (instance == null) {
            instance = new UserDAO(dbHandler);
        }
        return instance;
    }
    public boolean createUser(String email, String password, String name, String dob) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String sqlInsertStatement = "INSERT INTO " + TABLE_USER + " (" +
                COL_EMAIL + ", " +
                COL_PASSWORD + ", " +
                COL_NAME + ", " +
                COL_DOB +
                ") VALUES (?, ?, ?, ?)";
        try {
            db.execSQL(sqlInsertStatement, new Object[]{email.toLowerCase(), password, name, dob});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean signInUser(String userEmail , String userPassword)   {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String sqlSelect = "SELECT * FROM " + TABLE_USER + " WHERE " + COL_EMAIL + " = '" + userEmail + "' AND " + COL_PASSWORD + " = '" + userPassword + "'";
        //using rawQuery when return a cursor
        Cursor cursor = db.rawQuery(sqlSelect,null);
        if (cursor.moveToFirst()) return cursor.getString(1).equalsIgnoreCase(userEmail) && cursor.getString(2).equals(userPassword);
        return false;


    }

    public boolean checkIfUserExists (String userEmail){
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String sqlSelect = "SELECT * FROM " + TABLE_USER + " WHERE " + COL_EMAIL + " = '" + userEmail + "'";
        //using rawQuery when return a cursor
        Cursor cursor = db.rawQuery(sqlSelect,null);
        if (cursor.moveToFirst()) return cursor.getString(1).equalsIgnoreCase(userEmail);
        return false;

    }

    public User getUserLoggedIn(String userEmail){
        SQLiteDatabase db = dbHandler.getReadableDatabase();
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
        return null;
    }

    public boolean updateUserProfile(String name, String dob, String userEmail) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String sqlUpdate = "UPDATE " + TABLE_USER + " SET " +
                COL_NAME + " = ?, " +
                COL_DOB + " = ? " +
                "WHERE " + COL_EMAIL + " = ?";
        try  {
            db.execSQL(sqlUpdate, new Object[]{name, dob, userEmail});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
