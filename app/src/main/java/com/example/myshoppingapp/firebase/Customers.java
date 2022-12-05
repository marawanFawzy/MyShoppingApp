package com.example.myshoppingapp.firebase;

import java.util.Date;

public class Customers {
    private String  id;
    private String name, Username, Password, job, Email , gender;
    private Date Birthdate;
    private boolean flag;

    public Customers() {
    }

    public Customers(String id, String name, String username, String password, Date birthdate, String job, String email , String gender, boolean flag) {
        this.id = id;
        this.name = name;
        Username = username;
        Password = password;
        Birthdate = birthdate;
        this.job = job;
        Email = email;
        this.gender = gender;
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Date getBirthdate() {
        return Birthdate;
    }

    public void setBirthdate(Date birthdate) {
        Birthdate = birthdate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}