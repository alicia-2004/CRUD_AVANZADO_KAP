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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

    private static void crearTablasHibernate() {
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

}
