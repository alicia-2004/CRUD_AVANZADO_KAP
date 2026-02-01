package main;

import controller.Controller;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Admin;
import model.Card;
import model.HibernateUtil;
import model.Order;
import model.Profile;
import model.Review;
import model.Shoe;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utilities.Utilities;

public class Main extends Application {

    /**
     * Starts the JavaFX application by loading the login window.
     *
     * @param stage the primary stage for this application
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LogInWindow.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Login Application");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        crearTablasHibernate();
        launch(args);
    }

    public static void crearTablasHibernate() {
        System.out.println("Inicializando Hibernate");
        try {
            // Solo abrir y cerrar SessionFactory crea las tablas
            SessionFactory sessionFactory = model.HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            session.close();

            System.out.println("Tablas creadas/validadas");
        } catch (Exception e) {
            System.err.println("Error al crear tablas: " + e.getMessage());
            // Decidir si continuar o salir
            System.exit(1);  // Sale si no puede crear tablas
        }
    }
    
    public static void insertarDatosPrueba() {
    System.out.println("=== INSERTANDO DATOS DE PRUEBA EXACTOS ===");
    
    Session session = null;
    Transaction tx = null;
    
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // ========== LIMPIAR DATOS EN ORDEN CORRECTO ==========
            System.out.println("Limpiando datos existentes...");

            // 1. Borrar Orders (depende de User y Shoe)
            session.createNativeQuery("DELETE FROM ORDER_").executeUpdate();

            // 2. Borrar Reviews (depende de User y Shoe)
            session.createNativeQuery("DELETE FROM REVIEW").executeUpdate();

            // 3. Borrar Admins y Users usando SQL nativo (evita problemas JOINED)
            session.createNativeQuery("DELETE FROM ADMIN_").executeUpdate();
            session.createNativeQuery("DELETE FROM USER_").executeUpdate();

            // 4. Borrar Profiles (después de borrar las subclases)
            session.createNativeQuery("DELETE FROM PROFILE_").executeUpdate();

            // 5. Borrar Shoes (independiente)
            session.createNativeQuery("DELETE FROM SHOE").executeUpdate();

            // 6. Borrar Cards (independiente)
            session.createNativeQuery("DELETE FROM CARD").executeUpdate();

            System.out.println("Datos limpiados correctamente");
        
        // ========== 1. CARDS ==========
        Card card1 = new Card();
        card1.setCardNumber("AA1234567890123456789012");
        card1.setCvv(123);
        card1.setExpirationDate(java.sql.Date.valueOf("2027-05-31"));
        session.save(card1);
        
        Card card2 = new Card();
        card2.setCardNumber("BB0000000000000000000001");
        card2.setCvv(9);
        card2.setExpirationDate(java.sql.Date.valueOf("2026-12-31"));
        session.save(card2);
        
        Card card3 = new Card();
        card3.setCardNumber("CC9876543210987654321012");
        card3.setCvv(600);
        card3.setExpirationDate(java.sql.Date.valueOf("2030-01-15"));
        session.save(card3);
        
        // ========== 2. SHOES ==========
        Shoe shoe1 = new Shoe();
        shoe1.setPrice(79.99);
        shoe1.setModel("Air Max");
        shoe1.setSize(42);
        shoe1.setExclusive(model.Exclusive.FALSE);
        shoe1.setManufactorDate(java.sql.Date.valueOf("2024-01-10"));
        shoe1.setColor("Negro");
        shoe1.setOrigin("España");
        shoe1.setBrand("Nike");
        shoe1.setReserved(model.Reserved.FALSE);
        shoe1.setStock(50);
        shoe1.setImgPath("nike_airmax90_negras.jpg");
        session.save(shoe1);
        
        Shoe shoe2 = new Shoe();
        shoe2.setPrice(120.50);
        shoe2.setModel("Samba");
        shoe2.setSize(40);
        shoe2.setExclusive(model.Exclusive.TRUE);
        shoe2.setManufactorDate(java.sql.Date.valueOf("2023-11-05"));
        shoe2.setColor("Negro");
        shoe2.setOrigin("Italia");
        shoe2.setBrand("Adidas");
        shoe2.setReserved(model.Reserved.FALSE);
        shoe2.setStock(30);
        shoe2.setImgPath("adidas_samba_negras.jpg");
        session.save(shoe2);
        
        Shoe shoe3 = new Shoe();
        shoe3.setPrice(95.00);
        shoe3.setModel("Caven");
        shoe3.setSize(41);
        shoe3.setExclusive(model.Exclusive.FALSE);
        shoe3.setManufactorDate(java.sql.Date.valueOf("2024-02-20"));
        shoe3.setColor("Azul");
        shoe3.setOrigin("Portugal");
        shoe3.setBrand("Puma");
        shoe3.setReserved(model.Reserved.TRUE);
        shoe3.setStock(10);
        shoe3.setImgPath("puma_caven_azul.jpg");
        session.save(shoe3);
        
        // ========== 3. USERS ==========
        User user1 = new User();
        user1.setUsername("jlopez");
        user1.setPassword("pass123");
        user1.setEmail("jlopez@example.com");
        user1.setName("Juan");
        user1.setTelephone("987654321");
        user1.setSurname("Lopez");
        user1.setGender("Masculino");
        user1.setCardNumber("AA1234567890123456789012");
        session.save(user1);
        
        User user2 = new User();
        user2.setUsername("mramirez");
        user2.setPassword("pass456");
        user2.setEmail("mramirez@example.com");
        user2.setName("Maria");
        user2.setTelephone("912345678");
        user2.setSurname("Ramirez");
        user2.setGender("Femenino");
        user2.setCardNumber("BB0000000000000000000001");
        session.save(user2);
        
        User user3 = new User();
        user3.setUsername("cperez");
        user3.setPassword("pass789");
        user3.setEmail("cperez@example.com");
        user3.setName("Carlos");
        user3.setTelephone("934567890");
        user3.setSurname("Perez");
        user3.setGender("Masculino");
        user3.setCardNumber("CC9876543210987654321012");
        session.save(user3);
        
        User userTest = new User();
        userTest.setUsername("test");
        userTest.setPassword("test");
        userTest.setEmail("test@example.com");
        userTest.setName("test");
        userTest.setTelephone("123456789");
        userTest.setSurname("test");
        userTest.setGender("Masculino");
        userTest.setCardNumber("AA1234567890123456789012");
        session.save(userTest);
        
        // ========== 4. ADMINS ==========
        Admin admin1 = new Admin();
        admin1.setUsername("asanchez");
        admin1.setPassword("qwerty");
        admin1.setEmail("asanchez@example.com");
        admin1.setName("Ana");
        admin1.setTelephone("900112233");
        admin1.setSurname("Sanchez");
        admin1.setCurrentAccount("CTA-001");
        session.save(admin1);
        
        Admin admin2 = new Admin();
        admin2.setUsername("rluna");
        admin2.setPassword("zxcvbn");
        admin2.setEmail("rluna@example.com");
        admin2.setName("Rosa");
        admin2.setTelephone("955667788");
        admin2.setSurname("Luna");
        admin2.setCurrentAccount("CTA-002");
        session.save(admin2);
        
        // ========== 5. REVIEWS ==========
        Review review1 = new Review();
        review1.setShoe(shoe1);
        review1.setUser(user1);
        review1.setDescription("Muy cómodas");
        review1.setRating(5);
        review1.setDate(java.sql.Date.valueOf("2024-03-01"));
        session.save(review1);
        
        Review review2 = new Review();
        review2.setShoe(shoe2);
        review2.setUser(user2);
        review2.setDescription("Buen diseño");
        review2.setRating(4);
        review2.setDate(java.sql.Date.valueOf("2024-03-05"));
        session.save(review2);
        
        Review review3 = new Review();
        review3.setShoe(shoe3);
        review3.setUser(user3);
        review3.setDescription("Algo ajustadas");
        review3.setRating(3);
        review3.setDate(java.sql.Date.valueOf("2024-03-10"));
        session.save(review3);
        
        // ========== 6. ORDERS ==========
        Order order1 = new Order();
        order1.setUser(user1);
        order1.setShoe(shoe1);
        order1.setDate(java.sql.Date.valueOf("2024-03-15"));
        order1.setQuantity(2);
        session.save(order1);
        
        Order order2 = new Order();
        order2.setUser(user2);
        order2.setShoe(shoe2);
        order2.setDate(java.sql.Date.valueOf("2024-03-16"));
        order2.setQuantity(1);
        session.save(order2);
        
        Order order3 = new Order();
        order3.setUser(user3);
        order3.setShoe(shoe3);
        order3.setDate(java.sql.Date.valueOf("2024-03-17"));
        order3.setQuantity(1);
        session.save(order3);
        
        tx.commit();
        System.out.println("=== DATOS DE PRUEBA INSERTADOS EXITOSAMENTE ===");
        System.out.println("  - 3 Cards");
        System.out.println("  - 3 Shoes");
        System.out.println("  - 4 Users (1 test)");
        System.out.println("  - 2 Admins");
        System.out.println("  - 3 Reviews");
        System.out.println("  - 3 Orders");
        
    } catch (Exception e) {
        if (tx != null) tx.rollback();
        System.err.println("Error insertando datos de prueba: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("No se pudieron insertar datos de prueba", e);
    } finally {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }
}
        
    
}
