package com.example.praba.ipfire;

public class UserDetails {

    String uid , address , email , username , status;


    public UserDetails(String uid , String username, String status) {
        this.uid = uid;
        this.username = username;
        this.status = status;
    }


    public String getUid() {
        return uid;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
