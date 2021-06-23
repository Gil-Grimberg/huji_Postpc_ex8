package com.example.postpc_ex8;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.DocumentsContract;

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
import java.util.UUID;

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
        //todo: implement this! and a comperator to do sorting!
        Set<String> rootsFinders = sp.getAll().keySet();
        for (String key : rootsFinders) {
            String rootsFinderStringFromSp = sp.getString(key,null);
            RootsFinder rootsFinderFromString = RootsFinder.string_to_Item(rootsFinderStringFromSp); // make sure this static method works!
            if (rootsFinderFromString!=null){
                rootsFindersList.add(rootsFinderFromString);
            }

        }
        Collections.sort(rootsFindersList, new RootsFinderComparator());
//        toDoItemsLiveDataMutable.setValue(new ArrayList<TodoItem>(itemsList));
    }

    public List<RootsFinder> getCurrentFinders() {

        return new ArrayList<>(rootsFindersList);
    }

    public void addFinder(RootsFinder finder,Long calcIteration) {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(RootsFinderWorker.class)
                .setInputData(new Data.Builder().putLong("input number", finder.getNumber()).putLong("i",calcIteration).build()).addTag("interesting")
                .build();
        finder.setId(workRequest.getId());
        rootsFindersList.add(finder);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(String.valueOf(finder.getId()), finder.serializable());
        editor.apply();
        WorkManager.getInstance(this.context).enqueue(workRequest);


        Collections.sort(rootsFindersList, new RootsFinderComparator());
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(finder.getId(), finder.serializable());
//        editor.apply();
    }

    public void deleteFinder(RootsFinder finder) {
        if (finder == null) return;
        WorkManager.getInstance(this.context).cancelWorkById(finder.getId());
        rootsFindersList.remove(finder);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(String.valueOf(finder.getId()));
        editor.apply();
        // todo: delete\cancel a request

        Collections.sort(rootsFindersList, new RootsFinderComparator());
//        SharedPreferences.Editor editor = sp.edit();
//        editor.remove(finder.getId());
//        editor.apply();
    }

    public void updateFinder(RootsFinder finder)
    {
        UUID id = finder.getId();
        for (RootsFinder rootsFinder:this.rootsFindersList)
        {
            if (rootsFinder.getId().equals(id))
            {
                this.rootsFindersList.remove(rootsFinder);
                this.rootsFindersList.add(finder);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(String.valueOf(rootsFinder.getId()));
                editor.putString(String.valueOf(finder.getId()),finder.serializable());
                editor.apply();
                break;
            }
        }
    }

    public void clear() {
        // todo: maybe need to cancel all workers? not sure
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < rootsFindersList.size(); i++) {
            editor.remove(String.valueOf(rootsFindersList.get(i).getId()));
        }
        editor.apply();
        rootsFindersList.clear();
    }

    public void addAll(List<RootsFinder> finders) {
        if (!rootsFindersList.isEmpty())
        {
            rootsFindersList.clear();
        }
        SharedPreferences.Editor editor = sp.edit();
        for (RootsFinder finder : finders) {
            this.rootsFindersList.add(finder);
            editor.putString(String.valueOf(finder.getId()),finder.serializable());
        }
        editor.apply();
        Collections.sort(rootsFindersList, new RootsFinderComparator());
    }

    public int size() {
        return this.rootsFindersList.size();
    }


}
