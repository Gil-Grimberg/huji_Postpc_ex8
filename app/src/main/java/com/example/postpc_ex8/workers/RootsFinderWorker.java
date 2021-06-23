package com.example.postpc_ex8.workers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;

public class RootsFinderWorker extends Worker {


    public RootsFinderWorker(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("RestrictedApi")
    @NonNull
    @NotNull
    @Override
    public Result doWork() {
        // todo: save the calculation after several min (10?) and store it so i can relaunch it on resume (and return Retry?)
        Instant start = Instant.now();
        Long number = getInputData().getLong("input number",1L);
        Long calcIteration = getInputData().getLong("i",2L);

        String output = "number is prime";
        if (number==0 || number==1 || number==2)
        {
            return new Result.Success(new Data.Builder().putString("output",output).build());
        }
        while(calcIteration<number)
        {
            if (number%calcIteration==0)
            {
                output = String.valueOf(number/calcIteration) + "x" + String.valueOf(calcIteration);
                return new Result.Success(new Data.Builder().putString("output",output).build());
            }
            if (Duration.between(start,Instant.now()).toMinutes()>=10)
                // todo: save i to a sp
                return new Result.Failure(); //todo: send i with the Failure, delete corresponding finder and start a new worker

            Long progress = (100*calcIteration)/number;
            calcIteration++;
            setProgressAsync(new Data.Builder().putLong("progress",progress).build());

        }

        return new Result.Success(new Data.Builder().putString("output",output).build());    }
}
