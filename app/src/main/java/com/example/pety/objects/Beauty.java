package com.example.pety.objects;

import java.util.HashMap;
import java.util.Map;

public class Beauty {
    private String id;
    private String time_date;
    private boolean isActive;

    public Beauty() {
    }

    public Beauty(String time_date, boolean isActive) {
        this.time_date = time_date;
        this.isActive = isActive;
    }

    public String getTimeDate() {
        return time_date;
    }

    public void setTimeDate(String time_date) {
        this.time_date = time_date;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id",id);
        result.put("time_date", time_date);
        result.put("isActive", isActive);
        return result;
    }
}
