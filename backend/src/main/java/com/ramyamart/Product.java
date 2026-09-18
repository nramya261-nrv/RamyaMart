package com.ramyamart;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String brand;

    private String description;

    private double price;

    private String category;

    private int stock;


    // =====================================
    // DEFAULT CONSTRUCTOR
    // =====================================

    public Product() {
    }


    // =====================================
    // CONSTRUCTOR
    // =====================================

    public Product(
            String name,
            String brand,
            String description,
            double price,
            String category,
            int stock) {

        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }


    // =====================================
    // GET ID
    // =====================================

    public int getId() {
        return id;
    }


    // =====================================
    // NAME
    // =====================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    // =====================================
    // BRAND
    // =====================================

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    // =====================================
    // DESCRIPTION
    // =====================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    // =====================================
    // PRICE
    // =====================================

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    // =====================================
    // CATEGORY
    // =====================================

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    // =====================================
    // STOCK
    // =====================================

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}