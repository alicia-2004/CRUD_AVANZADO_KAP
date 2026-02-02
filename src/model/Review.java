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
@Table(name = "REVIEW")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "ID_SHOE", nullable = false)
    private Shoe shoe;
    
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "USERNAME")
    private User user;
    
    @Column(name = "DESCRIPTION", length = 40)
    private String description;
    
    @Column(name = "RATING", nullable = false)
    private int rating;
    
    @Column(name = "DATE_")
    @Temporal(TemporalType.DATE)
    private Date date;

    public Review() {}

    public Review(Shoe shoe, User user, String description, int rating, Date date) {
        this.shoe = shoe;
        this.user = user;
        this.description = description;
        this.rating = rating;
        this.date = date;
    }

    public int getId() { return id; }
    public Shoe getShoe() { return shoe; }
    public User getUser() { return user; }
    public String getDescription() { return description; }
    public int getRating() { return rating; }
    public Date getDate() { return date; }

    public void setId(int id) { this.id = id; }
    public void setShoe(Shoe shoe) { this.shoe = shoe; }
    public void setUser(User user) { this.user = user; }
    public void setDescription(String description) { this.description = description; }
    public void setRating(int rating) { this.rating = rating; }
    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        return "Review{id=" + id + ", user=" + (user != null ? user.getUsername() : "null") + ", rating=" + rating + "}";
    }
}
