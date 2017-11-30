package com.netcracker.parfenenko.model;

import java.util.List;

public class ConfigurableOffersSearch {
    
    private List<Category> categories;
    private List<Tag> tags;
    private double from;
    private double to;

    public ConfigurableOffersSearch() {}

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public double getFrom() {
        return from;
    }

    public void setFrom(double from) {
        this.from = from;
    }

    public double getTo() {
        return to;
    }

    public void setTo(double to) {
        this.to = to;
    }
    
}
