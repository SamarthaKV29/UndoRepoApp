package com.rightapps.undoredoapp;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Person implements Comparable<Person> {
    int position;

    Person(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US, "Person %d", position);
    }

    @Override
    public int compareTo(Person p) {
        return position - p.position;
    }
}
