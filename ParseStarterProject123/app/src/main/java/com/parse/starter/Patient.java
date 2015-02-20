package com.parse.starter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    public Patient(String strpatient){
        String[] parts = strpatient.split("\n");
        this.name = parts[0];
        this.gender = parts[1];
        DateFormat df = new SimpleDateFormat("dow mon dd hh:mm:ss zzz yyyy");
        try {
            this.birth = df.parse(parts[2]);
        }catch(Exception e){}
        this.age = Integer.parseInt(parts[3]);
    }

    public Patient(Patient p){
        this.name = p.name;
        this.gender = p.gender;
        this.birth = p.birth;
        this.age = p.age;
    }

    public String toString(){
        return name + "\n" + gender + "\n" + birth.toString() + "\n" + age+ "\n" ;
    }
}
