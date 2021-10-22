package com.example.fireex.model;

public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private Integer roleId;
    private String phNum;

    public User(){}

    public User(Integer id, String firstName, String lastName, Integer roleId, String phNum) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
        this.phNum = phNum;
    }

    public User(String firstName, String lastName, Integer roleId, String phNum) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
        this.phNum = phNum;
    }

    public String getPhNum() {
        return phNum;
    }

    public void setPhNum(String phNum) {
        this.phNum = phNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
