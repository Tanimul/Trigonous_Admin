package com.example.trigonousadmin.Model;

public class Admin {
    private String id;
    private String adminname;
    private String fullname;
    private String imageurl;

    public Admin() {
    }

    public Admin(String id, String adminname, String fullname, String imageurl) {
        this.id = id;
        this.adminname = adminname;
        this.fullname = fullname;
        this.imageurl = imageurl;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}
