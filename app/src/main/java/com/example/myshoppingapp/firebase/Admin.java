package com.example.myshoppingapp.firebase;

import com.example.myshoppingapp.helpers.AdminAccess;

public class Admin implements AdminAccess {
    public static Admin admin = new Admin();
    private String Username;
    @Override
    public boolean AdminRouter(Customers temp) {
        return true;
    }
    private Admin() {}

    public static Admin getAdmin() {
        return admin;
    }

    public static void setAdmin(Admin admin) {
        Admin.admin = admin;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }
}
