package com.cookingmama.cookingmamabackend.repository;

import com.cookingmama.cookingmamabackend.models.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
public interface RecipeRepository extends JpaRepository<RecipeModel, Long> {
//    List<RecipeModel> findByName(String name);
    List<RecipeModel> findByNameContaining(String name);
    List<RecipeModel> findByUserid(String userid);
    List<RecipeModel> findByPublikTrue();
    List<RecipeModel> findByPublikFalse();

//    List<RecipeModel> searchRecipe(String query);
//    @Query("SELECT * FROM recipes WHERE recipes.\"name\" LIKE CONCAT('%',:query, '%')" )
//    List<RecipeModel> searchRecipe(String query);
}

