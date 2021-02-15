package com.example.pety.objects;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public class Family {
    private String f_name;
    private String imageUrl;
    private Map<String,Pet> pets = new HashMap<>();

    public Family() {
    }

    public Family(String f_name) {
        this.f_name = f_name;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("f_name", f_name);
        result.put("imageURL", imageUrl);
        result.put("pets", pets);
        return result;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, Pet> getPets() {
        return pets;
    }

    public void setPets(Map<String, Pet> pets) {
        this.pets = pets;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }
}
