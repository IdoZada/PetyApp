package com.example.pety.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Pet {
    private String pet_id;
    private String name;
    private String birthday;
    private String pet_type;
    private String image_url;
    private ArrayList<Walk> walks;
    private ArrayList<Feed> feeds;
    private ArrayList<Vaccine> vaccines;
    private ArrayList<Beauty> beauties;

    public Pet() {
    }

    public Pet(String pet_id, String name, String birthday, String pet_type, String image_url, ArrayList<Walk> walks, ArrayList<Feed> feeds, ArrayList<Vaccine> vaccines, ArrayList<Beauty> beauties) {
        this.pet_id = pet_id;
        this.name = name;
        this.birthday = birthday;
        this.pet_type = pet_type;
        this.image_url = image_url;
        this.walks = walks;
        this.feeds = feeds;
        this.vaccines = vaccines;
        this.beauties = beauties;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("pet_id",pet_id);
        result.put("name", name);
        result.put("image_url", image_url);
        result.put("pet_type", pet_type);
        result.put("birthday", birthday);
        return result;
    }


    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPet_type() {
        return pet_type;
    }

    public void setPet_type(String pet_type) {
        this.pet_type = pet_type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public ArrayList<Walk> getWalks() {
        return walks;
    }

    public void setWalks(ArrayList<Walk> walks) {
        this.walks = walks;
    }

    public ArrayList<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(ArrayList<Feed> feeds) {
        this.feeds = feeds;
    }

    public ArrayList<Vaccine> getVaccines() {
        return vaccines;
    }

    public void setVaccines(ArrayList<Vaccine> vaccines) {
        this.vaccines = vaccines;
    }

    public ArrayList<Beauty> getBeauties() {
        return beauties;
    }

    public void setBeauties(ArrayList<Beauty> beauties) {
        this.beauties = beauties;
    }
}
