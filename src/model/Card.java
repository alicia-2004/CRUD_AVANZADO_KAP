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
@Table(name = "CARD")
public class Card {
     @Id
    @Column(name = "CARD_NUMBER", length = 24)
    private String cardNumber;
    
    @Column(name = "CVV")
    private int cvv;
    
    @Column(name = "EXPIRATION_DATE")
    @Temporal(TemporalType.DATE)
    private Date expirationDate;


    public Card() {
    }
    
    public Card(String cardNumber, int cvv, Date expirationDate) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expirationDate = expirationDate;
    }

    public String getCardNumber() { return cardNumber; }
    public int getCvv() { return cvv; }
    public Date getExpirationDate() { return expirationDate; }
    
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public void setCvv(int cvv) { this.cvv = cvv; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }

    @Override
    public String toString() {
        return "Card{number=" + cardNumber + ", cvv=" + cvv + ", exp=" + expirationDate + '}';
    }
}
