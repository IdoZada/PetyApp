package com.example.pety.interfaces;

import com.example.pety.enums.Fab;
import com.example.pety.enums.FamilyFlag;
import com.example.pety.objects.Family;
import com.example.pety.objects.Pet;
import com.example.pety.objects.User;

import java.util.ArrayList;

public interface SendDataCallback {
    void sendFamily(Family family , FamilyFlag familyFlag);
    void sendUser(User user);
    void sendPet(Family family, Pet pet, Fab chose_fab);
    void sendFamilies(ArrayList<Family> families);
}
