package com.parse.starter;

import java.util.Date;

/**
 * Created by Shawn on 2015-02-17.
 */
public class Patient {//Patient Object

    private String name;
    private String gender;
    private Date birth;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Patient(){
        name = "";
        gender = "";
        birth = new Date();
        age = 0;
    }

    public Patient(String name, String gender, Date birth, int age){
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.age = age;
    }

    public Patient(Patient p){
        this.name = p.name;
        this.gender = p.gender;
        this.birth = p.birth;
        this.age = p.age;
    }

    public String toString(){
        return name + gender + birth.toString() + age;
    }
}
