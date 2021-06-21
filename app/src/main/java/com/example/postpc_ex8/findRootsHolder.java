package com.example.postpc_ex8;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.postpc_ex8.workers.RootsFinderWorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class findRootsHolder {

    ArrayList<RootsFinder> rootsFindersList = new ArrayList<>();
    private final Context context;
    private SharedPreferences sp; //todo: needed? yes
    WorkManager workManager;


    public findRootsHolder(Context context) {
        this.context = context;
        this.sp = context.getSharedPreferences("local_db", Context.MODE_PRIVATE);
        workManager = WorkManager.getInstance(context);
        initializeFromSp();
    }

    private void initializeFromSp() {
//        Set<String> toDoItems = sp.getAll().keySet();
//        for (String key : toDoItems) {
//            String itemStringFromSp = sp.getString(key,null);
//            TodoItem itemFromString = TodoItem.string_to_Item(itemStringFromSp); // make sure this static method works!
//            if (itemFromString!=null){
//                itemsList.add(itemFromString);
//            }
//
//        }
//        Collections.sort(itemsList, new ToDoItemsCompartor());
//        toDoItemsLiveDataMutable.setValue(new ArrayList<TodoItem>(itemsList));
    }

    public List<RootsFinder> getCurrentFinders() {

        return new ArrayList<>(rootsFindersList);
    }

    public void addFinder(RootsFinder finder) {
        // todo: create a request
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(RootsFinderWorker.class)
                .setInputData(new Data.Builder().putInt("input number", finder.getNumber()).build()).addTag("interesting")
                .build();

        WorkManager.getInstance(this.context).enqueue(workRequest);

        finder.setId(workRequest.getId());
        rootsFindersList.add(finder);
//        Collections.sort(itemsList, new ToDoItemsCompartor());
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(finder.getId(), finder.serializable());
//        editor.apply();
    }

    public void deleteFinder(RootsFinder finder) {
        if (finder == null) return;
        WorkManager.getInstance(this.context).cancelWorkById(finder.getId());
        rootsFindersList.remove(finder);
        // todo: delete\cancel a request

//        Collections.sort(itemsList, new ToDoItemsCompartor());
//        SharedPreferences.Editor editor = sp.edit();
//        editor.remove(finder.getId());
//        editor.apply();
    }

    public void clear() {
        // todo: maybe need to cancel all workers? not sure
        rootsFindersList.clear();
    }

    public void addAll(List<RootsFinder> finders) {
        for (RootsFinder finder : finders) {
            this.addFinder(finder);
        }
    }

    public int size() {
        return this.rootsFindersList.size();
    }


}
