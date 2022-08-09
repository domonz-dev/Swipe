package com.domonz.swipe;

public class ProductModel {

    String image, product_name, product_type;
    double price, tax;


    public ProductModel() {
    }

    public ProductModel(String image, String product_name, String product_type, double price, double tax) {
        this.image = image;
        this.product_name = product_name;
        this.product_type = product_type;
        this.price = price;
        this.tax = tax;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
}
