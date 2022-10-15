package com.cookingmama.cookingmamabackend.models;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "recipes")
public class RecipeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ingredients")
    private String ingredients;

    @Column(name = "howto")
    private String howto;

    @Column(name = "publik")
    private Boolean publik;

    @Column(name = "userid")
    private String userid;
//    private String steps;
//    private String image;

    public RecipeModel() {
    }
    //long id, String name, String ingredients, String recipeModelName, Boolean publik, String userid
    public RecipeModel(String name, String ingredients, String howto, Boolean publik, String userid) {

        this.name = name;
        this.ingredients = ingredients;
        this.howto = howto;
        this.publik = publik;
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "RecipeModel [Recipe id=" + id + ", name=" + name + ", ingredients=" + ingredients + ", howto="
                + howto + ", publik=" + publik + ", iduser=" + userid + "]";
    }

}
