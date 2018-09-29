package com.kshitij.cloudprint.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "pets")
public class Pet {
    @Id
    String id;
    String name;
    String species;
    String breed;

    public Pet(String name, String species, String breed) {
        this.name = name;
        this.species = species;
        this.breed = breed;
    }
}
