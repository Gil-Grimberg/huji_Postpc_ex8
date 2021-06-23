package com.example.postpc_ex8;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Comparator;

public class RootsFinderComparator implements Comparator<RootsFinder> {

    public int compare(RootsFinder rootsFinder1, RootsFinder rootsFinder2) {
        if (rootsFinder1.getProgress()!=100L)
        {
            if (rootsFinder2.getProgress()==100L)
            {
                return -1;
            }
            else
            {
                return (int) (rootsFinder1.getNumber()-rootsFinder2.getNumber());
            }
        }
        else
        {
            return 1;
        }
    }
}
