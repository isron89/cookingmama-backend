package com.cookingmama.cookingmamabackend.repository;

import com.cookingmama.cookingmamabackend.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin(origins = "http://localhost:8080")
public interface RatingRepository extends JpaRepository<Rating,Long> {
//    int countByUserid(String userid);
    @Query(value = "SELECT COUNT(userid) FROM rating WHERE recipeid = ?1 AND userid = ?2", nativeQuery=true)
    int countUserRate(String recipeid, String userid);

    @Query(value = "SELECT AVG(rating) FROM rating WHERE recipe_id = ?1", nativeQuery=true)
    Float getRecipeRating(Long recipeid);

    @Query(value = "SELECT COUNT(userid) FROM rating WHERE recipeid = ?1", nativeQuery=true)
    int countRate(String recipeid);
}
