package com.cookingmama.cookingmamabackend.repository;

import com.cookingmama.cookingmamabackend.models.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
    Optional<ImageModel> findByName(String name);
}
