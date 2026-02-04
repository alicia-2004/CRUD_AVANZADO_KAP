/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interface for database operations. Defines all methods needed to work with
 * users, shoes, and orders.
 */
public interface ClassDAO {

    /**
     * Logs in a user or admin.
     *
     * @param username Login username
     * @param password Login password
     * @return Profile if login is correct, null if not
     */
    public Profile logIn(String username, String password);

    /**
     * Creates a new user account.
     *
     * @param gender User's gender
     * @param cardNumber Credit card number
     * @param username Login username
     * @param password Login password
     * @param email Email address
     * @param name First name
     * @param telephone Phone number
     * @param surname Last name
     * @return true if created, false if error
     */
    public Boolean signUp(String gender, String cardNumber, String username,
            String password, String email, String name,
            String telephone, String surname);

    /**
     * Deletes a user's own account.
     *
     * @param username Username to delete
     * @param password Password for verification
     * @return true if deleted, false if error
     */
    public Boolean dropOutUser(String username, String password);

    /**
     * Deletes a user account (by admin).
     *
     * @param usernameToDelete Username to delete
     * @param adminUsername Admin's username
     * @param adminPassword Admin's password
     * @return true if deleted, false if error
     */
    public Boolean dropOutAdmin(String usernameToDelete, String adminUsername,
            String adminPassword);

    /**
     * Updates user information.
     *
     * @param password New password
     * @param email New email
     * @param name New first name
     * @param telephone New phone number
     * @param surname New last name
     * @param username Username to update
     * @param gender New gender
     * @return true if updated, false if error
     */
    public Boolean modificarUser(String password, String email, String name,
            String telephone, String surname,
            String username, String gender);

    /**
     * Processes a shoe purchase with card payment.
     *
     * @param cvv Card CVV code
     * @param numTarjeta Card number
     * @param caducidad Card expiration date
     * @param user The user buying
     * @param shoeId The shoe ID to buy
     * @return true if purchase successful, false if error
     */
    public Boolean checkPayments(String cvv, String numTarjeta,
            LocalDate caducidad, User user, Integer shoeId);

    /**
     * Loads all shoes from database.
     *
     * @return List of all shoes
     */
    public List<Shoe> loadShoes();

    /**
     * Deletes a shoe from database.
     *
     * @param shoe The shoe to delete
     * @return true if deleted, false if error
     */
    public Boolean dropShoe(Shoe shoe);

    /**
     * Updates the stock quantity of a shoe.
     *
     * @param shoe The shoe to update
     * @param stock New stock quantity
     * @return true if updated, false if error
     */
    public Boolean updateStockShoe(Shoe shoe, int stock);

    /**
     * Gets shoes bought by a specific user.
     *
     * @param username User's username
     * @return List of shoes bought by user
     */
    public List<Shoe> getShoesByUser(String username);

    /**
     * Finds shoe variants by characteristics.
     *
     * @param brand Shoe brand
     * @param model Shoe model
     * @param color Shoe color
     * @param origin Country of origin
     * @return List of matching shoes
     */
    public List<Shoe> loadShoeVariants(String brand, String model,
            String color, String origin);

    /**
     * Adds a new shoe to database.
     *
     * @param shoe The shoe to add
     * @return true if added, false if error
     */
    public Boolean addShoe(Shoe shoe);

    /**
     * Loads one shoe per model (for display).
     *
     * @return List of unique shoe models
     */
    public List<Shoe> loadModels();

    /**
     * Gets all usernames for a dropdown menu.
     *
     * @return List of all usernames
     */
    List<String> comboBoxInsert();
}
