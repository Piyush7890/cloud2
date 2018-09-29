package com.kshitij.cloudprint.repository;

import com.kshitij.cloudprint.model.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetsRepository extends MongoRepository<Pet, String> {
    Pet findByName(String name);
}