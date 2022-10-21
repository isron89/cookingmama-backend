package com.cookingmama.cookingmamabackend.controllers;

import com.cookingmama.cookingmamabackend.models.Recipe;
import com.cookingmama.cookingmamabackend.models.User;
import com.cookingmama.cookingmamabackend.repository.RecipeRepository;
import com.cookingmama.cookingmamabackend.repository.UserRepository;
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

@RestController
@RequestMapping("api/recipe/rating")
public class RatingController {
    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @PostMapping(value = "/add")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> postRate(@RequestBody Rating userRating, @RequestParam(name = "recipeId") Long recipeId, @RequestParam(name = "userId") Long userId){
      Boolean canDoRating = ratingRepository.canDoRating(recipeId, userId);
      if (canDoRating){
          Rating rating = userRating;
          User user = userRepository.findById(userId).orElseThrow();
          Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();

          rating.setUser(user);
          rating.setRecipe(recipe);

          ratingRepository.save(rating);

          return new ResponseEntity<>("Success", HttpStatus.OK);
      } else {
          return new ResponseEntity<>("Failed", HttpStatus.OK);
      }
    }

    @GetMapping("/recipeId={recipeid}")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyRecipes(@PathVariable("recipeid") Long recipeId) {
        Float recipeRating = ratingRepository.getRecipeRating(recipeId);
        if (ratingRepository.countRate(recipeId) == 0){
            try {
                String indexString = "{\"Message\":\"This recipe have no rating\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode noRating = mapper.readTree(indexString);
                return new ResponseEntity<>(noRating, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(1);
            recipeRating = Float.parseFloat(df.format(recipeRating));
            return new ResponseEntity<>(recipeRating, HttpStatus.OK);
        }
    }
}
