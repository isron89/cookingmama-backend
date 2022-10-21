package com.cookingmama.cookingmamabackend.models;

import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "comment")

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Size(max = 2000)
    private String comment;
}
