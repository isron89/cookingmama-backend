package com.cookingmama.cookingmamabackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.cookingmama.cookingmamabackend.model.RecipeModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@CrossOrigin(origins = "http://localhost:8081")
public interface RecipeRepository extends JpaRepository<RecipeModel, Long> {
    List<RecipeModel> findByName(String name);
    List<RecipeModel> findByUserid(long userid);
}

