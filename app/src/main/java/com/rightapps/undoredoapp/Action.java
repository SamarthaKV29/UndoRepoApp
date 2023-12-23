package com.rightapps.undoredoapp;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

public class Action {
    private final ActionType type;

    public ActionType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String text;

    public Action(ActionType type, String text) {
        this.type = type;
        this.text = text;
    }

    public Action(ActionType type) {
        this.type = type;
        this.text = "";
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        String actionType = "";
        switch (type) {
            case KEYBOARD_ACTION:
                actionType = "Keyboard";
                break;
            case MOUSE_ACTION:
                actionType = "Mouse";
                break;
        }
        return String.format("type: %s, text: %s", actionType, text);
    }
}

enum ActionType {
    KEYBOARD_ACTION,
    MOUSE_ACTION
}
