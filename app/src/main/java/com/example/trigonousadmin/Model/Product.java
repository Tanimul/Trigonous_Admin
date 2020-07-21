package com.example.trigonousadmin.Model;

public class Product {
    private String adminid;
    private String productcode;
    private String productname;
    private int productprice;
    private int productquantity;
    private int productsell;
    private int productavailable;
    private int productdiscountprice;
    private String productdescription;
    private String productcategory;
    private String producturl;

    public Product() {
    }

    public Product(String adminid, String productcode, String productname, int productprice
            , int productquantity, int productsell, int productavailable, int productdiscountprice
            , String productdescription, String productcategory, String producturl) {
        this.adminid = adminid;
        this.productcode = productcode;
        this.productname = productname;
        this.productprice = productprice;
        this.productquantity = productquantity;
        this.productsell = productsell;
        this.productavailable = productavailable;
        this.productdiscountprice = productdiscountprice;
        this.productdescription = productdescription;
        this.productcategory = productcategory;
        this.producturl = producturl;
    }

    public String getAdminid() {
        return adminid;
    }

    public String getProducturl() {
        return producturl;
    }

    public void setProducturl(String producturl) {
        this.producturl = producturl;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public int getProductprice() {
        return productprice;
    }

    public void setProductprice(int productprice) {
        this.productprice = productprice;
    }

    public int getProductquantity() {
        return productquantity;
    }

    public void setProductquantity(int productquantity) {
        this.productquantity = productquantity;
    }

    public int getProductsell() {
        return productsell;
    }

    public void setProductsell(int productsell) {
        this.productsell = productsell;
    }

    public int getProductavailable() {
        return productavailable;
    }

    public void setProductavailable(int productavailable) {
        this.productavailable = productavailable;
    }

    public int getProductdiscountprice() {
        return productdiscountprice;
    }

    public void setProductdiscountprice(int productdiscountprice) {
        this.productdiscountprice = productdiscountprice;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    public String getProductcategory() {
        return productcategory;
    }

    public void setProductcategory(String productcategory) {
        this.productcategory = productcategory;
    }
}
