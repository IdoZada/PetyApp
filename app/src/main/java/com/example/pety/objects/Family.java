package com.example.pety.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Family {
    public static final String FAMILY_ID = "f_";
    private String family_id;
    private String f_name;
    private String imageURL;
    private Map<String,Pet> pets = new HashMap<>();

    public Family() {
    }

    public Family(String imageURL, UUID uuid, String f_name) {
        this.family_id = FAMILY_ID + uuid.toString();
        this.f_name = f_name;
        this.imageURL = imageURL;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("family_id", family_id);
        result.put("f_name", f_name);
        result.put("imageURL", imageURL);
        result.put("pets", pets);
        return result;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
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
