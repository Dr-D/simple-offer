package com.drd.data.entities;

import javax.persistence.*;

/**
 * Author: dr-d 2017-08-25
 */
@Entity
@Table(name = "offer")
public class OfferEntity {

    long id;
    String name;
    String Description;
    float price;

    public OfferEntity(){}

    public OfferEntity(long id, String name, String description, float price) {
        this.id = id;
        this.name = name;
        Description = description;
        this.price = price;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(unique = true, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //default length 255
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OfferEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Description='" + Description + '\'' +
                ", price=" + price +
                '}';
    }
}
