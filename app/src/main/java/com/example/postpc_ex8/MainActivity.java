package com.example.postpc_ex8;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.postpc_ex8.workers.RootsFinderWorker;
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
        workManager = WorkManager.getInstance(this);
        if (holder == null) {
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
            // than create a new worker and RootsFinder
            Long number;
            try{
                number = Long.parseLong(inputNumber.getText().toString());
            }
            catch (Exception e)
            {
                Toast.makeText(this,"Please enter a valid number",Toast.LENGTH_SHORT).show();
                return;

            }
            RootsFinder finder = new RootsFinder(number,"Calculating roots for",String.valueOf(number),0L);
            holder.addFinder(finder,2L);
            inputNumber.setText("");
            adapter.setRootsFinders(holder.getCurrentFinders());
        });
//        workManager.cancelAllWorkByTag("interesting");
//        workManager.pruneWork();
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
                        if (finder.getId().equals(workerId))
                        {
                            Long total = progress.getLong("progress", 0);
                            if (total != -1) {
                                finder.setProgress(total);
                            }
                            if (workInfo.getState().equals(WorkInfo.State.SUCCEEDED)) {
                                finder.setProgress(100L);
                                String output = workInfo.getOutputData().getString("output");
                                finder.preffix = "Roots for " + finder.getNumber() + " are: ";
                                finder.suffix = output;
                            }
                            else if(workInfo.getState().equals(WorkInfo.State.FAILED))
                            {
                                Long currentIteration = workInfo.getOutputData().getLong("i",2L);
                                holder.deleteFinder(finder);
                                Long num = finder.getNumber();
                                String pre =finder.getPreffix();
                                String suff = finder.getSuffix();
                                Long progressState = finder.getProgress();
                                UUID finderId = workInfo.getId();
                                RootsFinder newFinder = new RootsFinder(num,pre,suff,progressState,finderId);
                                holder.addFinder(newFinder,currentIteration);
                            }
                            holder.updateFinder(finder);
                            adapter.setRootsFinders(holder.getCurrentFinders());

                        }
                    }
                }

            }
        });

    }
}