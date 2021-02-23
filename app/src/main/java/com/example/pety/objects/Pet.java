package com.example.pety.objects;

import java.util.HashMap;
import java.util.Map;

public class Pet {
    private String pet_id;
    private String name;
    private String birthday;
    private String pet_type;
    private String image_url;
    private Map<String,Walk> walks = new HashMap<>();
    private Map<String,Feed> feeds = new HashMap<>();
    private Map<String, Health> health = new HashMap<>();
    private Map<String,Beauty> beauty = new HashMap<>();

    public Pet() {
    }

    public Pet(String pet_id, String name, String birthday, String pet_type, String image_url, Map<String,Walk> walks, Map<String,Feed> feeds, Map<String, Health> health, Map<String,Beauty> beauty) {
        this.pet_id = pet_id;
        this.name = name;
        this.birthday = birthday;
        this.pet_type = pet_type;
        this.image_url = image_url;
        this.walks = walks;
        this.feeds = feeds;
        this.health = health;
        this.beauty = beauty;
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

    public Map<String,Walk> getWalks() {
        return walks;
    }

    public void setWalks(Map<String,Walk> walks) {
        this.walks = walks;
    }

    public Map<String,Feed>  getFeeds() {
        return feeds;
    }

    public void setFeeds(Map<String,Feed> feeds) {
        this.feeds = feeds;
    }

    public Map<String, Health> getHealth() {
        return health;
    }

    public void setHealth(Map<String, Health>  health) {
        this.health = health;
    }

    public Map<String,Beauty> getBeauty() {
        return beauty;
    }

    public void setBeauty(Map<String,Beauty> beauty) {
        this.beauty = beauty;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "pet_id='" + pet_id + '\'' +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", pet_type='" + pet_type + '\'' +
                ", image_url='" + image_url + '\'' +
                ", walks=" + walks +
                ", feeds=" + feeds +
                ", health=" + health +
                ", beauty=" + beauty +
                '}';
    }
}
