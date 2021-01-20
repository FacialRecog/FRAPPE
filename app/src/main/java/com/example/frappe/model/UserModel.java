package com.example.frappe.model;

public class UserModel {
    public  int id;
    public  int age;
    public String username;
    public String password;

    public String getEmployee_Name() {
        return Employee_Name;
    }

    public String getLogin_status() {
        return Login_status;
    }

    public String getTime() {
        return Time;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String Employee_Name;
    public String Login_status;
    public String Time;
    public String deleted_at;
    public String created_at;
    public String updated_at;


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
