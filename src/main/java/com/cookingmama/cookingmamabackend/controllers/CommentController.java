package com.cookingmama.cookingmamabackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.cookingmama.cookingmamabackend.models.CommentModel;
import com.cookingmama.cookingmamabackend.repository.CommentRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentRepository CommentRepository;
    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping(value = "/postcomment")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> postComment(@RequestBody CommentModel commentModel){
        System.out.println(commentModel.getText());
        //cek name kosong
        if(commentModel.getText() == null || commentModel.getText().isEmpty()) {
            return new ResponseEntity<>("Comment can't be empty", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            try{
                CommentModel comment = CommentRepository.save(new CommentModel(commentModel.getText(), commentModel.getRecipeid(), commentModel.getUserid()));
                return new ResponseEntity<>("Success", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/comments/{recipeid}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyRecipes(@PathVariable("recipeid") String recipeid) {
        List<CommentModel> allComment = CommentRepository.findByRecipeid(recipeid);
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
