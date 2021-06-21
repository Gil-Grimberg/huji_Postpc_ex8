package com.example.postpc_ex8.workers;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;

public class RootsFinderWorker extends Worker {


    public RootsFinderWorker(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @SuppressLint("RestrictedApi")
    @NonNull
    @NotNull
    @Override
    public Result doWork() {

        int number = getInputData().getInt("input data",1);
        String output = "number is prime";
        if (number==0 || number==1 || number==2)
        {
            return new Result.Success(new Data.Builder().putString("output",output).build());
        }
        for (int i=2; i<number;i++)
        {
            if (number%i==0)
            {
                output = String.valueOf(number/i) + "x" + String.valueOf("i");
                return new Result.Success(new Data.Builder().putString("output",output).build());
            }

            int progress = 100*(number/i);
            setProgressAsync(new Data.Builder().putInt("progress",progress).build());

        }

        return new Result.Success(new Data.Builder().putString("output",output).build());    }
}
