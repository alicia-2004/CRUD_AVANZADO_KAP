/**
 * Controlador para la ventana de detalles del zapato.
 * Gestiona la visualización de información detallada de un producto,
 * permite seleccionar tallas y navegar a la ventana de compra.
 * 
 * @author Pablo
 * @version 1.0
 */
package view;

import controller.Controller;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // AÑADIR ESTE IMPORT
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.PersonalLogger;
import model.Profile;
import model.Shoe;

/**
 * Controlador de la ventana que muestra los detalles completos de un zapato.
 * Se accede desde la página principal al hacer clic en un producto.
 */
public class ShoeDetailController {

    @FXML
    private ImageView imgShoe;
    @FXML
    private Label lblName;
    @FXML
    private Label lblSubtitle;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblStock;
    @FXML
    private ComboBox<Double> cmbSize;
    
    @FXML
    private Button buyProductButton;
    
    private PersonalLogger personalLogger = new PersonalLogger(); // INICIALIZAR
    private Controller cont;
    private Profile profile;
    private Shoe shoe;
    private java.util.List<Shoe> variants = new java.util.ArrayList<>();
    private Shoe selectedVariant;
    @FXML
    private HBox shoeDetailWindow;
    @FXML
    private MenuBar menu;
    @FXML
    private Menu menuHome;
    @FXML
    private Menu menuActions;
    @FXML
    private MenuItem menuReport;
    @FXML
    private MenuItem menuSettings;
    @FXML
    private Menu menuHelp;
    @FXML
    private TextField searchTextField;
    @FXML
    private ImageView imgFilter;
    @FXML
    private Label lblFIlter;
    @FXML
    private Button btnBack;
    @FXML
    private MenuItem context; // MenuItem del menú contextual

    /**
     * Método de inicialización - cambiar el texto del menú contextual
     */
    @FXML
    public void initialize() {
        // Cambiar el texto del menú contextual a "Detalles"
        if (context != null) {
            context.setText("Detalles");
        }
    }

    /**
     * Establece el controlador principal.
     * @param cont Controlador de la aplicación
     */
    public void setCont(Controller cont) {
        this.cont = cont;
    }

    /**
     * Establece el usuario actual.
     * @param profile Perfil del usuario
     */
    public void setUser(Profile profile) {
        this.profile = profile;
    }

    /**
     * Configura el zapato a mostrar y carga sus variantes.
     * @param shoe Zapato seleccionado
     */
    public void setShoe(Shoe shoe) {
        this.shoe = shoe;

        variants = cont.loadShoeVariants(shoe);
        paintBaseInfo(shoe);

        fillSizesComboAndSelectInitial(shoe.getSize());
    }

