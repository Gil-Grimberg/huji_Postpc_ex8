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

        Long number = getInputData().getLong("input number",1);
        String output = "number is prime";
        if (number==0 || number==1 || number==2)
        {
            return new Result.Success(new Data.Builder().putString("output",output).build());
        }
        for (Long i = 2L; i<number; i++)
        {
            if (number%i==0)
            {
                output = String.valueOf(number/i) + "x" + String.valueOf(i);
                return new Result.Success(new Data.Builder().putString("output",output).build());
            }

            Long progress = (100*i)/number;
            setProgressAsync(new Data.Builder().putLong("progress",progress).build());

        }

        return new Result.Success(new Data.Builder().putString("output",output).build());    }
}
