package com.rightapps.undoredoapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.PriorityQueue;

import timber.log.Timber;

public class PersonQueueActivity extends Activity {

    PriorityQueue<Person> personQueue = new PriorityQueue<>();
    LinearLayoutCompat personQueueView = null;
    private int persons = 1;
    private Handler addHandler;
    private Handler removeHandler;
    private ToggleButton automateToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        personQueueView = findViewById(R.id.personQueue);
        automateToggle = findViewById(R.id.automateToggle);

        HandlerThread addHandlerThread = new HandlerThread("AddHandlerThread");
        HandlerThread removeHandlerThread = new HandlerThread("RemoveHandlerThread");
        addHandlerThread.start();
        removeHandlerThread.start();

        addHandler = new Handler(addHandlerThread.getLooper());
        removeHandler = new Handler(removeHandlerThread.getLooper());
    }

    @Override
    protected void onStart() {
        super.onStart();
        automateToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                addHandler.post(automatedAddRunnable);
                removeHandler.post(automatedRemoveRunnable);
            }
        });
    }


    private void updatePersonQueueView() {
        personQueueView.removeAllViews();
        for (Person p : personQueue) {
            TextView t = new TextView(getApplicationContext());
            t.setText(p.toString());
            personQueueView.addView(t);
        }
    }

    public void onAddClick(View view) {
        if (personQueue.size() >= 5) {
            personQueue.remove();
        }
        personQueue.add(new Person(persons++));
        updatePersonQueueView();
    }

    public void onRemoveClick(View view) {
        if (personQueue.size() > 0) personQueue.remove();
        updatePersonQueueView();
    }


    Runnable addRunnable = () -> onAddClick(null);

    Runnable removeRunnable = () -> onRemoveClick(null);

    Runnable automatedAddRunnable = () -> {
        boolean isAutomating = true;
        while (isAutomating) {
            int d = (int) (Math.random() * 500) + 300;
            runOnUiThread(addRunnable);
            try {
                Thread.sleep(d);
            } catch (InterruptedException e) {
                Timber.e(e, "Thread interrupted");
            }
            isAutomating = isAutomating();
        }
    };

    Runnable automatedRemoveRunnable = () -> {
        boolean isAutomating = true;
        while (isAutomating) {
            int d = (int) (Math.random() * 2000) + 600;
            runOnUiThread(removeRunnable);
            try {
                Thread.sleep(d);
            } catch (InterruptedException e) {
                Timber.e(e, "Thread interrupted");
            }
            isAutomating = isAutomating();
        }
    };

    private boolean isAutomating() {
        return automateToggle != null && automateToggle.isChecked();
    }
}
