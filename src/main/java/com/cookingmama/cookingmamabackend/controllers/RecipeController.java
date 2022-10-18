package com.cookingmama.cookingmamabackend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
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
//            List<RecipeModel> recipes= RecipeRepository.findAll();
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
//            List<RecipeModel> recipes= RecipeRepository.findAll();
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

    @GetMapping("/private")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getPrivateRecipes() {
        try {
//            List<RecipeModel> recipes= RecipeRepository.findAll();
            List<RecipeModel> recipesPrivate = new ArrayList<RecipeModel>();
            RecipeRepository.findByPublikFalse().forEach(recipesPrivate::add);
            if (recipesPrivate.isEmpty()) {
                String indexString = "{\"Message\":\"You have not created a recipe\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode noRecipe = mapper.readTree(indexString);
                return new ResponseEntity<>(noRecipe, HttpStatus.OK);
            }
            return new ResponseEntity<>(recipesPrivate, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
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

//    @GetMapping("/save")
//    private String getOrder(@RequestParam(value = "productId", defaultValue = "Kosong") String id,
//                            @RequestParam(value = "buyerNama") String buyerNama,
//                            @RequestParam(value = "address") String address){
////        System.out.println(orderItem);
//        if(id.equals("Kosong")) {
//            return "Product ID tidak boleh kosong";
//        }
//        String url = "http://localhost:8080/api/products/" + id;
//        String result = restTemplate.getForObject(url, String.class);
//        RecipeModel recipeModel = restTemplate.getForObject(url, RecipeModel.class);
//        System.out.println("Order success: " + result);
//
//        if (result.isEmpty()) {
//            System.out.println("Gagal menyimpan karena data kosong");
//        } else {
//            orderModel orderModel = new orderModel();
//            orderModel.setOrderid(formatter.format(today) + buyerNama + product.getId());
//            orderModel.setNama(buyerNama);
//            orderModel.setOrderdate(formatter.format(today));
//            orderModel.setAddress(address);
//            orderModel.setProductitem(product.getNama());
//            orderRepository.save(orderModel);
//        }
//        return result;
//    }

    @PostMapping(value = "/save")
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id")long id){
        RecipeRepository.deleteById(id);
        return new ResponseEntity<>("Recipe has been deleted!", HttpStatus.OK);
    }

    //update resep by id

    @PostMapping ("/update/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    //@RequestMapping(value = "/resep/update/{id}", method = RequestMethod.POST)
    public ResponseEntity<RecipeModel> updateRecipe(@PathVariable("id")long id, @RequestBody RecipeModel recipeModel){
        Optional<RecipeModel> recipeData = RecipeRepository.findById(id);
        System.out.println(recipeData);

        if (recipeData.isPresent()) {
            RecipeModel _recipeModel = recipeData.get();
            //_recipeModel.setId(recipeModel.getId());
            _recipeModel.setHowto(recipeModel.getHowto());
            _recipeModel.setIngredients(recipeModel.getIngredients());
            _recipeModel.setName(recipeModel.getName());
            _recipeModel.setPublik(recipeModel.getPublik());
            //_recipeModel.setUserid(recipeModel.getUserid());
            return new ResponseEntity<>(RecipeRepository.save(_recipeModel), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> searchProducts(@RequestParam("query") String query){
        List<RecipeModel> search = RecipeRepository.findByName(query);
        if (search.isEmpty()){
            return new ResponseEntity<>("Not Found", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(search, HttpStatus.OK);
        }
    }
}
