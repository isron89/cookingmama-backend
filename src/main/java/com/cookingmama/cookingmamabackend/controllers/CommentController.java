package com.cookingmama.cookingmamabackend.controllers;

import com.cookingmama.cookingmamabackend.models.Recipe;
import com.cookingmama.cookingmamabackend.models.User;
import com.cookingmama.cookingmamabackend.repository.RecipeRepository;
import com.cookingmama.cookingmamabackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.cookingmama.cookingmamabackend.models.Comment;
import com.cookingmama.cookingmamabackend.repository.CommentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@RequestMapping("api/recipe/comment")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @PostMapping(value = "/post")
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> postComment(@RequestBody Comment commentModel, @RequestParam(name = "recipeId") Long recipeId, @RequestParam(name = "userId") Long userId){

        User user = userRepository.findById(userId).orElseThrow();
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();

        if(recipe.getPublicAccess() == false && recipe.getUser()!=user){
            return new ResponseEntity<>("Only its user that can post a comment on their private recipe", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(commentModel.getComment() == null || commentModel.getComment().isEmpty()) {
            return new ResponseEntity<>("Comment can't be empty", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            try {
                Comment newComment = commentModel;

                newComment.setUser(user);
                newComment.setRecipe(recipe);

                commentRepository.save(newComment);
                return new ResponseEntity<>("Success", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/recipeId={recipeid}")
    //@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyRecipes(@PathVariable("recipeid") Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
        List<Comment> allComment = commentRepository.findCommentByRecipe(recipe);
        if (allComment.isEmpty()){
            try {
                String indexString = "{\"Message\":\"There is no comment on this recipe\"}";
                ObjectMapper mapper = new ObjectMapper();
                JsonNode notFound = mapper.readTree(indexString);
                return new ResponseEntity<>(notFound, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(allComment, HttpStatus.OK);
        }
    }

}
