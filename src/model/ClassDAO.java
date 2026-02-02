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
 * Data Access Object interface for database operations.
 * Provides methods to interact with user and admin records in the database.
 */
public interface ClassDAO {

    public Profile logIn(String username, String password);
    public Boolean signUp(String gender, String cardNumber, String username, String password, String email, String name, String telephone, String surname);
    public Boolean dropOutUser(String username, String password);
    public Boolean dropOutAdmin(String usernameToDelete, String adminUsername, String adminPassword);
    public Boolean modificarUser (String password, String email, String name, String telephone, String surname, String username, String gender);
    public Boolean checkPayments(String cvv, String numTarjeta, Date caducidad, String username);
    public List<Shoe> loadShoes();
    public Boolean dropShoe(Shoe shoe);
    public Boolean updateStockShoe (Shoe shoe, int stock);
    public List<Shoe> getShoesByUser(String username);
    public List<Shoe> loadShoeVariants(String brand, String model, String color, String origin);
    public Boolean addShoe(Shoe shoe);
    public List<Shoe> loadModels();

  
    List comboBoxInsert();
}
