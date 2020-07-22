package com.example.trigonousadmin.Model;

public class Customer {
    String name;
    String area;
    String phone;

    public Customer() {
    }

    public Customer(String name, String area, String phone) {
        this.name = name;
        this.area = area;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
