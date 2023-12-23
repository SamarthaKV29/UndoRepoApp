package com.rightapps.undoredoapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.Collections;
import java.util.Stack;

public class MainActivity extends Activity {
    static final String TAG = "MainActivity";
    Stack<Action> undoStack = new Stack<>();
    Stack<Action> redoStack = new Stack<>();

    LinearLayoutCompat undoStackView = null;
    LinearLayoutCompat redoStackView = null;

    boolean isUndoOrRedoInProgress = false;

    EditText userTextInput = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_main);
        userTextInput = findViewById(R.id.userTextInput);
        undoStackView = findViewById(R.id.undoStack);
        redoStackView = findViewById(R.id.redoStack);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (userTextInput != null) {
            userTextInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.d(TAG, String.format("onTextChanged: %s", s.toString()));
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d(TAG, "onTextChanged: " + s.toString());
                    Action a = new Action(ActionType.KEYBOARD_ACTION, s.toString());
                    if (!isUndoOrRedoInProgress) {
                        if (undoStack.size() > 4) {
                            undoStack.remove(0);
                        }
                        undoStack.push(a);
                        updateUndoStackView();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void updateUndoStackView() {
        undoStackView.removeAllViews();
        for (Action a : undoStack) {
            TextView t = new TextView(getApplicationContext());
            t.setText(a.getText());
            t.setTextColor(getColor(R.color.black));
            undoStackView.addView(t);
        }
    }

    private void updateRedoStackView() {
        redoStackView.removeAllViews();
        for (Action a : redoStack) {
            TextView t = new TextView(getApplicationContext());
            t.setText(a.getText());
            t.setTextColor(getColor(R.color.white));
            redoStackView.addView(t);
        }
    }

    public void onUndoClick(View view) {
        if (!undoStack.empty()) {
            isUndoOrRedoInProgress = true;
            Action oldAction = undoStack.pop();
            //reverse(redoStack);
            redoStack.push(oldAction);

            if (userTextInput != null && !undoStack.empty()) {
                userTextInput.setText(undoStack.peek().getText());
            }
            isUndoOrRedoInProgress = false;
        }

        updateUndoStackView();
        updateRedoStackView();
    }

    private static void reverse(Stack<Action> arr) {
        arr.sort(Collections.reverseOrder());
        System.out.println("After reverse");
        System.out.println(arr);
    }

    public void onRedoClick(View view) {
        if (!redoStack.empty()) {
            isUndoOrRedoInProgress = true;
            Action newAction = redoStack.pop();
            undoStack.push(newAction);

            if (userTextInput != null) {
                userTextInput.setText(newAction.getText());
            }
            isUndoOrRedoInProgress = false;
        }
        updateUndoStackView();
        updateRedoStackView();
    }

}
