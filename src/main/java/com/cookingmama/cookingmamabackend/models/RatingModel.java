package com.cookingmama.cookingmamabackend.models;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "rating")

public class RatingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "rate")
    private int rate;

    @Column(name = "recipeid")
    private String recipeid;

    @Column(name = "userid")
    private String userid;

    public RatingModel() {
    }

    public RatingModel(int rate, String recipeid, String userid) {
        this.rate = rate;
        this.recipeid = recipeid;
        this.userid = userid;
    }
}
