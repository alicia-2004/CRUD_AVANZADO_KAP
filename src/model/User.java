/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.persistence.*;

/**
 * A normal user in the system (customer). Extends Profile and adds gender and
 * credit card information. This is for regular customers who can buy shoes.
 */
@Entity
@Table(name = "USER_")
@PrimaryKeyJoinColumn(name = "USERNAME")
public class User extends Profile {

    /**
     * The gender of the user (male, female, other). Maximum 40 characters.
     */
    @Column(name = "GENDER", length = 40)
    private String gender;

    /**
     * The credit card number for payments. Maximum 24 characters.
     */
    @Column(name = "CARD_NUMBER", length = 24)
    private String cardNumber;

    /**
     * Creates a new user with all information.
     *
     * @param gender User's gender
     * @param cardNumber Credit card number
     * @param username Login username
     * @param password Login password
     * @param email Email address
     * @param name First name
     * @param telephone Phone number
     * @param surname Last name
     */
    public User(String gender, String cardNumber, String username, String password,
            String email, String name, String telephone, String surname) {
        super(username, password, email, name, telephone, surname);
        this.gender = gender;
        this.cardNumber = cardNumber;
    }

    /**
     * Creates an empty user with default values.
     */
    public User() {
        super();
        this.gender = "";
        this.cardNumber = null;
    }

    // GETTERS
    /**
     * @return The user's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @return The credit card number
     */
    public String getCardNumber() {
        return cardNumber;
    }

    // SETTERS
    /**
     * @param gender The new gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @param cardNumber The new credit card number
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Handles user login. Currently throws an exception - needs implementation.
     *
     * @throws UnsupportedOperationException Login not implemented yet
     */
    @Override
    public void logIn() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns a string with user-specific information.
     *
     * @return String with gender and card number
     */
    @Override
    public String toString() {
        return "User{" + "gender=" + gender + ", cardNumber=" + cardNumber + '}';
    }
}
