package com.cookingmama.cookingmamabackend.controllers;

import com.cookingmama.cookingmamabackend.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.cookingmama.cookingmamabackend.models.RatingModel;
import com.cookingmama.cookingmamabackend.repository.RatingRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.text.DecimalFormat;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/rating")
public class RatingController {
    @Autowired
    RatingRepository RatingRepository;

    @Autowired
    RecipeRepository RecipeRepository;
    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping(value = "/rate")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> postRate(@RequestBody RatingModel ratingModel){
//        System.out.println(RatingRepository.countUserRate(ratingModel.getRecipeid(), ratingModel.getUserid()));
//        if(RatingRepository.countByUserid(ratingModel.getUserid()) == 0) {
        if(RatingRepository.countUserRate(ratingModel.getRecipeid(), ratingModel.getUserid()) == 0) {
            //cek rating value 1-5
            if(ratingModel.getRate() > 0 && ratingModel.getRate() <= 5) {
                try{
                    RatingModel rate = RatingRepository.save(new RatingModel(ratingModel.getRate(), ratingModel.getRecipeid(), ratingModel.getUserid()));
                    float newRate = RatingRepository.getRecipeRating(ratingModel.getRecipeid());
                    System.out.println(newRate);
//                    System.out.println(ratingModel.getRecipeid());
                    System.out.println(Long.valueOf(ratingModel.getRecipeid()).longValue());
                    RecipeRepository.updateRate(newRate, Long.valueOf(ratingModel.getRecipeid()).longValue()); //;
                    System.out.println("masuk");
                    return new ResponseEntity<>("Success", HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("Rating must be between 1 to 5", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("You have already rated this recipe", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reciperate/{recipeid}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyRecipes(@PathVariable("recipeid") String recipeid) {
        System.out.println(RatingRepository.countRate(recipeid));
        if (RatingRepository.countRate(recipeid) == 0){
            System.out.println("MASUK BRO");
            try {
                String indexString = "{\"Message\":\"This recipe have no rating\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode noRating = mapper.readTree(indexString);
                return new ResponseEntity<>(noRating, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            float recipeRating = RatingRepository.getRecipeRating(recipeid);
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(1);
            return new ResponseEntity<>(df.format(recipeRating), HttpStatus.OK);
        }
    }
}
