package com.cookingmama.cookingmamabackend.repository;

import com.cookingmama.cookingmamabackend.models.Recipe;
import com.cookingmama.cookingmamabackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Repository
@CrossOrigin(origins = "http://localhost:8080")
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
//    List<RecipeModel> findByName(String name);
    List<Recipe> findRecipesByPublicAccessTrueAndRecipeNameContaining(String recipeName);
    List<Recipe> findByUser(User user);
    List<Recipe> findByPublicAccessTrue();
    List<Recipe> findByPublicAccessFalse();

    List<Recipe> findByPublicAccessFalseAndUser(User user);

    List<Recipe> findRecipesByPublicAccessFalseAndUserAndRecipeNameContaining(User user, String recipeName);

    List<Recipe> findRecipesByUserAndRecipeName(User user, String recipeName);

    List<Recipe> findByRecipeName(String name);

    @Query(value = "SELECT * FROM recipes AS r WHERE r.user_id = ?1 AND LOWER(r.recipe_name) LIKE Lower(?2) ORDER BY r.recipe_id DESC", nativeQuery=true)
    List<Recipe> findRecipesByUserIdAndRecipeName(Long userId, String searchText);

    @Query(value = "SELECT * FROM recipes AS r WHERE r.user_id = ?1 OR r.public_access=true AND LOWER(r.recipe_name) LIKE Lower(?2) ORDER BY r.recipe_id DESC", nativeQuery=true)
    List<Recipe> findRecipesByUserIdOrPublicAccessTrueAndRecipeName(Long userId, String searchText);

    @Query(value = "SELECT * FROM recipes AS r WHERE r.public_access=true AND LOWER(r.recipe_name) LIKE Lower(?1) ORDER BY r.recipe_id DESC", nativeQuery=true)
    List<Recipe> findRecipesByPublicAccessAndRecipeName(String searchText);

//    List<RecipeModel> searchRecipe(String query);
//    @Query("SELECT * FROM recipes WHERE recipes.\"name\" LIKE CONCAT('%',:query, '%')" )
//    List<RecipeModel> searchRecipe(String query);
}

