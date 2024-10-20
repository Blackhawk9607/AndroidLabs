package com.example.androidlabs;

class TodoItem {
    private final String text;
    private final boolean isUrgent;

    public TodoItem(String text, boolean isUrgent) {
        this.text = text;
        this.isUrgent = isUrgent;
    }

    public String getText() {
        return text;
    }

    public boolean isUrgent() {
        return isUrgent;
    }
}
