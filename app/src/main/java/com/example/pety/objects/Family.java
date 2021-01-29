package com.example.pety.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Family {
    public static final String FAMILY_ID = "f_";
    private String family_id;
    private Map<String,Pet> pets;
    private String f_name;
    private int mImage;

    public Family() {
    }

    public Family(int mImage, UUID uuid, Map<String, Pet> pets, String f_name) {
        this.family_id = FAMILY_ID + uuid.toString();
        this.pets = pets;
        this.f_name = f_name;
        this.mImage = mImage;
    }

    public int getmImage() {
        return mImage;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
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
