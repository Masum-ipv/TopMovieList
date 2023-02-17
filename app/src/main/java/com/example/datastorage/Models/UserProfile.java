package com.example.datastorage.Models;

public class UserProfile {
    String username, phone;

    public UserProfile(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }

    public UserProfile() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
