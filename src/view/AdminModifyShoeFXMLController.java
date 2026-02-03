/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.PersonalLogger;
import model.Profile;
import model.Shoe;

/**
 * FXML controller for the Admin Shoe Management view.
 * <p>
 * This class allows an administrator to view, add, delete, and modify shoes. It
 * also handles user profile modification and opening system manuals or reports
 * in PDF format.
 * </p>
 *
 * @author Deusto
 * @version 1.0
 * @since 2024
 */
public class AdminModifyShoeFXMLController implements Initializable {

    /**
     * TableView that displays the list of shoes.
     */
    @FXML
    private TableView<Shoe> tableShoe;

    /**
     * Table columns for shoe attributes.
     */
    @FXML
    private TableColumn<Shoe, Integer> columnBrand;
    @FXML
    private TableColumn<Shoe, Integer> columnModel;
    @FXML
    private TableColumn<Shoe, Integer> columnColor;
    @FXML
    private TableColumn<Shoe, Integer> columnSize;
    @FXML
    private TableColumn<Shoe, Integer> columnPrice;
    @FXML
    private TableColumn<Shoe, Integer> columnStock;

    /**
     * Buttons used to delete and add selected shoe.
     */
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;

    /**
     * Logger used to register application errors.
     */
    private PersonalLogger personalLogger;

    /**
     * Observable list containing the shoes displayed in the table.
     */
    private ObservableList<Shoe> listShoe;

    /**
     * Main application controller.
     */
    private Controller cont;

    /**
     * Profile of the currently logged-in user.
     */
    private Profile profile;

    /**
     * Menu bar and menu items of the view.
     */
    @FXML
    private MenuBar menu;
    @FXML
    private Menu menuHome;
    @FXML
    private MenuItem ModifyProfileSubmenu;
    @FXML
    private Menu menuHelp;
    @FXML
    private Menu menuActions;
    @FXML
    private MenuItem menuSettings;
    @FXML
    private MenuItem menuReport;

    /**
     * Gets the main controller instance.
     *
     * @return the controller
     */
    public Controller getCont() {
        return cont;
    }

    /**
     * Sets the main controller and loads the shoe data.
     *
     * @param cont the controller to set
     */
    public void setCont(Controller cont) {
        this.cont = cont;
        loadShoes();
    }

    /**
     * Gets the current user profile.
     *
     * @return the user profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the current user profile.
     *
     * @param profile the profile to set
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Initializes the controller.
     * <p>
     * This method is automatically called after the FXML file is loaded. It
     * configures the table columns, enables editing, and sets up stock editing
     * handling. It also adds a context menu to each row for adding or editing
     * shoes.
     * </p>
     *
     * @param url location used to resolve relative paths
     * @param rb resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableShoe.setEditable(true);
        listShoe = FXCollections.observableArrayList();

        columnBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        columnModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        columnColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        columnSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnStock.setCellFactory(
                TextFieldTableCell.forTableColumn(new IntegerStringConverter())
        );
        columnStock.setOnEditCommit(this::onStockEdit);
        tableShoe.setRowFactory(tv -> {
            TableRow<Shoe> row = new TableRow<>();

            ContextMenu contextMenu = new ContextMenu();
            MenuItem addEditItem = new MenuItem("Add shoe");
            addEditItem.setOnAction(event -> {
                Shoe clickedShoe = row.getItem();
                openAddShoeWindow(clickedShoe);
            });
            contextMenu.getItems().add(addEditItem);

            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });

    }

    /**
     * Loads all shoes from the controller and displays them in the table.
     */
    private void loadShoes() {
        listShoe.setAll(cont.loadShoes());
        tableShoe.setItems(listShoe);
    }

    /**
     * Deletes the selected shoe from the system and removes it from the table.
     */
    @FXML
    private void delete() {
        Shoe selectedShoe = tableShoe.getSelectionModel().getSelectedItem();

        if (cont.dropShoe(selectedShoe)) {
            listShoe.remove(selectedShoe);
        }
    }

    /**
     * Opens a modal window to add a new shoe to the system.
     */
    @FXML
    public void addNew() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
            Parent root = loader.load();

            AddProductController addCtrl = loader.getController();
            addCtrl.setCont(cont);
            addCtrl.setProfile(profile);

            Stage stage = new Stage();
            stage.setTitle("Add new shoe");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            loadShoes();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles stock editing from the table.
     * <p>
     * Prevents negative values and updates the stock using the controller.
     * </p>
     *
     * @param event the cell edit event
     */
    private void onStockEdit(
            TableColumn.CellEditEvent<Shoe, Integer> event
    ) {

        Shoe shoe = event.getRowValue();
        Integer newStock = event.getNewValue();

        if (newStock < 0) {
            showInfo("You cannot enter negative stock;, ensure that the stock is greater than 0.");
            tableShoe.refresh();
            return;
        }
        cont.updateStockShoe(shoe, newStock);
    }

    /**
     * Opens the window to modify the current user profile.
     *
     * @param event the action event triggered by the menu
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
            Stage currentStage = (Stage) deleteButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException ex) {
            personalLogger.logError(ex.getMessage());
        }
    }

    /**
     * Opens the user manual PDF file using the system default PDF viewer.
     *
     * @param event the action event triggered by the menu item
     */
    @FXML
    private void openReport(ActionEvent event) {
        try {
            File pdf = new File("pdfs/INFORME_DIN.pdf");
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
     * Opens the system manual PDF using the default PDF viewer.
     *
     * @param event the action event triggered by the menu item
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

    /**
     * Displays an information alert with a custom message.
     *
     * @param msg the message to display
     */
    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Error modifying stock");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    /**
     * Opens a window to add or edit a shoe.
     *
     * @param shoe the shoe to add or edit
     */
    private void openAddShoeWindow(Shoe shoe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
            Parent root = loader.load();

            AddProductController addProduct = loader.getController();
            addProduct.setCont(cont);
            addProduct.setProfile(profile);

            Stage stage = new Stage();
            stage.setTitle("Add");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadShoes();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
