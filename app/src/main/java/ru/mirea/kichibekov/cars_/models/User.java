package ru.mirea.kichibekov.cars_.models;

import java.util.ArrayList;

public class User {
    private String name, email, pass, phone, carNumber;
    Long age, experience;
    ArrayList<String> comments;

    public User(){}

    public User(String name, String email, String pass, String phone, String carNumber) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.carNumber = carNumber;
    }

    public User(String name, String email, String pass, String phone, String carNumber, ArrayList<String> comments) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.carNumber = carNumber;
        this.comments = comments;
    }

    public User(String name, String email, String phone, String carNumber, ArrayList<String> comments) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.carNumber = carNumber;
        this.comments = comments;
    }

    public User(String name, String email, String phone, String carNumber,
                ArrayList<String> comments, Long age, Long experience) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.carNumber = carNumber;
        this.comments = comments;
        this.age = age;
        this.experience = experience;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public String getCarNumber() {
        return carNumber;
    }
    public String getPhone() {
        return phone;
    }

    public String getAge() {
        return age.toString();
    }

    public String getExperience() {
        return experience.toString();
    }
}

