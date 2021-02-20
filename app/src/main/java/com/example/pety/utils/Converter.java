package com.example.pety.utils;

import com.example.pety.objects.Pet;

import java.util.Map;

public class Converter {

    public static Object fromMap(Map<String, Object> result){
        Pet pet = new Pet();
        Object pet_id = result.get("pet_id");
        Object name = result.get("name");
        Object image_url = result.get("image_url");
        Object pet_type = result.get("pet_type");
        Object birthday = result.get("birthday");

        pet.setPet_id(pet_id.toString());
        pet.setName(name.toString());
        pet.setImage_url(image_url.toString());
        pet.setPet_type(pet_type.toString());
        pet.setBirthday(birthday.toString());

        return pet;
    }
}
