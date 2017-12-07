package com.netcracker.parfenenko.filter;

import java.util.List;

public class OfferFilter {
    
    private List<Long> categories;
    private List<String> tags;
    private double from;
    private double to;

    public OfferFilter() {}

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
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
