package com.cookingmama.cookingmamabackend.models;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

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
    @Length(max=2000)
    private String ingredients;

    @Column(name = "howto")
    @Length(max=4000)
    private String howto;

    @Column(name = "publik")
    private Boolean publik;

    @Column(name = "userid")
    private String userid;

    @Nullable
    @Column(name = "rate")
    private float rate;

    public RecipeModel() {
    }
    //long id, String name, String ingredients, String recipeModelName, Boolean publik, String userid
    public RecipeModel(String name, String ingredients, String howto, Boolean publik, String userid) {

        this.name = name;
        this.ingredients = ingredients;
        this.howto = howto;
        this.publik = publik;
        this.userid = userid;
        this.rate = 0;
    }

    @Override
    public String toString() {
        return "RecipeModel [Recipe id=" + id + ", name=" + name + ", ingredients=" + ingredients + ", howto="
                + howto + ", publik=" + publik + ", iduser=" + userid + "]";
    }

}
