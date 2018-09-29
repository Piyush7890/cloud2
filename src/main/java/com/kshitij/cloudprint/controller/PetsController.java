package com.kshitij.cloudprint.controller;

import com.kshitij.cloudprint.model.Pet;
import com.kshitij.cloudprint.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PetsController {
    @Autowired
    private PetService service;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Pet create(@RequestBody Pet pet) {
        return service.create(pet);
    }

    @RequestMapping(value = "/pets", method = RequestMethod.GET)
    public List<Pet> getAll() {
        return service.getAll();
    }
}
