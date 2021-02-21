package com.example.pety.utils;

import com.example.pety.objects.Pet;

import java.util.HashMap;
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

    static Map<String, Pet> convertPets(Map<String, Pet> pets) {
        Map<String,Pet> pets_map = new HashMap<>();
        for (Map.Entry<String,Pet> entry : pets.entrySet()){
            Pet pet = (Pet) Converter.fromMap((Map<String, Object>) entry.getValue());
            pets_map.put(entry.getKey(),pet);
        }
        return pets_map;
    }
}
