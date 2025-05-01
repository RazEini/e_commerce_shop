package com.shop.bagrutproject.models;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class Deal {
    private String id;
    private String title;
    private String description;
    private double discountPercentage;
    private String validUntil;
    private String itemType;

    public Deal() {}

    public Deal(String id, String title, String description, double discountPercentage, String validUntil, String itemType) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.discountPercentage = discountPercentage;
        this.validUntil = validUntil;
        this.itemType = itemType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public boolean isValid() {
        String validUntilString = this.validUntil; // התאריך "31/05/2025"

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date validUntilDate = sdf.parse(validUntilString);

            // אם אתה רוצה להשוות לתאריך נוכחי:
            Date currentDate = new Date();
            return !validUntilDate.before(currentDate);
        } catch (Exception e) {
            // טיפול בשגיאות (אם הפורמט לא תואם)
            return false;
        }
    }
}
