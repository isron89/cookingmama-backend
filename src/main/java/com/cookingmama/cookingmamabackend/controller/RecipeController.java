package com.cookingmama.cookingmamabackend.controller;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RecipeController {

    @GetMapping({"","/","/index"})
    public JsonNode index() throws JsonParseException, IOException {
        String indexString = "{\"pesan\":\"Welcome to Cooking Mama Backend Service\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode welcome = mapper.readTree(indexString);
        return welcome;
    }

}
