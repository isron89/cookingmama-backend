package com.cookingmama.cookingmamabackend.repository;

import com.cookingmama.cookingmamabackend.model.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
public interface RecipeRepository extends JpaRepository<RecipeModel, Long> {
    List<RecipeModel> findByName(String name);
    List<RecipeModel> findByUserid(long userid);
}

