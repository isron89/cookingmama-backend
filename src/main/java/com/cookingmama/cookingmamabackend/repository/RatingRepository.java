package com.cookingmama.cookingmamabackend.repository;

import com.cookingmama.cookingmamabackend.models.RatingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
public interface RatingRepository extends JpaRepository<RatingModel,Long> {
//    int countByUserid(String userid);
    @Query(value = "SELECT COUNT(userid) FROM rating WHERE recipeid = ?1 AND userid = ?2", nativeQuery=true)
    int countUserRate(String recipeid, String userid);

    @Query(value = "SELECT AVG(rate) FROM rating WHERE recipeid = ?1", nativeQuery=true)
    float getRecipeRating(String recipeid);
}
