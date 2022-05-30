package org.mycode.finalproject.Model;

public class Products
{
    private String name, price, description, brand, category, image, id ;
    private int countInStock ;

    public int getCountInStock() {
        return countInStock;
    }

    public void setCountInStock(int countInStock) {
        this.countInStock = countInStock;
    }

    public Products(String name, String price, String description, String brand, String category, String image, String id, int countInStock) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.image = image;
        this.id = id;
        this.countInStock = countInStock;
    }

    public Products(Products sample) {
        this.name = sample.name;
        this.price = sample.price;
        this.description = sample.description;
        this.brand = sample.brand;
        this.category = sample.category;
        this.image = sample.image;
        this.id = sample.id;
        this.countInStock = sample.countInStock;
    }

    public Products clone() throws CloneNotSupportedException {
        return new Products( this );
    }

    public Products() {

    }

    public Products(String name, String price, String description, String brand, String category, String image, String id) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.image = image;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void cleanImagePath(String imagePath) {

    }
}
