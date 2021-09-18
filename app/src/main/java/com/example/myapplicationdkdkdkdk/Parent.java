package com.example.myapplicationdkdkdkdk;


import java.util.ArrayList;

public class Parent {

    private String name;
    private ArrayList<Child> productList = new ArrayList<Child>();;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Child> getProductList() {
        return productList;
    }
    public void setProductList(ArrayList<Child> productList) {
        this.productList = productList;
    }

}
