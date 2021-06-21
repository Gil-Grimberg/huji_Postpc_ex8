package com.example.postpc_ex8;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public findRootsHolder holder = null;
    findRootsAdapter adapter;
    WorkManager workManager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (holder == null) {
//            holder = new TodoItemsHolderImpl();
            holder = findRootsApp.getInstance().getDataBase(); // replace the last row with this??
        }

        adapter = new findRootsAdapter((findRootsHolder)holder);
        adapter.setRootsFinders(holder.getCurrentFinders());
        RecyclerView recyclerRootsFindersList = findViewById(R.id.recyclerCalculationsList);
        recyclerRootsFindersList.setAdapter(adapter);
        recyclerRootsFindersList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        EditText inputNumber = findViewById(R.id.insertNumber);
        FloatingActionButton findRootsButton = findViewById(R.id.buttonCalculateRoots);
        findRootsButton.setOnClickListener(v->{
            // todo: check validity of the number
            // than create a new worker and RootsFinder
            RootsFinder finder = new RootsFinder(100,"preffix try","suffix try",100);
            holder.addFinder(finder);
            adapter.setRootsFinders(holder.getCurrentFinders());
        });

        try {


        LiveData<List<WorkInfo>> liveData = workManager.getWorkInfosByTagLiveData("interesting");
        liveData.observe(this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {

                for (WorkInfo workInfo:workInfos)
                {
                    UUID workerId = workInfo.getId();
                    Data progress = workInfo.getProgress();
                    for (RootsFinder finder:holder.getCurrentFinders())
                    {
                        if (finder.getId()==workerId)
                        {
                            int total = progress.getInt("progress", -1);
                            if (total != -1) {
                                finder.setProgress(total);
                                //todo: maybe delete the finder from the holder and create a new one with updated params
                            }
                            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                finder.setProgress(100);
                                String output = workInfo.getOutputData().getString("output");
                                finder.preffix = "Roots for " + finder.getNumber() + " are: ";
                                finder.suffix = output;
                            }

                        }
                    }
                }

            }
        });}
        catch (Exception e)
        {
            Log.d("1","no workers");
        }
    }
}