package com.netcracker.parfenenko.entity;

import java.util.Objects;

public class FreshOrder {

    private String description;
    private String customerMail;

    public FreshOrder() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerMail() {
        return customerMail;
    }

    public void setCustomerMail(String customerMail) {
        this.customerMail = customerMail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FreshOrder that = (FreshOrder) o;
        return Objects.equals(description, that.description) &&
                Objects.equals(customerMail, that.customerMail);
    }

    @Override
    public int hashCode() {

        return Objects.hash(description, customerMail);
    }

}
