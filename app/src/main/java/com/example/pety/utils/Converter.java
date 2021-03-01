package com.example.pety.utils;

import com.example.pety.objects.Beauty;
import com.example.pety.objects.Feed;
import com.example.pety.objects.Pet;
import com.example.pety.objects.Health;
import com.example.pety.objects.Walk;

import java.util.HashMap;
import java.util.Map;

public class Converter {

    public static Object fromMap(Map<String, Object> result){
        Pet pet = new Pet();
        Map<String,Walk> walks = new HashMap<>();
        Map<String,Feed> feeds = new HashMap<>();
        Map<String,Beauty> beauty = new HashMap<>();
        Map<String, Health> health = new HashMap<>();

        Object pet_id = result.get("pet_id");
        Object name = result.get("name");
        Object image_url = result.get("image_url");
        Object pet_type = result.get("pet_type");
        Object birthday = result.get("birthday");

        Map<String,Walk> walks_map = (Map<String, Walk>) result.get("walks");
        Map<String,Feed> feeds_map = (Map<String, Feed>) result.get("feeds");
        Map<String,Beauty> beauty_map = (Map<String, Beauty>) result.get("beauty");
        Map<String,Health> health_map = (Map<String, Health>) result.get("health");

        if(walks_map != null){
            for (Map.Entry<String,Walk> entry : walks_map.entrySet()){
                Walk walk = new Walk();
                walk = (Walk) Converter.fromMapCares((Map<String, Object>) entry.getValue(),walk);
                walks.put(entry.getKey(),walk);
            }
        }

        if(feeds_map !=null){
            for (Map.Entry<String,Feed> entry : feeds_map.entrySet()){
                Feed feed = new Feed();
                feed = (Feed) Converter.fromMapCares((Map<String, Object>) entry.getValue(),feed);
                feeds.put(entry.getKey(),feed);
            }
        }

        if(beauty_map != null){
            for (Map.Entry<String,Beauty> entry : beauty_map.entrySet()){
                Beauty beauty1 = new Beauty();
                beauty1 = (Beauty) Converter.fromMapCares((Map<String, Object>) entry.getValue(),beauty1);
                beauty.put(entry.getKey(),beauty1);
            }
        }

        if(health_map != null){
            for (Map.Entry<String,Health> entry : health_map.entrySet()){
                Health health1 = new Health();
                health1 = (Health) Converter.fromMapCares((Map<String, Object>) entry.getValue(),health1);
                health.put(entry.getKey(),health1);
            }
        }

        pet.setWalks(walks);
        pet.setFeeds(feeds);
        pet.setBeauty(beauty);
        pet.setHealth(health);
        pet.setPet_id(pet_id.toString());
        pet.setName(name.toString());
        pet.setImage_url(image_url.toString());
        pet.setPet_type(pet_type.toString());
        pet.setBirthday(birthday.toString());

        return pet;
    }

    private static Object fromMapCares(Map<String, Object> value , Object obj) {
        Object id = value.get("id");
        Object time = value.get("time");
        Object time_date = value.get("time_date");
        Object isActive = value.get("isActive");

        if(obj instanceof Walk){
            ((Walk) obj).setId(id.toString());
            ((Walk) obj).setTime(time.toString());
            ((Walk) obj).setActive((Boolean) isActive);
        }else if(obj instanceof Feed){
            ((Feed) obj).setId(id.toString());
            ((Feed) obj).setTime(time.toString());
            ((Feed) obj).setActive((Boolean) isActive);
        }else if(obj instanceof Beauty){
            ((Beauty) obj).setId(id.toString());
            ((Beauty) obj).setTimeDate(time_date.toString());
            ((Beauty) obj).setActive((Boolean) isActive);
        }else {
            ((Health) obj).setId(id.toString());
            ((Health) obj).setTimeDate(time_date.toString());
            ((Health) obj).setActive((Boolean) isActive);
        }

        return obj;
    }

    static Map<String, Pet> convertPets(Map<String, Pet> pets) {
        Map<String,Pet> pets_map = new HashMap<>();

        for (Map.Entry<String,Pet> entry : pets.entrySet()){
            Pet pet = (Pet) Converter.fromMap((Map<String, Object>) entry.getValue());
            pets_map.put(entry.getKey(),pet);
        }

        return pets_map;
    }
}
