/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.persistence.*;

/**
 * Base class for all user profiles in the system. This class stores common
 * information that all profiles have. Other profile types (like User, Admin)
 * extend this class.
 */
@Entity
@Table(name = "PROFILE_")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Profile {

    /**
     * The username for logging in. This is the primary key in the database.
     */
    @Id
    @Column(name = "USERNAME", length = 40)
    private String username;

    /**
     * The password for the account.
     */
    @Column(name = "PASSWORD_", length = 40)
    private String password;

    /**
     * The email address of the profile. Each email must be unique in the
     * database.
     */
    @Column(name = "EMAIL", length = 40, unique = true)
    private String email;

    /**
     * The first name of the person.
     */
    @Column(name = "NAME_", length = 40)
    private String name;

    /**
     * The phone number of the person.
     */
    @Column(name = "TELEPHONE", length = 9)
    private String telephone;

    /**
     * The last name of the person.
     */
    @Column(name = "SURNAME", length = 40)
    private String surname;

    /**
     * Creates a new Profile with all information.
     *
     * @param username The login username
     * @param password The account password
     * @param email The email address
     * @param name The first name
     * @param telephone The phone number
     * @param surname The last name
     */
    public Profile(String username, String password, String email,
            String name, String telephone, String surname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.telephone = telephone;
        this.surname = surname;
    }

    /**
     * Creates an empty Profile with default values.
     */
    public Profile() {
        this.username = "";
        this.password = "";
        this.email = "";
        this.name = "";
        this.telephone = "";
        this.surname = "";
    }

    /**
     * Gets the username.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password.
     *
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the email address.
     *
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the first name.
     *
     * @return The first name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the phone number.
     *
     * @return The phone number
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Gets the last name.
     *
     * @return The last name
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the username.
     *
     * @param username The new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password.
     *
     * @param password The new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the email address.
     *
     * @param email The new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the first name.
     *
     * @param name The new first name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the phone number.
     *
     * @param telephone The new phone number
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Sets the last name.
     *
     * @param surname The new last name
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Returns a string with all profile information.
     *
     * @return String with profile details
     */
    @Override
    public String toString() {
        return "Profile{" + "username=" + username + ", password=" + password
                + ", email=" + email + ", name=" + name + ", telephone=" + telephone
                + ", surname=" + surname + '}';
    }

    /**
     * Method for logging into the system. Each type of profile must define its
     * own login logic.
     */
    public abstract void logIn();
}
