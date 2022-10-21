package com.cookingmama.cookingmamabackend.repository;

import com.cookingmama.cookingmamabackend.models.Recipe;
import com.cookingmama.cookingmamabackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Repository
@CrossOrigin(origins = "http://localhost:8080")
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
//    List<RecipeModel> findByName(String name);
    List<Recipe> findByRecipeNameAndPublicAccessTrue(String recipeName);
    List<Recipe> findByUser(User user);
    List<Recipe> findByPublicAccessTrue();
    List<Recipe> findByPublicAccessFalse();

    List<Recipe> findByPublicAccessFalseAndUser(User user);

    List<Recipe> findByRecipeNameAndPublicAccessFalseAndUser(User user);

    List<Recipe> findByRecipeName(String name);

//    List<RecipeModel> searchRecipe(String query);
//    @Query("SELECT * FROM recipes WHERE recipes.\"name\" LIKE CONCAT('%',:query, '%')" )
//    List<RecipeModel> searchRecipe(String query);
}

