package com.example.myapplicationdkdkdkdk;


import java.util.ArrayList;

public class Parent {

    private String name;
    private ArrayList<Child> childnameList = new ArrayList<Child>();;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Child> childList() {
        return childnameList;
    }
    public void setChildnameList(ArrayList<Child> childnameList) {
        this.childnameList = childnameList;
    }

}
