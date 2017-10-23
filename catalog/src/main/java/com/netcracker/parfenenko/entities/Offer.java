package com.netcracker.parfenenko.entities;

import com.netcracker.parfenenko.content.Category;
import com.netcracker.parfenenko.content.Price;
import com.netcracker.parfenenko.content.Tag;

public class Offer {

    private Price price;
    private Category category;
    private Tag tag;
    private long id;
    private String name;
    private String description;

    public Offer() {}

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
