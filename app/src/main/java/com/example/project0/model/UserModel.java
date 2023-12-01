package com.example.project0.model;


public class UserModel {
    private String email     ;
    private String username  ;
    private String role      ;
    private String password  ;

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail(){
        return this.email ;
    }
    public void setEmail(String email){
        this.email = email ;
    }
    public String getRole(){
        return this.role ;
    }
    public void setRole(String role){
        this.role = role ;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

