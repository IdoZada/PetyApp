package com.example.pety.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String f_name;
    private String l_name;
    private String phone_number;
    private String image_url;
    private List<String> families = new ArrayList<>(); //Each user contains a map for all families

    public User() {
    }

    public User( String f_name, String l_name, String phone_number, String image_url) {
        this.f_name = f_name;
        this.l_name = l_name;
        this.phone_number = phone_number;
        this.image_url = image_url;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstName", f_name);
        result.put("lastName", l_name);
        result.put("phoneNumber", phone_number);
        result.put("imageUrl", image_url);
        result.put("families", families);
        return result;
    }

    public List<String> getFamilies_keys() {
        return families;
    }

    public void setFamilies_keys(List<String> families) {
        this.families = families;
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

    @Override
    public String toString() {
        return "User{" +
                "f_name='" + f_name + '\'' +
                ", l_name='" + l_name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", image_url='" + image_url + '\'' +
                ", families=" + families +
                '}';
    }
}
