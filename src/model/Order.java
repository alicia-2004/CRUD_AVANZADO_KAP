/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import javax.persistence.*;
import java.util.Date;
/**
 *
 * @author kevin
 */
@Entity
@Table(name = "ORDER_")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private int orderId;
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "USERNAME")
    private User user;
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "SHOE_ID")
    private Shoe shoe;
    
    @Column(name = "DATE_", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;
    
    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    public Order() {}

    public Order(User user, Shoe shoe, Date date, int quantity) {
        this.user = user;
        this.shoe = shoe;
        this.date = date;
        this.quantity = quantity;
    }

    public int getOrderId() { return orderId; }
    public User getUser() { return user; }
    public Shoe getShoe() { return shoe; }
    public Date getDate() { return date; }
    public int getQuantity() { return quantity; }

    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setUser(User user) { this.user = user; }
    public void setShoe(Shoe shoe) { this.shoe = shoe; }
    public void setDate(Date date) { this.date = date; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "Order{id=" + orderId + ", user=" + (user != null ? user.getUsername() : "null") + ", quantity=" + quantity + "}";
    }
}
