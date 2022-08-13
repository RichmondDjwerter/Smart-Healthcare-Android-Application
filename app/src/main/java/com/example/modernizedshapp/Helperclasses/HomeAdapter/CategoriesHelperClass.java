package com.example.modernizedshapp.Helperclasses.HomeAdapter;

import android.graphics.drawable.Drawable;

public class CategoriesHelperClass {
    int image;
    String tittle;
    Drawable Gradient;

    public CategoriesHelperClass(int image, String tittle, Drawable gradient) {
        this.image = image;
        this.tittle = tittle;
        Gradient = gradient;
    }

    public int getImage() {
        return image;
    }

    public String getTittle() {
        return tittle;
    }

    public Drawable getGradient() {
        return Gradient;
    }
}
