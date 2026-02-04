package model;

import java.time.LocalDate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import threads.HiloConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;

/**
 * Implementation of ClassDAO using Hibernate ORM. Handles all database
 * interactions for users and admins. Provides login, signup, deletion,
 * modification, and retrieval of usernames.
 *
 * @author kevin
 */
public class HibernateDBImplementation implements ClassDAO {

    /**
     * Logs in a user or admin from the database using Hibernate.
     */
    @Override
    public Profile logIn(String username, String password) {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();

        try {
            Session session = waitForHibernateSession(connectionThread);

            User user = session.get(User.class, username);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }

            Admin admin = session.get(Admin.class, username);
            if (admin != null && admin.getPassword().equals(password)) {
                return admin;
            }

        } catch (Exception e) {
            System.out.println("Database query error with Hibernate");
            e.printStackTrace();
        } finally {
            connectionThread.releaseConnection();
        }
        return null;
    }

    /**
     * Signs up a new user in the database using Hibernate.
     */
    @Override
    public Boolean signUp(String gender, String cardNumber, String username,
            String password, String email, String name,
            String telephone, String surname) {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();
        Transaction tx = null;

        try {
            Session session = waitForHibernateSession(connectionThread);
            tx = session.beginTransaction();

            User user = new User(gender, cardNumber, username, password, email,
                    name, telephone, surname);

            session.save(user);
            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Database error on signup with Hibernate");
            e.printStackTrace();
            return false;
        } finally {
            connectionThread.releaseConnection();
        }
    }

    /**
     * Deletes a standard user from the database using Hibernate.
     */
    @Override
    public Boolean dropOutUser(String username, String password) {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();
        Transaction tx = null;

        try {
            Session session = waitForHibernateSession(connectionThread);

            User user = session.get(User.class, username);
            if (user == null || !user.getPassword().equals(password)) {
                return false;
            }

            tx = session.beginTransaction();
            session.delete(user);
            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Database error on deleting user with Hibernate");
            e.printStackTrace();
            return false;
        } finally {
            connectionThread.releaseConnection();
        }
    }

    /**
     * Deletes a user selected by admin from the database using Hibernate.
     */
    @Override
    public Boolean dropOutAdmin(String usernameToDelete, String adminUsername, String adminPassword) {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();
        Transaction tx = null;

        try {
            Session session = waitForHibernateSession(connectionThread);

            Admin admin = session.get(Admin.class, adminUsername);
            if (admin == null || !admin.getPassword().equals(adminPassword)) {
                return false;
            }
            Profile profileToDelete = session.get(User.class, usernameToDelete);
            if (profileToDelete == null) {
                profileToDelete = session.get(Admin.class, usernameToDelete);
            }

            if (profileToDelete == null) {
                return false;
            }

            tx = session.beginTransaction();
            session.delete(profileToDelete);
            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Database error on deleting admin with Hibernate");
            e.printStackTrace();
            return false;
        } finally {
            connectionThread.releaseConnection();
        }
    }

    /**
     * Modifies the information of a user in the database using Hibernate.
     */
    @Override
    public Boolean modificarUser(String password, String email, String name,
            String telephone, String surname, String username,
            String gender) {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();
        Transaction tx = null;

        try {
            Session session = waitForHibernateSession(connectionThread);

            User user = session.get(User.class, username);
            if (user == null) {
                System.out.println("Usuario no encontrado en la base de datos");
                return false;
            }

            tx = session.beginTransaction();

            user.setPassword(password);
            user.setEmail(email);
            user.setName(name);
            user.setTelephone(telephone);
            user.setSurname(surname);
            user.setGender(gender);

            session.update(user);
            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Database error on modifying user with Hibernate");
            e.printStackTrace();
            return false;
        } finally {
            connectionThread.releaseConnection();
        }
    }

    /**
     * Retrieves a list of usernames from the database using Hibernate.
     */
    @Override
    public List<String> comboBoxInsert() {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();
        List<String> listaUsuarios = new ArrayList<>();

        try {
            Session session = waitForHibernateSession(connectionThread);

            String hql = "SELECT u.username FROM User u";
            Query<String> query = session.createQuery(hql, String.class);
            listaUsuarios = query.list();

        } catch (Exception e) {
            System.out.println("Database error on retrieving usernames with Hibernate");
            e.printStackTrace();
        } finally {
            connectionThread.releaseConnection();
        }
        return listaUsuarios;
    }

    /**
     * Waits for a Hibernate Session from a HiloConnection thread.
     */
    private Session waitForHibernateSession(HiloConnection thread) throws InterruptedException {
        int attempts = 0;
        while (!thread.isReady() && attempts < 50) {
            Thread.sleep(10);
            attempts++;
        }

        return thread.getConnection();
    }

    /**
     * Generates a new order ID.
     */
    private Long generateNewOrderId(Session session) {

        String hql = "SELECT MAX(o.orderId) FROM Order o";
        Query<Long> query = session.createQuery(hql, Long.class);
        Long maxId = query.uniqueResult();

        if (maxId == null) {
            return 1L;
        }
        return maxId + 1;

    }

    /**
     * Loads shoes from the database.
     */
    @Override
    public List<Shoe> loadShoes() {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();
        List<Shoe> mapShoe = new ArrayList<>();

        try {
            Session session = waitForHibernateSession(connectionThread);

            String hql = "FROM Shoe";

            Query<Shoe> query = session.createQuery(hql, Shoe.class);

            mapShoe = query.list();
            System.out.println("Hace la peticion" + mapShoe);

        } catch (InterruptedException ex) {
            Logger.getLogger(HibernateDBImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapShoe;
    }

    /**
     * Loads models from the database.
     */
    @Override
    public List<Shoe> loadModels() {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();
        List<Shoe> mapShoe = new ArrayList<>();

        try {
            Session session = waitForHibernateSession(connectionThread);

            String hql = "SELECT s FROM Shoe s WHERE s.id IN (SELECT MIN(s2.id) FROM Shoe s2 GROUP BY s2.model)";

            Query<Shoe> query = session.createQuery(hql, Shoe.class);

            mapShoe = query.list();
            System.out.println("Hace la peticion" + mapShoe);

        } catch (InterruptedException ex) {
            Logger.getLogger(HibernateDBImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mapShoe;
    }

    /**
     * Drops a shoe from the database.
     */
    @Override
    public Boolean dropShoe(Shoe shoe) {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();
        Transaction tx = null;
        int idShoe = shoe.getId();

        try {
            Session session = waitForHibernateSession(connectionThread);

            tx = session.beginTransaction();
            Shoe shoeToDelete = session.get(Shoe.class, shoe.getId());
            if (shoeToDelete != null) {
                session.delete(shoeToDelete);
            }

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Database error on deleting shoe with Hibernate");
            e.printStackTrace();
            return false;
        } finally {
            connectionThread.releaseConnection();
        }

    }

    /**
     * Updates stock of a shoe.
     */
    @Override
    public Boolean updateStockShoe(Shoe shoe, int stock) {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();
        Transaction tx = null;

        try {
            Session session = waitForHibernateSession(connectionThread);
            tx = session.beginTransaction();

            shoe.setStock(stock);
            session.update(shoe);
            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Database error on modifying user with Hibernate");
            e.printStackTrace();
            return false;
        } finally {
            connectionThread.releaseConnection();
        }
    }

    /**
     * Gets shoes by user.
     */
    @Override
    public List<Shoe> getShoesByUser(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Loads shoe variants.
     */
    @Override
    public List<Shoe> loadShoeVariants(String brand, String model, String color, String origin) {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();

        List<Shoe> result = new ArrayList<>();

        try {
            Session session = waitForHibernateSession(connectionThread);
            String hql = "FROM Shoe s "
                    + "WHERE s.brand = :brand AND s.model = :model "
                    + "AND s.color = :color AND s.origin = :origin "
                    + "ORDER BY s.size ASC";

            Query<Shoe> query = session.createQuery(hql, Shoe.class);
            query.setParameter("brand", brand);
            query.setParameter("model", model);
            query.setParameter("color", color);
            query.setParameter("origin", origin);

            result = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionThread.releaseConnection();
        }
        return result;
    }

    /**
     * Adds a shoe to the database.
     */
    @Override
    public Boolean addShoe(Shoe shoe) {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();
        Transaction tx = null;

        try {
            Session session = waitForHibernateSession(connectionThread);
            tx = session.beginTransaction();

            session.save(shoe);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;

        } finally {
            connectionThread.releaseConnection();
        }
    }

    /**
     * Checks payments.
     */
    @Override
    public Boolean checkPayments(String cvv, String numTarjeta, LocalDate caducidad,
            User user, Integer shoeId) {

        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();
        String username = user.getUsername();

        Transaction tx = null;
        try {
            Session session = waitForHibernateSession(connectionThread);
            tx = session.beginTransaction();

            String hqlUser = "SELECT u.cardNumber FROM User u WHERE u.username = :username";
            Query<String> userQuery = session.createQuery(hqlUser, String.class);
            userQuery.setParameter("username", username);

            String userCardNumber = userQuery.uniqueResult();

            if (userCardNumber == null || !userCardNumber.equals(numTarjeta)) {
                tx.rollback();
                return false;
            }

            java.sql.Date sqlCaducidad = java.sql.Date.valueOf(caducidad);
            String hqlCard = "FROM Card c WHERE c.cardNumber = :numTarjeta AND c.cvv = :cvv AND c.expirationDate = :caducidad";
            Query<Card> cardQuery = session.createQuery(hqlCard, Card.class);
            cardQuery.setParameter("numTarjeta", numTarjeta);
            cardQuery.setParameter("cvv", Integer.parseInt(cvv));
            cardQuery.setParameter("caducidad", sqlCaducidad);

            try {
                cardQuery.getSingleResult();
            } catch (NoResultException e) {
                tx.rollback();
                return false;
            }

            String hqlShoe = "FROM Shoe s WHERE s.id = :shoeId";
            Query<Shoe> shoeQuery = session.createQuery(hqlShoe, Shoe.class);
            shoeQuery.setParameter("shoeId", shoeId);

            Shoe shoe = shoeQuery.uniqueResult();

            if (shoe == null || shoe.getStock() < 1) {
                tx.rollback();
                return false;
            }

            int nuevoStock = shoe.getStock() - 1;
            shoe.setStock(nuevoStock);

            if (nuevoStock == 0) {
                session.delete(shoe);
            } else {
                session.update(shoe);
            }

            Order newOrder = new Order();

            newOrder.setDate(new java.sql.Date(System.currentTimeMillis()));
            newOrder.setQuantity(1);
            newOrder.setShoe(shoe);
            newOrder.setUser(user);

            session.save(newOrder);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            connectionThread.releaseConnection();
        }
    }

}
