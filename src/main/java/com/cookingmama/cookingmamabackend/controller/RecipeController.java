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
import org.springframework.web.client.RestTemplate;


@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")



public class RecipeController {

    @Autowired
    RecipeRepository RecipeRepository;

    private RestTemplate restTemplate = new RestTemplate();

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
    public ResponseEntity<String> postRecipe(@RequestBody RecipeModel recipeModel){
        System.out.println(recipeModel.getName());
            //cek name kosong
        if(recipeModel.getName() == null || recipeModel.getName().isEmpty()) {
            return new ResponseEntity<>("Nama resep tidak boleh kosong", HttpStatus.INTERNAL_SERVER_ERROR);
            //cek ingredients kosong
        } else if (recipeModel.getIngredients().isEmpty()) {
            return new ResponseEntity<>("Bahan-bahan tidak boleh kosong", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            try{
                RecipeModel recipe = RecipeRepository.save(new RecipeModel(recipeModel.getName(), recipeModel.getIngredients(), recipeModel.getHowto(), recipeModel.getPublik(), recipeModel.getUserid()));
                return new ResponseEntity<>("Success", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


    }

    //delete resep by id
    @DeleteMapping("/resep/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable("id")long id){
        RecipeRepository.deleteById(id);
        return new ResponseEntity<>("Resep telah dihapus!", HttpStatus.OK);
    }

    //update resep by id
    @PutMapping("/resep/{id}")
    public ResponseEntity<RecipeModel> updateRecipe(@PathVariable("id")long id, @RequestBody RecipeModel recipeModel){
        Optional<RecipeModel> recipeData = RecipeRepository.findById(id);

        if (recipeData.isPresent()) {
            RecipeModel _recipeModel = recipeData.get();
            _recipeModel.setId(recipeModel.getId());
            _recipeModel.setHowto(recipeModel.getHowto());
            _recipeModel.setIngredients(recipeModel.getIngredients());
            _recipeModel.setName(recipeModel.getName());
            _recipeModel.setPublik(recipeModel.getPublik());
            _recipeModel.setUserid(recipeModel.getUserid());
            return new ResponseEntity<>(RecipeRepository.save(_recipeModel), HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
