package com.drd.rest.model;

/**
 * Author: drd 2017-08-25
 */
public class OfferRest {

    private long id;
    private String name;
    private String Description;
    private float price;

    public OfferRest() {}//Required for jax-rs when create called

    public OfferRest(long id, String name, String description, float price) {
        this.id = id;
        this.name = name;
        Description = description;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
        return "OfferRest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Description='" + Description + '\'' +
                ", price=" + price +
                '}';
    }
}
