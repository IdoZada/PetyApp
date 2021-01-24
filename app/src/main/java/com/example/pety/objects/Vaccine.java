package com.example.pety.objects;

public class Vaccine {
    private String time;
    private boolean isActive;

    public Vaccine() {
    }

    public Vaccine(String time, boolean isActive) {
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
