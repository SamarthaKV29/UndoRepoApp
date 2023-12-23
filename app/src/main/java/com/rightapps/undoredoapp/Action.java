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
        return String.format("type: %s, text: %s", getActionType(type), text);
    }

    private String getActionType(ActionType type) {
        switch (type) {
            case KEYBOARD_ACTION:
                return "Keyboard";
            case MOUSE_ACTION:
                return "Mouse";
        }
        return "";
    }
}

enum ActionType {
    KEYBOARD_ACTION,
    MOUSE_ACTION
}
