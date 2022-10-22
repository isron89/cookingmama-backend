package com.cookingmama.cookingmamabackend.repository;

import com.cookingmama.cookingmamabackend.models.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
public interface RecipeRepository extends JpaRepository<RecipeModel, Long> {
//    List<RecipeModel> findByName(String name);
    List<RecipeModel> findByNameContainingAndPublikTrue(String name);
    @Query(value = "SELECT * FROM recipes WHERE userid=?1 AND name LIKE %?2%", nativeQuery=true)
    List<RecipeModel> searchMyRecipe(String userid, String name);
    List<RecipeModel> findByUserid(String userid);
    List<RecipeModel> findByPublikTrue();
    List<RecipeModel> findByPublikFalse();


//    @Modifying
//    @Query(value = "UPDATE recipes SET rate=?1 WHERE id=?2", nativeQuery=true)
//    void updateRate(float rate, int id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE recipes u SET rate=?1 WHERE u.id=?2", nativeQuery=true)
    void updateRate(float rate, long id);


//    List<RecipeModel> searchRecipe(String query);
//    @Query("SELECT * FROM recipes WHERE recipes.\"name\" LIKE CONCAT('%',:query, '%')" )
//    List<RecipeModel> searchRecipe(String query);
}

