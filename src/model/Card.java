/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity that represents a credit/debit card.
 * <p>
 * Stores basic payment information such as card number, CVV and expiration
 * date. It is mapped to the CARD table.
 * </p>
 *
 * @author dami
 */
@Entity
@Table(name = "CARD")
public class Card {

    /**
     * Unique card number (primary key).
     */
    @Id
    @Column(name = "CARD_NUMBER", length = 24)
    private String cardNumber;
    /**
     * Security code of the card.
     */
    @Column(name = "CVV")
    private int cvv;
    /**
     * Card expiration date.
     */
    @Column(name = "EXPIRATION_DATE")
    @Temporal(TemporalType.DATE)
    private Date expirationDate;

    /**
     * Empty constructor required by JPA.
     */
    public Card() {
    }

    /**
     * Creates a card with all its data.
     *
     * @param cardNumber card number
     * @param cvv security code
     * @param expirationDate expiration date
     */
    public Card(String cardNumber, int cvv, Date expirationDate) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expirationDate = expirationDate;
    }

    /**
     * @return the card number
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * @return the CVV code
     */
    public int getCvv() {
        return cvv;
    }

    /**
     * @return the expiration date
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param cardNumber new card number
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @param cvv new CVV code
     */
    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    /**
     * @param expirationDate new expiration date
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return card information as text
     */
    @Override
    public String toString() {
        return "Card{number=" + cardNumber + ", cvv=" + cvv + ", exp=" + expirationDate + '}';
    }
}
