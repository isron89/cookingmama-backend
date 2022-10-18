package com.cookingmama.cookingmamabackend.repository;

import com.cookingmama.cookingmamabackend.models.CommentModel;
import com.cookingmama.cookingmamabackend.models.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
public interface CommentRepository extends JpaRepository<CommentModel, Long> {
    List<CommentModel> findByRecipeid(String recipeid);
}
