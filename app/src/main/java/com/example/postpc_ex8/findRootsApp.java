package com.example.postpc_ex8;

import android.app.Application;

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
        // todo: dealing with workers, create a worker class and define the calculation there
        instance = this;
        dataBase = new findRootsHolder(this);
    }
}
