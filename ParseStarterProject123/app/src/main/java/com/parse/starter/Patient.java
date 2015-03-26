package com.parse.starter;

import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Shawn on 2015-02-17.
 */
public class Patient {//Patient Object

    private String name;
    private String gender;
    private Date birth;
    private int age;
    private String username;
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

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
        this.age = calcAge(birth);
    }
    public String getUserName() {
        return username;
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
        username = "";
    }

    public Patient(String name, String gender, Date birth, int age, String username, int ID){
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.age = age;
        this.username = username;
        this.ID = ID;
    }

    public Patient(String name, String gender, Date birth, String username, int ID){
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.age = calcAge(birth);
        this.username = username;
        this.ID = ID;
    }

    public Patient(String strpatient){
        String[] parts = strpatient.split("\n");
        this.name = parts[0];
        this.gender = parts[1];
        DateFormat df = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        try {
            this.birth = df.parse(parts[2]);
        }catch(Exception e){}
        this.age = Integer.parseInt(parts[3]);
        this.username = parts[4];
        this.ID = Integer.parseInt(parts[5]);
    }

    public Patient(Patient p){
        this.name = p.name;
        this.gender = p.gender;
        this.birth = p.birth;
        this.age = p.age;
        this.username = p.username;
        this.ID = p.ID;
    }

    public String toString(){
        DateFormat df = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        return name + "\n" + gender + "\n" + df.format(birth) + "\n" + age + "\n" + username + "\n" + ID + "\n" ;
    }

    public int calcAge(Date born) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(born);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int current_year = Calendar.getInstance().get(Calendar.YEAR);
        int current_month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int current_day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);



        if (current_month < month)
            return current_year - year - 1;
        else if (current_month == month) {
            if (current_day < day)
                return current_year - year - 1;
            else
                return current_year - year;
        } else
            return current_year - year;
    }
}
