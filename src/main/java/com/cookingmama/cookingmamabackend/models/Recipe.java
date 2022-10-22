package com.cookingmama.cookingmamabackend.models;

import javax.persistence.*;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "recipe_id")
    private long id;

    private String recipeName;

    @Column(nullable = false)
    private LocalDateTime date;

    @Length(max=2000)
    private String ingredients;

    @Length(max=4000)
    private String directions;

    private Boolean publicAccess;

    private String imgUrlPath;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    private void onCreate(){
        date = LocalDateTime.now();
    }
}
