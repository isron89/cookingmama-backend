package com.cookingmama.cookingmamabackend.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cookingmama.cookingmamabackend.model.RecipeModel;
import com.cookingmama.cookingmamabackend.repository.RecipeRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RecipeController {

    @Autowired
    RecipeRepository RecipeRepository;

    @GetMapping({"","/","/index"})
    public JsonNode index() throws JsonParseException, IOException {
        String indexString = "{\"pesan\":\"Welcome to Cooking Mama Backend Service\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode welcome = mapper.readTree(indexString);
        return welcome;
    }

    @GetMapping("/resep")
    public ResponseEntity<List<RecipeModel>> getAllRecipes() {
        try {
//            List<RecipeModel> recipes= RecipeRepository.findAll();
            List<RecipeModel> recipes = new ArrayList<RecipeModel>();
            RecipeRepository.findAll().forEach(recipes::add);
            if (recipes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(recipes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/resep/{id}")
    public ResponseEntity<RecipeModel> getProductById(@PathVariable("id") long id) {
        try {
            Optional<RecipeModel> recipesData = RecipeRepository.findById(id);

            return recipesData.isPresent() ? new ResponseEntity<>(recipesData.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            if (recipesData.isPresent() && recipesData.get().getId() == id) {
//                return new ResponseEntity<>(recipesData.get(), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
