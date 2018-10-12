package com.kshitij.cloudprint.controller;

import com.kshitij.cloudprint.configuration.JwtTokenUtil;
import com.kshitij.cloudprint.model.Pet;
import com.kshitij.cloudprint.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class PetsController {
    @Autowired
    private PetService service;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Pet create(@RequestBody Pet pet) {
        return service.create(pet);
    }

    @RequestMapping(value = "/pets", method = RequestMethod.GET)
    public List<Pet> getAll(@RequestHeader("Authorization") String auth) {
        if (auth.startsWith("Bearer ")) {
            auth = auth.replace("Bearer ", "");
            if (jwtTokenUtil.validateToken(auth))
                return service.getAll();
        }
        return Collections.emptyList();
    }
}
