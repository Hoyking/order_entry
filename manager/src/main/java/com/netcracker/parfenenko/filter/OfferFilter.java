package com.netcracker.parfenenko.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OfferFilter {

    @JsonProperty
    private List<Long> categories;
    @JsonProperty
    private List<String> tags;
    @JsonProperty(defaultValue = "0")
    private double from;
    @JsonProperty(defaultValue = "0")
    private double to;

    public OfferFilter() {}

    public OfferFilter(List<Long> categories, List<String> tags, double from, double to) {
        this.categories = categories;
        this.tags = tags;
        this.from = from;
        this.to = to;
    }

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
