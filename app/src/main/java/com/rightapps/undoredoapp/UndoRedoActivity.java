package com.rightapps.undoredoapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.Collections;
import java.util.Stack;

public class UndoRedoActivity extends Activity {
    Stack<Action> undoStack = new Stack<>();
    Stack<Action> redoStack = new Stack<>();

    LinearLayoutCompat undoStackView = null;
    LinearLayoutCompat redoStackView = null;

    boolean isUndoOrRedoInProgress = false;

    EditText userTextInput = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undo_redo);
        userTextInput = findViewById(R.id.userTextInput);
        undoStackView = findViewById(R.id.undoStack);
        redoStackView = findViewById(R.id.redoStack);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (userTextInput != null) {
            userTextInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
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
