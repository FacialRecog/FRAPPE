package com.example.frappe.model;

public class UserModel {
    public  int id;
    public  int age;
    public String username;
    public String password;

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public String getModel_pic() {
        return model_pic;
    }

    public String getImage() {
        return image;
    }

    public String confirm_password;
    public String model_pic;
    public String image;
}
