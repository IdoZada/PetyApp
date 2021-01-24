package com.example.pety.objects;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private String user_id;
    private ArrayList<String> families_keys; //Each user contains a map for all families
    private String f_name;
    private String l_name;
    private String phone_number;
    private String image_url;

    public User() {
    }

    public User(String user_id, ArrayList<String> families_keys, String f_name, String l_name, String phone_number, String image_url) {
        this.user_id = user_id;
        this.families_keys = families_keys;
        this.f_name = f_name;
        this.l_name = l_name;
        this.phone_number = phone_number;
        this.image_url = image_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<String> getFamilies_keys() {
        return families_keys;
    }

    public void setFamilies_keys(ArrayList<String> families_keys) {
        this.families_keys = families_keys;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
