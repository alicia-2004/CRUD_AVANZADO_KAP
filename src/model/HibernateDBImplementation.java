package model;

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

            // Buscar User
            User user = session.get(User.class, username);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }

            // Si no es User, buscar Admin
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
                    name, telephone, surname); // userCode lo genera la BD

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

            // Verificar password primero
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

            // Verificar que el admin existe y password es correcto
            Admin admin = session.get(Admin.class, adminUsername);
            if (admin == null || !admin.getPassword().equals(adminPassword)) {
                return false;
            }

            // Buscar el usuario a eliminar (puede ser User o Admin)
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

            // Actualizar campos
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
        // Asumo que tu HiloConnection puede proporcionar una Session de Hibernate
        // Si no, necesitarías adaptar HiloConnection para Hibernate
        return thread.getConnection();
    }

    @Override
    public Boolean checkPayments(String cvv, String numTarjeta, Date caducidad, String username) {

        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();

        try {
            Session session = waitForHibernateSession(connectionThread);

            String hql = "SELECT c FROM Card c, User u WHERE u.username = :username AND u.cardNumber = c.cardNumber AND c.cardNumber = :numTarjetaAND c.cvv = :cvv AND c.expirationDate = :caducidad";
            Query<Card> query = session.createQuery(hql, Card.class);
            query.setParameter("username", username);
            query.setParameter("numTarjeta", numTarjeta);
            query.setParameter("cvv", cvv);
            query.setParameter("caducidad", caducidad);

            return !query.list().isEmpty();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            connectionThread.releaseConnection();
        }
    }

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

    @Override
    public Boolean dropShoe(Shoe shoe) {
        HiloConnection connectionThread = new HiloConnection(30);
        connectionThread.start();
        Transaction tx = null;
        int idShoe = shoe.getId();

        try {
            Session session = waitForHibernateSession(connectionThread);

            // Verificar password primero
            //Shoe shoe = session.get(Shoe.class, idShoe);
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

    @Override
    public List<Shoe> getShoesByUser(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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

}
