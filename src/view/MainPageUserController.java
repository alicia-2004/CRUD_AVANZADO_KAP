/**
 * FXML Controller class for the main user page.
 * This class handles the user interface logic for the main page where users can
 * view, search, and filter shoes. It manages the display of shoes in a grid layout,
 * provides filtering by brand and price, and handles navigation to detailed views
 * and other application windows.
 * 
 * @author 2dami
 */
package view;

import controller.Controller;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import model.Shoe;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.PersonalLogger;
import model.Profile;

/**
 * MainPageUserController implements the user interface logic for the main page
 * where shoes are displayed, filtered, and selected for detailed viewing.
 */
public class MainPageUserController implements Initializable {

    /** Menu bar component for application navigation */
    @FXML
    private MenuBar menu;
    
    /** Home menu item */
    @FXML
    private Menu menuHome;
    
    /** Help menu item */
    @FXML
    private Menu menuHelp;
    
    /** Text field for searching shoes by brand */
    @FXML
    private TextField searchTextField;
    
    /** Image view for price filter button */
    @FXML
    private ImageView imgFilter;
    
    /** Grid pane for displaying shoes in a 2-column layout */
    @FXML
    private GridPane gridShoes;
    
    /** Label displaying the current filter state */
    @FXML
    private Label lblFIlter;
    
    /** Actions menu containing user settings */
    @FXML
    private Menu menuActions;
    
    /** Menu item for opening profile modification window */
    @FXML
    private MenuItem menuSettings;
    
    /** Menu item for opening reports */
    @FXML
    private MenuItem menuReport;

    /** Application controller for business logic */
    private Controller cont;
    
    /** Current user profile */
    private Profile profile;
    
    /** List of shoes to display */
    private List<Shoe> shoeList;
    
    /** Current state of the price filter (0=Original, 1=Ascending, 2=Descending) */
    private int filerState;
    
    /** Logger for error tracking */
    private PersonalLogger personalLogger;
    @FXML
    private MenuItem menuHelpItem;

    /**
     * Sets the application controller and loads shoes into the grid.
     * 
     * @param cont the application controller to set
     */
    public void setCont(Controller cont) {
        this.cont = cont;

        if (cont != null) {
            loadShoesToGridPane(cont.loadModels());
            System.out.println("lista completa cargada: " + cont.loadShoes());
        }
    }

    /**
     * Sets the current user profile.
     * 
     * @param profile the user profile to set
     */
    public void setUser(Profile profile) {
        this.profile = profile;
    }

    /**
     * Initializes the controller class by configuring the grid layout.
     * 
     * @param url the location used to resolve relative paths for the root object
     * @param rb the resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gridConfiguration();
        personalLogger = new PersonalLogger();
    }

    /**
     * Configures the grid layout by clearing existing content and setting up
     * two columns with fixed width.
     */
    private void gridConfiguration() {
        gridShoes.getChildren().clear();
        gridShoes.getColumnConstraints().clear();
        gridShoes.getRowConstraints().clear();

        for (int i = 0; i < 2; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(240);
            gridShoes.getColumnConstraints().add(col);
        }
    }

    /**
     * Loads shoes into the grid pane in a 2-column layout.
     * Calculates the required number of rows and creates VBox containers
     * for each shoe.
     * 
     * @param list the list of shoes to display
     */
    private void loadShoesToGridPane(List<Shoe> list) {
        // Calculate how many columns 
        shoeList = list;
        if (!shoeList.isEmpty()) {

            // Calculate how many rows
            int neededRows = (int) Math.ceil(shoeList.size() / 2.0); // rounds the number up

            for (int i = 0; i < neededRows; i++) {
                RowConstraints row = new RowConstraints();
                row.setPrefHeight(164);
                gridShoes.getRowConstraints().add(row);
            }

            // Load the vbox in the grid
            for (int i = 0; i < shoeList.size(); i++) {
                Shoe shoe = shoeList.get(i);
                VBox vbox = createVbox(shoe);

                int row = i / 2;
                int column = i % 2;

                gridShoes.add(vbox, column, row);
                System.out.println("Se ha creado el vbox");
            }   
        }
    }

