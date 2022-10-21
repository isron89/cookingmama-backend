package com.cookingmama.cookingmamabackend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cookingmama.cookingmamabackend.models.User;
import com.cookingmama.cookingmamabackend.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.cookingmama.cookingmamabackend.models.Recipe;
import com.cookingmama.cookingmamabackend.repository.RecipeRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    @Autowired
    RecipeRepository recipeRepository;
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    UserRepository userRepository;

    //Default
    @GetMapping({"","/","/index"})
    public JsonNode index() throws JsonParseException, IOException {
        String indexString = "{\"Message\":\"Welcome to Cooking Mama Backend Service\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode welcome = mapper.readTree(indexString);
        return welcome;
    }

    //AllRecipeByAdmin
    @GetMapping("/recipes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRecipes() {
        try {
            List<Recipe> recipes = new ArrayList<Recipe>();
            recipeRepository.findAll().forEach(recipes::add);
            if (recipes.isEmpty()) {
                String indexString = "{\"Message\":\"There is no recipe yet\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode noRecipe = mapper.readTree(indexString);
                return new ResponseEntity<>(noRecipe, HttpStatus.OK);
            }
            return new ResponseEntity<>(recipes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GetAllPublic -> USE WHEN HAVEN'T SIGN IN
    @GetMapping("/public")
    public ResponseEntity<?> getPublicRecipes() {
        try {
            List<Recipe> recipesPublic = new ArrayList<Recipe>();
            recipeRepository.findByPublicAccessTrue().forEach(recipesPublic::add);
            if (recipesPublic.isEmpty()) {
                String indexString = "{\"Message\":\"There is no public recipe\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode noRecipe = mapper.readTree(indexString);
                return new ResponseEntity<>(noRecipe, HttpStatus.OK);
            }
            return new ResponseEntity<>(recipesPublic, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GetAllPublicRecipe and UserPrivateRecipe
    @GetMapping("/all")
    public ResponseEntity<?> getPublicRecipes(@RequestParam(name = "userId") String userIdStr) {
        Long userId = Long.parseLong(userIdStr);
        User user = userRepository.findById(userId).orElseThrow();

        try {
            List<Recipe> recipesPublic = new ArrayList<Recipe>();
            recipeRepository.findByPublicAccessTrue().forEach(recipesPublic::add);
            recipeRepository.findByPublicAccessFalseAndUser(user).forEach(recipesPublic::add);
            if (recipesPublic.isEmpty()) {
                String indexString = "{\"Message\":\"There is no public recipe\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode noRecipe = mapper.readTree(indexString);
                return new ResponseEntity<>(noRecipe, HttpStatus.OK);
            }
            return new ResponseEntity<>(recipesPublic, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //NOT USED
    @GetMapping("/private")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getPrivateRecipes() {
        try {
            List<Recipe> recipesPrivate = new ArrayList<Recipe>();
            recipeRepository.findByPublicAccessFalse().forEach(recipesPrivate::add);
            if (recipesPrivate.isEmpty()) {
                String indexString = "{\"Message\":\"You have no private recipe\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode noRecipe = mapper.readTree(indexString);
                return new ResponseEntity<>(noRecipe, HttpStatus.OK);
            }
            return new ResponseEntity<>(recipesPrivate, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GetRecipeByUserId (RESEPKU)
    @GetMapping("/myrecipes/{userid}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyRecipes(@PathVariable("userid") Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Recipe> myRecipes = recipeRepository.findByUser(user);
        if (myRecipes.isEmpty()){
            try {
                String indexString = "{\"Message\":\"You have not created a recipe yet\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode notFound = mapper.readTree(indexString);
                return new ResponseEntity<>(notFound, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(myRecipes, HttpStatus.OK);
        }
    }

    //GetRecipeByRecipeId
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getProductById(@PathVariable("id") long id) {
        try {
            Optional<Recipe> recipesData = recipeRepository.findById(id);
            if (recipesData.isEmpty()) {
                String indexString = "{\"Message\":\"no recipe with this id\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode wrongId = mapper.readTree(indexString);
                return new ResponseEntity(wrongId, HttpStatus.OK);
            } else {
                return recipesData.isPresent() ? new ResponseEntity<>(recipesData.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //PostNewRecipe
    @PostMapping(value = "/save")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> postRecipe(@RequestBody Recipe recipeModel, @RequestParam(name = "userId") Long userId){
        System.out.println(recipeModel.getRecipeName());
        User user = userRepository.findById(userId).orElseThrow();
            //cek name kosong
        if(recipeModel.getRecipeName() == null || recipeModel.getRecipeName().isEmpty()) {
            return new ResponseEntity<>("Recipe name can't be empty", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //cek ingredients kosong
        else if (recipeModel.getIngredients() == null || recipeModel.getIngredients().isEmpty()) {
            return new ResponseEntity<>("Ingredients can't be empty", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //cek How to Cook kosong
        else if (recipeModel.getDirections() == null || recipeModel.getDirections().isEmpty()) {
            return new ResponseEntity<>("Steps can't be empty", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            try{
                recipeModel.setUser(user);
                recipeRepository.save(recipeModel);
                return new ResponseEntity<>("Success", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    //DeleteByRecipeId MUST GIVE USER ID AS PARAMATER
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id")long id, @RequestParam(name = "userId") Long userId){
        Recipe recipe = recipeRepository.findById(id).orElseThrow();

        User user = userRepository.findById(userId).orElseThrow();

        if(user != recipe.getUser()){
            return new ResponseEntity<>("User Not Allowed To Delete", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        recipeRepository.deleteById(id);

        return new ResponseEntity<>("Recipe has been deleted!", HttpStatus.OK);
    }

    //UpdateRecipeByRecipeId MUST GIVE USER ID AS PARAMETER
    @PutMapping ("/update/{id}")
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateRecipe(@PathVariable("id")long id, @RequestBody Recipe recipe, @RequestParam(name = "userId") Long userId){
        Optional<Recipe> recipeData = recipeRepository.findById(id);
        System.out.println(recipeData);

        User user = userRepository.findById(userId).orElseThrow();

        if(user != recipeData.get().getUser()){
            return new ResponseEntity<>("User Not Allowed To Edit", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(recipe.getRecipeName() == null || recipe.getRecipeName().isEmpty()) {
            return new ResponseEntity<>("Recipe name can't be empty", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //cek ingredients kosong
        else if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            return new ResponseEntity<>("Ingredients can't be empty", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //cek How to Cook kosong
        else if (recipe.getDirections() == null || recipe.getDirections().isEmpty()) {
            return new ResponseEntity<>("Steps can't be empty", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            if (recipeData.isPresent()) {
                Recipe newRecipe = recipeData.get();
                newRecipe.setDirections(recipe.getDirections());
                newRecipe.setIngredients(recipe.getIngredients());
                newRecipe.setRecipeName(recipe.getRecipeName());
                newRecipe.setPublicAccess(recipe.getPublicAccess());
                return new ResponseEntity<>(recipeRepository.save(newRecipe), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    //SearchInMainMenu -> USE WHEN USER HAVEN'T SIGN IN
    @GetMapping("/search/public")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> searchProducts(@RequestParam("query") String query){
        List<Recipe> search = recipeRepository.findByRecipeNameAndPublicAccessTrue(query);
        if (search.isEmpty()){
            try {
                String indexString = "{\"Message\":\"Not Found\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode notFound = mapper.readTree(indexString);
                return new ResponseEntity<>(notFound, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(search, HttpStatus.OK);
        }
    }

    //SearchInMainMenu -> USE WHEN USER HAVE SIGN IN
    @GetMapping("/search")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> searchAllProducts(@RequestParam("query") String query, @RequestParam(name = "userId") Long userId){
        List<Recipe> search = recipeRepository.findByRecipeNameAndPublicAccessTrue(query);

        User user = userRepository.findById(userId).orElseThrow();
        recipeRepository.findByRecipeNameAndPublicAccessFalseAndUser(user).forEach(search::add);
        if (search.isEmpty()){
            try {
                String indexString = "{\"Message\":\"Not Found\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode notFound = mapper.readTree(indexString);
                return new ResponseEntity<>(notFound, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(search, HttpStatus.OK);
        }
    }

}