    /**
     * Vuelve a la página principal del usuario.
     * @param event Evento del botón
     */
    @FXML
    private void onBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("MainPageUser.fxml")
            );
            Parent root = loader.load();

            MainPageUserController mainCtrl = loader.getController();
            mainCtrl.setCont(cont);
            mainCtrl.setUser(profile);
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la información básica del zapato.
     * @param s Zapato a mostrar
     */
    private void paintBaseInfo(Shoe s) {
        if (s == null) {
            return;
        }

        lblName.setText(s.getBrand() + " " + s.getModel());
        lblSubtitle.setText(s.getColor() + " (" + s.getOrigin() + ")");
        lblPrice.setText(String.format("€%.2f", s.getPrice()));

        setImageSafe("/images/" + s.getImgPath());
    }

    /**
     * Rellena el combo de tallas y selecciona la inicial.
     * @param initialSize Talla inicial a seleccionar
     */
    private void fillSizesComboAndSelectInitial(double initialSize) {
        cmbSize.getItems().clear();

        for (Shoe s : variants) {
            Double size = s.getSize();
            if (!cmbSize.getItems().contains(size)) {
                cmbSize.getItems().add(size);
            }
        }

        cmbSize.setOnAction(e -> {
            Double size = cmbSize.getValue();
            if (size != null) {
                selectVariantBySize(size);
            }
        });
        cmbSize.getSelectionModel().select(initialSize);
        selectVariantBySize(initialSize);
    }

    /**
     * Selecciona una variante según la talla.
     * @param size Talla a buscar
     */
    private void selectVariantBySize(double size) {
        selectedVariant = null;

        for (Shoe s : variants) {
            if (Double.compare(s.getSize(), size) == 0) {
                selectedVariant = s;
                break;
            }
        }

        if (selectedVariant == null) {
            lblStock.setText("No stock info");
            return;
        }

        lblStock.setText(selectedVariant.getStock() + " Units in stock");
    }

    /**
     * Carga la imagen del zapato, usa una por defecto si falla.
     * @param path Ruta de la imagen
     */
    private void setImageSafe(String path) {
        try {
            Image img = new Image(getClass().getResourceAsStream(path));
            imgShoe.setImage(img);
        } catch (Exception e) {
            Image imgDefault = new Image(getClass().getResource("/images/default_img.jpg").toExternalForm());
            imgShoe.setImage(imgDefault);
        }
    }
    
    /**
 * Abre el menú contextual para mostrar detalles de la zapatilla.
 * Se activa al hacer clic derecho en la ventana.
 * @param event Evento del menú
 */
    @FXML
    private void openMenuContext(ActionEvent event) {
        if (shoe == null) {
            System.out.println("No hay zapatilla seleccionada");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles de la zapatilla");
        alert.setHeaderText(shoe.getBrand() + " " + shoe.getModel());

        String info = "Marca: " + shoe.getBrand() + "\n" +
                     "Modelo: " + shoe.getModel() + "\n" +
                     "Color: " + shoe.getColor() + "\n" +
                     "Origen: " + shoe.getOrigin() + "\n" +
                     "Precio: " + String.format("€%.2f", shoe.getPrice()) + "\n" +
                     "Talla: " + shoe.getSize() + "\n" +
                     "Stock disponible: " + (selectedVariant != null ? selectedVariant.getStock() : shoe.getStock()) + " unidades";

        alert.setContentText(info);

        alert.setWidth(400);
        alert.setHeight(350);

        alert.showAndWait();
    }
    
    /**
     * Abre el manual de usuario en PDF.
     * @param event Evento del menú
     */
    @FXML
    private void openReport(ActionEvent event) {
        try {
            File pdf = new File("pdfs/MANUAL_DE_USUARIO.pdf");
            if (!pdf.exists()) {
                System.out.println("No existe el PDF: " + pdf.getAbsolutePath());
                return;
            }
            Desktop.getDesktop().open(pdf);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre el informe en PDF.
     * @param event Evento del menú
     */
    @FXML
    private void openManual(ActionEvent event) {
        try {
            File pdf = new File("pdfs/INFORME DIN.pdf");
            if (!pdf.exists()) {
                System.out.println("No existe el PDF: " + pdf.getAbsolutePath());
                return;
            }
            Desktop.getDesktop().open(pdf);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de pago para comprar el producto.
     * @param event Evento del botón
     */
    @FXML
    private void buyProduct(ActionEvent event) {
       try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PaymentWindowFXML.fxml"));
                        Parent root = fxmlLoader.load();
                        view.PaymentWindowFXMLController controller = fxmlLoader.getController();
                        controller.setCont(cont);
                        controller.setUsuario(profile);
                        controller.setShoe(shoe);
                        
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();

                        Stage currentStage = (Stage) buyProductButton.getScene().getWindow();
                        currentStage.close();

                    } catch (IOException ex) {
                        Logger.getLogger(PaymentWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                    }
    }
    
    /**
     * Abre la ventana para modificar el perfil.
     * @param event Evento del menú
     */
    @FXML
    private void openModifyProfileWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ModifyWindow.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Modify your profile");
            stage.setScene(new Scene(root));
            stage.show();

            view.ModifyWindowController controllerWindow = fxmlLoader.getController();
            controllerWindow.setCont(cont);
            controllerWindow.setProfile(profile);

            // Close current window
            Stage currentStage = (Stage) buyProductButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            personalLogger.logError(ex.getMessage());
        }
    }

}