    /**
     * Creates a VBox container for displaying a single shoe.
     * Includes shoe image, name, price, and interactive features like
     * context menu and click handlers.
     * 
     * @param shoe the shoe to display in the VBox
     * @return the configured VBox containing shoe information
     */
    private VBox createVbox(Shoe shoe) {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(180);
        vbox.setPrefHeight(200);
        vbox.setStyle("-fx-background-color:white; -fx-border-color:#ddd; -fx-border-radius: 8; -fx-padding: 15;");

        vbox.setPickOnBounds(true);

        vbox.setUserData(shoe);

        // Context menu
        ContextMenu contextMenu = new ContextMenu();

        MenuItem details = new MenuItem("Details");

        details.setOnAction(e -> {
            // Alert
            Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
            detailsAlert.setTitle("Shoe info");
            detailsAlert.setHeaderText(shoe.getBrand() + " " + shoe.getModel());

            // Shoe info
            String info = "Shoe: " + shoe.getBrand() + shoe.getModel() + "\n"
                    + "Precio: " + String.format("€%.2f", shoe.getPrice()) + "\n"
                    + "Color: " + shoe.getColor() + "\n"
                    + "Origin: " + shoe.getOrigin() + "\n"
                    + "Exclusive: " + shoe.getExclusive();

            detailsAlert.setContentText(info);
            detailsAlert.showAndWait();
        });

        contextMenu.getItems().addAll(details);

        vbox.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(vbox, event.getScreenX(), event.getScreenY());
            }
        });

        vbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) { // left click to differentiate from right click 
                    Shoe selectedShoe = (Shoe) vbox.getUserData();
                    openNextWindow(selectedShoe);
                }
            }
        });

        vbox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                vbox.setStyle("-fx-background-color:#f0f8ff; -fx-border-color: #1e90ff; fx-border-width:2; -fx-border-radious:8; -fx-padding:15; -fx-cursor-hand;");
            }
        });

        vbox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                vbox.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 8; -fx-padding: 15; -fx-cursor: default;");
            }
        });

        // Image of the shoe in the vbox
        ImageView imgView = new ImageView();
        String shoeImgPath = shoe.getImgPath();
        System.out.println(shoeImgPath);

        try {
            Image img = new Image(getClass().getResource("/images/" + shoeImgPath).toExternalForm());
            imgView.setImage(img);
        } catch (Exception e) {
            personalLogger.logError(e.getMessage());
            Image imgDefault = new Image(getClass().getResource("/images/default_img.jpg").toExternalForm());
            imgView.setImage(imgDefault);
        }

        imgView.setFitWidth(120);
        imgView.setFitHeight(80);
        imgView.setPreserveRatio(true);

        // The name
        Label labelName = new Label(shoe.getBrand() + " " + shoe.getModel());
        labelName.setStyle("-fx-font-weight:bold; -fx-font-size:14px;");
        labelName.setWrapText(true);
        labelName.setMaxWidth(150);

        // The price
        Label labelPrice = new Label(String.format("€%.2f", shoe.getPrice()));
        labelPrice.setStyle("-fx-font-size: 16px; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        vbox.getChildren().addAll(imgView, labelName, labelPrice);

        return vbox;
    }

    /**
     * Opens the shoe detail window for the selected shoe.
     * Closes the current main window and opens a new window with detailed
     * shoe information.
     * 
     * @param shoe the shoe to display in detail
     */
    private void openNextWindow(Shoe shoe) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ShoeDetail.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Shoe Detail");
            stage.setScene(new Scene(root));
            stage.show();

            ShoeDetailController controller = fxmlLoader.getController();
            controller.setCont(cont);
            controller.setUser(profile);
            controller.setShoe(shoe);

            Stage currentStage = (Stage) gridShoes.getScene().getWindow();
            currentStage.close();

        } catch (IOException ex) {
            personalLogger.logError(ex.getMessage());
        }
    }

    /**
     * Filters shoes by brand name when Enter key is pressed in search field.
     * If search text is empty, reloads all shoes.
     * 
     * @param event the key event triggered by the search field
     */
    @FXML
    private void filterShoes(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String textToSearch = searchTextField.getText();
            List<Shoe> filteredShoe = new ArrayList<>();
            shoeList = cont.loadModels();

            if (textToSearch != "") {
                for (Shoe shoe : shoeList) {
                    if (shoe.getBrand().equalsIgnoreCase(textToSearch)) {
                        filteredShoe.add(shoe);
                    }
                }
                gridConfiguration();
                loadShoesToGridPane(filteredShoe);
            }
        } else {
            gridConfiguration();
            loadShoesToGridPane(cont.loadShoes());
        }
    }

    /**
     * Filters shoes by price in ascending, descending, or original order.
     * Cycles through three states: Original → Ascending → Descending → Original.
     * Updates the filter label to reflect the current sorting order.
     * 
     * @param event the mouse event triggered by clicking the filter icon
     */
    @FXML
    private void filterByPrice(MouseEvent event) {
        shoeList = cont.loadShoes();
        List<Shoe> filteredShoe = new ArrayList<>(shoeList);

        switch (filerState) {
            case 0:  // Ascending order
                Collections.sort(filteredShoe);
                lblFIlter.setText("Ascending");
                filerState = 1;
                break;

            case 1:  // Descending order
                Collections.sort(filteredShoe, new Comparator<Shoe>() {
                    @Override
                    public int compare(Shoe s1, Shoe s2) {
                        lblFIlter.setText("Descending");
                        return s2.compareTo(s1);
                    }
                });
                filerState = 2;
                break;

            case 2:  // Original order
                filteredShoe = shoeList;
                lblFIlter.setText("Original");
                filerState = 0;
                break;
        }

        gridConfiguration();
        loadShoesToGridPane(filteredShoe);
    }

    /**
     * Opens the profile modification window.
     * Closes the current window and opens a new window for profile editing.
     * 
     * @param event the action event triggered by selecting the modify profile menu item
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
            Stage currentStage = (Stage) imgFilter.getScene().getWindow();
            currentStage.close();
        } catch (Exception ex) {
            personalLogger.logError(ex.getMessage());
        }
    }

    /**
     * Opens the user manual PDF file using the system's default PDF viewer.
     * 
     * @param event the action event triggered by selecting the report menu item
     */
    @FXML
    private void openReport(ActionEvent event) {
        try {
            File pdf = new File("pdfs/MANUAL_DE_USUARIO.pdf");
            if (!pdf.exists()) {
                personalLogger.logMessage("No existe el PDF: " + pdf.getAbsolutePath());
                return;
            }
            Desktop.getDesktop().open(pdf);

        } catch (Exception e) {
            personalLogger.logError(e.getMessage());
        }
    }

    /**
     * Opens the user manual PDF file using the system's default PDF viewer.
     * 
     * @param event the action event triggered by selecting the manual menu item
     */
    @FXML
    private void openManual(ActionEvent event) {
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
}