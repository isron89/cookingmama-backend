package com.cookingmama.cookingmamabackend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.cookingmama.cookingmamabackend.models.RecipeModel;
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
    RecipeRepository RecipeRepository;
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping({"","/","/index"})
    public JsonNode index() throws JsonParseException, IOException {
        String indexString = "{\"Message\":\"Welcome to Cooking Mama Backend Service\"}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode welcome = mapper.readTree(indexString);
        return welcome;
    }

    @GetMapping("/recipes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRecipes() {
        try {
            List<RecipeModel> recipes = new ArrayList<RecipeModel>();
            RecipeRepository.findAll().forEach(recipes::add);
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

    @GetMapping("/public")
    public ResponseEntity<?> getPublicRecipes() {
        try {
            List<RecipeModel> recipesPublic = new ArrayList<RecipeModel>();
            RecipeRepository.findByPublikTrue().forEach(recipesPublic::add);
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

    @GetMapping("/pagepublic")
    public List<RecipeModel> getPagePublicRecipes(@RequestParam(defaultValue = "0") Integer pageNo) {
        //create pagerequest object
        PageRequest pageRequest = PageRequest.of(pageNo, 6);
        //pass it to repos
        Page<RecipeModel> pagingRecipe = RecipeRepository.findAll(pageRequest);
        //pagingUser.hasContent(); -- to check pages are there or not
        return pagingRecipe.getContent();
//        return userService.getUsersByPagination(pageNo,pageSize);

//        try {
//            List<RecipeModel> recipesPage = new ArrayList<RecipeModel>();
//            RecipeRepository.findByPublikTrue().forEach(recipesPage::add);
//            if (recipesPage.isEmpty()) {
//                String indexString = "{\"Message\":\"There is no public recipe\"}";
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode noRecipe = mapper.readTree(indexString);
//                return new ResponseEntity<>(noRecipe, HttpStatus.OK);
//            }
//            return new ResponseEntity<>(recipesPage, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

    @GetMapping("/private")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getPrivateRecipes() {
        try {
            List<RecipeModel> recipesPrivate = new ArrayList<RecipeModel>();
            RecipeRepository.findByPublikFalse().forEach(recipesPrivate::add);
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

    @GetMapping("/myrecipes/{userid}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyRecipes(@PathVariable("userid") String userid) {
        List<RecipeModel> myRecipes = RecipeRepository.findByUserid(userid);
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

    @GetMapping("/{id}")
    public ResponseEntity<RecipeModel> getProductById(@PathVariable("id") long id) {
        try {
            Optional<RecipeModel> recipesData = RecipeRepository.findById(id);
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

    @PostMapping(value = "/save")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> postRecipe(@RequestBody RecipeModel recipeModel){
        System.out.println(recipeModel.getName());
            //cek name kosong
        if(recipeModel.getName() == null || recipeModel.getName().isEmpty()) {
            return new ResponseEntity<>("Recipe name can't be empty", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //cek ingredients kosong
        else if (recipeModel.getIngredients() == null || recipeModel.getIngredients().isEmpty()) {
            return new ResponseEntity<>("Ingredients can't be empty", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //cek How to Cook kosong
        else if (recipeModel.getHowto() == null || recipeModel.getHowto().isEmpty()) {
            return new ResponseEntity<>("Steps can't be empty", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            try{
                RecipeModel recipe = RecipeRepository.save(new RecipeModel(recipeModel.getName(), recipeModel.getIngredients(), recipeModel.getHowto(), recipeModel.getPublik(), recipeModel.getUserid()));
                return new ResponseEntity<>(null, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    //delete resep by id
    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id")long id){
        RecipeRepository.deleteById(id);
        return new ResponseEntity<>("Recipe has been deleted!", HttpStatus.OK);
    }

    //update resep by id

    @PostMapping ("/update/{id}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<RecipeModel> updateRecipe(@PathVariable("id")long id, @RequestBody RecipeModel recipeModel){
        Optional<RecipeModel> recipeData = RecipeRepository.findById(id);
        System.out.println(recipeData);

        if (recipeData.isPresent()) {
            RecipeModel _recipeModel = recipeData.get();
            _recipeModel.setHowto(recipeModel.getHowto());
            _recipeModel.setIngredients(recipeModel.getIngredients());
            _recipeModel.setName(recipeModel.getName());
            _recipeModel.setPublik(recipeModel.getPublik());
            return new ResponseEntity<>(RecipeRepository.save(_recipeModel), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> searchRecipe(@RequestParam("query") String query){
        List<RecipeModel> search = RecipeRepository.findByNameContainingAndPublikTrue(query);
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

    @GetMapping("/searchMy")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> searchMyRecipe(@RequestParam("query") String query, @RequestParam("userid") String userid){
        List<RecipeModel> searchMy = RecipeRepository.searchMyRecipe(userid, query);
//        System.out.println(query);
        if (searchMy.isEmpty()){
            try {
                String indexString = "{\"Message\":\"Not Found\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode notFound = mapper.readTree(indexString);
                return new ResponseEntity<>(notFound, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(searchMy, HttpStatus.OK);
        }
    }

}
