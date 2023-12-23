package com.rightapps.undoredoapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.PriorityQueue;

import timber.log.Timber;

public class PersonQueueActivity extends Activity {

    private static final int MIN_DELAY = 1000;
    final PriorityQueue<Person> personQueue = new PriorityQueue<>();
    LinearLayoutCompat personQueueView = null;
    private int persons = 1;
    private Thread addPersonThread;
    private Thread removePersonThread;
    private ToggleButton automateToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
        personQueueView = findViewById(R.id.personQueue);
        automateToggle = findViewById(R.id.automateToggle);


        automateToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                addPersonThread = new Thread(automatedAddRunnable);
                addPersonThread.start();

                removePersonThread = new Thread(automatedRemoveRunnable);
                removePersonThread.start();
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
        synchronized (personQueue) {
            if (personQueue.size() >= 5) {
                personQueue.remove();
            }
            personQueue.add(new Person(persons++));
            updatePersonQueueView();
        }
    }

    public void onRemoveClick(View view) {
        synchronized (personQueue) {
            if (personQueue.size() > 0) {
                personQueue.remove();
                if (personQueue.isEmpty()) persons = 1;
            }
            updatePersonQueueView();
        }
    }

    @Override
    protected void onDestroy() {
        if (automateToggle != null) automateToggle.setChecked(false);
        super.onDestroy();
    }

    Runnable addRunnable = () -> onAddClick(null);

    Runnable removeRunnable = () -> onRemoveClick(null);

    Runnable automatedAddRunnable = () -> {
        while (isAutomating()) {
            int d = (int) (Math.random() * MIN_DELAY) + MIN_DELAY;
            runOnUiThread(addRunnable);
            try {
                Thread.sleep(d);
            } catch (InterruptedException e) {
                Timber.e(e, "Thread interrupted");
            }
        }
    };

    Runnable automatedRemoveRunnable = () -> {
        while (isAutomating()) {
            int d = (int) (Math.random() * MIN_DELAY * 2) + MIN_DELAY;
            runOnUiThread(removeRunnable);
            try {
                Thread.sleep(d);
            } catch (InterruptedException e) {
                Timber.e(e, "Thread interrupted");
            }
        }
    };

    private boolean isAutomating() {
        boolean automating = automateToggle != null && automateToggle.isChecked();
        Timber.d("Automating %s", automating);
        return automating;
    }
}
