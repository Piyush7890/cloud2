package com.kshitij.cloudprint.service;

import com.kshitij.cloudprint.model.Pet;
import com.kshitij.cloudprint.repository.PetsRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {
    @Autowired
    private PetsRepository petsRepository;

    // Create operation
    public Pet create(Pet pet) {
        pet.setId(ObjectId.get().toString());
        return petsRepository.save(pet);
    }

    //Retrieve operation
    public List<Pet> getAll() {
        return petsRepository.findAll();
    }

    public Pet getByName(String name) {
        return petsRepository.findByName(name);
    }

    // Delete operation
    public void deleteAll() {
        petsRepository.deleteAll();
    }

    public void delete(String name) {
        Pet p = petsRepository.findByName(name);
        petsRepository.delete(p);
    }
}
