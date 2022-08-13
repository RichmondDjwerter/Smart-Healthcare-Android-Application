package com.example.modernizedshapp.doctor.foodtip;

public class Condition {

    public String getCategory(float result) {
        String category;
        if (result < 15) {
            category = "Severe Thinness";
        } else if (result >= 15 && result <= 16) {
            category = "Moderate Thinness";
        } else if (result > 16 && result <= 18.5) {
            category = "Mild Thinness";
        } else if (result > 18.5 && result <= 25) {
            category = "Normal";
        } else if (result > 25 && result <= 30) {
            category = "Overweight";
        } else if (result > 30 && result <= 35) {
            category = "Obese Class I";
        } else if (result > 35 && result <= 40) {
            category = "Obese Class II";
        } else {
            category = "Obese Class III";
        }
        return category;
    }

}
