/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.persistence.*;
/**
 *
 * @author kevin
 */
@Entity
@Table(name = "SHOE")
public class Shoe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    
    @Column(name = "PRICE", nullable = false)
    private double price;
    
    @Column(name = "MODEL", nullable = false, length = 30)
    private String model;
    
    @Column(name = "SIZE", nullable = false)
    private double size;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "EXCLUSIVE", columnDefinition = "ENUM('TRUE', 'FALSE')")
    private Exclusive exclusive;
    
    @Column(name = "MANUFACTER_DATE")
    @Temporal(TemporalType.DATE)
    private java.util.Date manufactorDate;
    
    @Column(name = "COLOR", nullable = false, length = 20)
    private String color;
    
    @Column(name = "ORIGIN", nullable = false, length = 20)
    private String origin;
    
    @Column(name = "BRAND", nullable = false, length = 20)
    private String brand;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "RESERVED", columnDefinition = "ENUM('TRUE', 'FALSE')")
    private Reserved reserved;
    
    @Column(name = "STOCK")
    private int stock;
    
    @Column(name = "IMGPATH", nullable = false, length = 100)
    private String path;

    public Shoe() {}

    public Shoe(double price, String model, double size, Exclusive exclusive, java.util.Date manufactorDate, String color, String origin, String brand, Reserved reserved, int stock ,String path) {
        this.price = price;
        this.model = model;
        this.size = size;
        this.exclusive = exclusive;
        this.manufactorDate = manufactorDate;
        this.color = color;
        this.origin = origin;
        this.brand = brand;
        this.reserved = reserved;
        this.stock = stock;
        this.path = path;
    }


    public Shoe(int id, double price, String model, String brand, String path) {
        this.id = id;
        this.price = price;
        this.model = model;
        this.brand = brand;
        this.path = path;
    }
    

    public int getId() { return id; }
    public double getPrice() { return price; }
    public String getModel() { return model; }
    public double getSize() { return size; }
    public Exclusive getExclusive() { return exclusive; }
    public java.util.Date getManufactorDate() { return manufactorDate; }
    public String getColor() { return color; }
    public String getOrigin() { return origin; }
    public String getBrand() { return brand; }
    public Reserved getReserved() { return reserved; }
    public int getStock() { return stock; }
    public String getImgPath() { return path; }
    
    public void setId(int id) { this.id = id; }
    public void setPrice(double price) { this.price = price; }
    public void setModel(String model) { this.model = model; }
    public void setSize(double size) { this.size = size; }
    public void setExclusive(Exclusive exclusive) { this.exclusive = exclusive; }
    public void setManufactorDate(java.util.Date manufactorDate) { this.manufactorDate = manufactorDate; }
    public void setColor(String color) { this.color = color; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setReserved(Reserved reserved) { this.reserved = reserved; }
    public void setStock(int stock) { this.stock = stock; }
    public void setImgPath(String path) { this.path = path; }
    
    @Override
    public String toString() {
        return "Shoe{id=" + id + ", model=" + model + ", brand=" + brand + ", price=" + price + "}";
    }
}

enum Exclusive {
    TRUE, FALSE
}

enum Reserved {
    TRUE, FALSE
}
