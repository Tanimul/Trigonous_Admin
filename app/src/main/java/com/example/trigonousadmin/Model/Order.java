package com.example.trigonousadmin.Model;

public class Order {
    String adminid;
    String cal;
    String productcode;
    String productcategory;
    int productquantity;
    String productcost;
    String deliverycost;
    int totalcost;
    int totalpaid;
    int totaldue;
    String customername;
    String customerarea;
    String customerphone;
    String request;
    boolean completedelivery;
    String key;


    public Order() {
    }

    public Order(String adminid,String cal, String productcode, String productcategory, int productquantity, String productcost
            , String deliverycost, int totalcost, int totalpaid, int totaldue, String customername
            , String customerarea, String customerphone, String request,boolean completedelivery,String key) {
        this.adminid=adminid;
        this.cal = cal;
        this.productcode = productcode;
        this.productcategory = productcategory;
        this.productquantity = productquantity;
        this.productcost = productcost;
        this.deliverycost = deliverycost;
        this.totalcost = totalcost;
        this.totalpaid = totalpaid;
        this.totaldue = totaldue;
        this.customername = customername;
        this.customerarea = customerarea;
        this.customerphone = customerphone;
        this.request = request;
        this.completedelivery=completedelivery;
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isCompletedelivery() {
        return completedelivery;
    }

    public void setCompletedelivery(boolean completedelivery) {
        this.completedelivery = completedelivery;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getProductcategory() {
        return productcategory;
    }

    public void setProductcategory(String productcategory) {
        this.productcategory = productcategory;
    }

    public int getProductquantity() {
        return productquantity;
    }

    public void setProductquantity(int productquantity) {
        this.productquantity = productquantity;
    }

    public String getProductcost() {
        return productcost;
    }

    public void setProductcost(String productcost) {
        this.productcost = productcost;
    }

    public String getDeliverycost() {
        return deliverycost;
    }

    public void setDeliverycost(String deliverycost) {
        this.deliverycost = deliverycost;
    }

    public int getTotalcost() {
        return totalcost;
    }

    public void setTotalcost(int totalcost) {
        this.totalcost = totalcost;
    }

    public int getTotalpaid() {
        return totalpaid;
    }

    public void setTotalpaid(int totalpaid) {
        this.totalpaid = totalpaid;
    }

    public int getTotaldue() {
        return totaldue;
    }

    public void setTotaldue(int totaldue) {
        this.totaldue = totaldue;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getCustomerarea() {
        return customerarea;
    }

    public void setCustomerarea(String customerarea) {
        this.customerarea = customerarea;
    }

    public String getCustomerphone() {
        return customerphone;
    }

    public void setCustomerphone(String customerphone) {
        this.customerphone = customerphone;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
