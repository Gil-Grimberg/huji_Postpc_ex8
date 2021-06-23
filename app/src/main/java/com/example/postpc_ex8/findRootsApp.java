package com.example.postpc_ex8;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class findRootsApp extends Application {

    private findRootsHolder dataBase;

    public findRootsHolder getDataBase(){
        return dataBase;
    }
    public static findRootsApp instance = null;

    public static findRootsApp getInstance(){
        return instance;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        dataBase = new findRootsHolder(this);
    }

}
