package com.cookingmama.cookingmamabackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.cookingmama.cookingmamabackend.models.Rating;
import com.cookingmama.cookingmamabackend.repository.RatingRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.text.DecimalFormat;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rating")
public class RatingController {
    @Autowired
    RatingRepository ratingRepository;
    private RestTemplate restTemplate = new RestTemplate();

//    @PostMapping(value = "/rate")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public ResponseEntity<String> postRate(@RequestBody Rating rating){
//      Float recipeRating = ratingRepository.getRecipeRating(recipeId);
//        if (recipeRating == 0){
//            try {
//                String indexString = "{\"Message\":\"This recipe have no rating\"}";
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode notFound = mapper.readTree(indexString);
//                return ResponseEntity<>(notFound, HttpStatus.OK);
//            } catch (Exception e) {
//                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } else {
//            DecimalFormat df = new DecimalFormat();
//            df.setMaximumFractionDigits(1);
//            return new ResponseEntity<>(df.format(recipeRating), HttpStatus.OK);
//        }
//    }

//    @GetMapping("/reciperate/{recipeid}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public ResponseEntity<?> getMyRecipes(@PathVariable("recipeid") String recipeid) {
//        System.out.println(ratingRepository.countRate(recipeid));
//        if (ratingRepository.countRate(recipeid) == 0){
//            System.out.println("MASUK BRO");
//            try {
//                String indexString = "{\"Message\":\"This recipe have no rating\"}";
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode noRating = mapper.readTree(indexString);
//                return new ResponseEntity<>(noRating, HttpStatus.OK);
//            } catch (Exception e) {
//                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } else {
//            float recipeRating = ratingRepository.getRecipeRating(recipeid);
//            DecimalFormat df = new DecimalFormat();
//            df.setMaximumFractionDigits(1);
//            return new ResponseEntity<>(df.format(recipeRating), HttpStatus.OK);
//        }
//    }
}
