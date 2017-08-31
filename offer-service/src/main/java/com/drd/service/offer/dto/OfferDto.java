package com.drd.service.offer.dto;

/**
 * Author: drd 2017-08-25
 */
public class OfferDto {

    long id;
    String name;
    String Description;
    float price;

    public OfferDto(long id, String name, String description, float price) {
        this.id = id;
        this.name = name;
        Description = description;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return "OfferDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Description='" + Description + '\'' +
                ", price=" + price +
                '}';
    }
}
