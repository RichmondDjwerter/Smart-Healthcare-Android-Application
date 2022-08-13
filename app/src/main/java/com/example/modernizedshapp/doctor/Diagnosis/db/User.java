package com.example.modernizedshapp.doctor.Diagnosis.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.modernizedshapp.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class User {
    private static Bitmap FEMALE_PICTURE;
    private static Bitmap MALE_PICTURE;
    private final int id;
    private final Date birthDate;
    private final String name;
    private final String gender;

    public User(int id, String name, String birthDate, String gender) throws ParseException {
        this.id = id;
        this.birthDate = ChatSQLiteDBHelper.DB_DATE_USER_FORMAT.parse(birthDate);
        this.gender = gender;
        this.name = name;
    }

    public User(int id, String name, Date birthDate, String gender) {
        this.id = id;
        this.birthDate = birthDate;
        this.name = name;
        this.gender = gender;
    }

    public static void loadBitmaps(Context context) {
        FEMALE_PICTURE = BitmapFactory.decodeResource(context.getResources(), R.drawable.female_dark);
        MALE_PICTURE = BitmapFactory.decodeResource(context.getResources(), R.drawable.male_dark);
    }

    public static Bitmap getPictureBasedOnGender(String gender) {
        if (gender.equals("M")) {
            return MALE_PICTURE;
        }
        return FEMALE_PICTURE;
    }

    public int getId() {
        return id;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPicture() {
        return getPictureBasedOnGender(this.gender);
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        Calendar now = new GregorianCalendar();
        Calendar birth = new GregorianCalendar();
        now.setTime(new Date());
        birth.setTime(this.birthDate);
        int result = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        if (birth.get(Calendar.MONTH) > now.get(Calendar.MONTH)) {
            result--;
        } else if (birth.get(Calendar.MONTH) == now.get(Calendar.MONTH)) {
            if (birth.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH)) {
                result--;
            }
        }
        return result;
    }

    public String getFullGenderName() {
        if (this.getGender().equals("F")) {
            return "female";
        } else {
            return "male";
        }
    }

}
