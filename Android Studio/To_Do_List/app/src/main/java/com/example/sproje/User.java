package com.example.sproje;

public class User {
    private String email;
    private String username;
    private String uid; // Kullanıcının benzersiz kimliği


    public User(String email, String username) {
    }

    public User(String email, String username, String uid) {
        this.email = email;
        this.username = username;
        this.uid = uid;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
