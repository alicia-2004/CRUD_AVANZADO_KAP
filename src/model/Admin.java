/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.persistence.*;

/**
 * Entity that represents an administrator user.
 * <p>
 * Inherits common data from {@link Profile} and adds the current account. It is
 * mapped to the table ADMIN_ in the database.
 * </p>
 *
 * @author Deusto
 */
@Entity
@Table(name = "ADMIN_")
@PrimaryKeyJoinColumn(name = "USERNAME")
public class Admin extends Profile {

    /**
     * Current account associated with the admin.
     */
    @Column(name = "CURRENT_ACCOUNT", length = 40)
    private String currentAccount;

    /**
     * Creates an admin with all profile data.
     */
    public Admin(String currentAccount, String username, String password, String email, String name, String telephone, String surname) {
        super(username, password, email, name, telephone, surname);
        this.currentAccount = currentAccount;
    }

    /**
     * Empty constructor required by JPA.
     */
    public Admin() {
        this.currentAccount = "";
    }

    /**
     * @return the current account
     */
    public String getCurrentAccount() {
        return currentAccount;
    }

    /**
     * @param currentAccount new account value
     */
    public void setCurrentAccount(String currentAccount) {
        this.currentAccount = currentAccount;
    }

    @Override
    public void logIn() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return admin information as text
     */
    @Override
    public String toString() {
        return "Admin{" + "currentAccount=" + currentAccount + '}';
    }
}
