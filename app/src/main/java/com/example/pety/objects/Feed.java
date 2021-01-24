package com.example.pety.objects;

public class Feed {
    private String time;
    private boolean isActive;

    public Feed() {
    }

    public Feed(String time, boolean isActive) {
        this.time = time;
        this.isActive = isActive;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
