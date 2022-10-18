package com.cookingmama.cookingmamabackend.models;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "comment")

public class CommentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "text")
    private String text;

    @Column(name = "recipeid")
    private String recipeid;

    @Column(name = "userid")
    private String userid;

    public CommentModel() {
    }

    public CommentModel(String text, String recipeid, String userid) {
        this.text = text;
        this.recipeid = recipeid;
        this.userid = userid;
    }
}
