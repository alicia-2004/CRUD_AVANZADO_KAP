package model;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity that represents a purchase order.
 * <p>
 * Stores the user who made the order, the shoe purchased, the date of the order
 * and the quantity. It is mapped to the ORDER_ table.
 * </p>
 *
 * @author Kevin
 */
@Entity
@Table(name = "ORDER_")
public class Order {

    /**
     * Order identifier (primary key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private int orderId;

    /**
     * User who made the order.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "USERNAME")
    private User user;

    /**
     * Shoe purchased in the order.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "SHOE_ID")
    private Shoe shoe;

    /**
     * Date when the order was created.
     */
    @Column(name = "DATE_", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    /**
     * Number of items purchased.
     */
    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    /**
     * Empty constructor required by JPA.
     */
    public Order() {
    }

    /**
     * Creates an order with all required data.
     *
     * @param user user who makes the order
     * @param shoe shoe purchased
     * @param date order date
     * @param quantity number of units
     */
    public Order(User user, Shoe shoe, Date date, int quantity) {
        this.user = user;
        this.shoe = shoe;
        this.date = date;
        this.quantity = quantity;
    }

    /**
     * @return order id
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * @return shoe
     */
    public Shoe getShoe() {
        return shoe;
    }

    /**
     * @return order date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param orderId new id
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * @param user new user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @param shoe new shoe
     */
    public void setShoe(Shoe shoe) {
        this.shoe = shoe;
    }

    /**
     * @param date new date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @param quantity new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return order information as text
     */
    @Override
    public String toString() {
        return "Order{id=" + orderId + ", user="
                + (user != null ? user.getUsername() : "null")
                + ", quantity=" + quantity + "}";
    }
}
