package com.example.modernizedshapp.Helperclasses.HomeAdapter;

public class FeaturedHealperClass {
    int image;
    String tittle, description;

    public FeaturedHealperClass(int image, String tittle, String description) {
        this.image = image;
        this.tittle = tittle;
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDescription() {
        return description;
    }
}
