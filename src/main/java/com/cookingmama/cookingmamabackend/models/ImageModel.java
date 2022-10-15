package com.cookingmama.cookingmamabackend.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "img_table")
public class ImageModel {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (name = "name")
    private String name;

    @Column (name = "type")
    private String type;

    @Column (name = "pictByte", length = 1000)
    private byte[] picByte;

    public ImageModel(){
        super();
    }

    public ImageModel(String name, String type, byte[] picByte){
        this.name = name;
        this.type = type;
        this.picByte = picByte;
    }
}
