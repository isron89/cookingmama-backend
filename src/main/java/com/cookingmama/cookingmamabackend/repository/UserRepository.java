package com.cookingmama.cookingmamabackend.repository;

import java.util.List;
import java.util.Optional;

import com.cookingmama.cookingmamabackend.models.RecipeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cookingmama.cookingmamabackend.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
