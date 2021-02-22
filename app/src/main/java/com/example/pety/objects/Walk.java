package com.example.pety.objects;

import java.util.HashMap;
import java.util.Map;

public class Walk {
    private String walk_id;
    private String time;
    private boolean isActive;

    public Walk() {
    }

    public Walk(String time, boolean isActive) {
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

    public String getWalk_id() {
        return walk_id;
    }

    public void setWalk_id(String walk_id) {
        this.walk_id = walk_id;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("walk_id",walk_id);
        result.put("time", time);
        result.put("isActive", isActive);
        return result;
    }

    @Override
    public String toString() {
        return "Walk{" +
                "walk_id='" + walk_id + '\'' +
                ", time='" + time + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
