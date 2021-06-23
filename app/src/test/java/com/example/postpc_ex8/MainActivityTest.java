package com.example.postpc_ex8;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.constraintlayout.utils.widget.MockView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Ordering;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest extends TestCase {

    @Test
    public void when_addingCalculation_then_newRootFinderIsCreated(){
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();
        findRootsHolder holder = mainActivity.holder;
        FloatingActionButton button = mainActivity.findViewById(R.id.buttonCalculateRoots);
        EditText input = mainActivity.findViewById(R.id.insertNumber);
        input.setText("12");
        assertEquals(holder.size(),0);
        button.performClick();

        assertEquals(holder.size(),1);
        RootsFinder savedFinder = holder.getCurrentFinders().get(0);
        long number = savedFinder.getNumber();
        assertEquals(number,12L);
    }

    @Test
    public void when_addingCalculation_then_workerIsCreated(){
        // create a MainActivity and let it think it's currently displayed on the screen
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();
        findRootsHolder holder = mainActivity.holder;
        WorkManager workManager = mainActivity.workManager;
        FloatingActionButton button = mainActivity.findViewById(R.id.buttonCalculateRoots);
        EditText input = mainActivity.findViewById(R.id.insertNumber);
        input.setText("12");
        button.performClick();

        UUID rootFinderId = holder.getCurrentFinders().get(0).getId();
        assertNotNull(workManager.getWorkInfoById(rootFinderId));
    }

    @Test
    public void when_deletingCalculation_than_rootFinderIsDeletedFromHolder()
    {
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();
        findRootsHolder holder = mainActivity.holder;
        WorkManager workManager = mainActivity.workManager;
        FloatingActionButton button = mainActivity.findViewById(R.id.buttonCalculateRoots);
        EditText input = mainActivity.findViewById(R.id.insertNumber);
        input.setText("35317");
        button.performClick();
        RootsFinder rootsFinder = holder.getCurrentFinders().get(0);
        assertEquals(holder.size(),1);
        holder.deleteFinder(rootsFinder);
        assertEquals(holder.size(),0);

    }

    // to mock a click on the button:
    //    call `button.performClick()`
    //
    // to mock inserting input to the edit-text:
    //    call `editText.setText("input here")`
    //
    // to mock sending a broadcast:
    //    create the broadcast intent (example: `new Intent("my_action_here")` ) and put extras
    //    call `RuntimeEnvironment.application.sendBroadcast()` to send the broadcast
    //    call `Shadows.shadowOf(Looper.getMainLooper()).idle();` to let the android OS time to process the broadcast the let your activity consume it
}
