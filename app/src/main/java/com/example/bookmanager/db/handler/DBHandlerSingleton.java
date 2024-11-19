package com.example.bookmanager.db.handler;

import android.content.Context;

public class DBHandlerSingleton {
    private static DBHandler instance;
    private DBHandlerSingleton() {}
    public static synchronized DBHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DBHandler(context.getApplicationContext());
        }
        return instance;
    }
}
