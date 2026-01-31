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
import java.net.URL;
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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
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
 * FXML Controller class
 *
 * @author Deusto
 */
public class AdminModifyShoeFXMLController implements Initializable {

    @FXML
    private TableView<Shoe> tableShoe;
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
    @FXML
    private Button deleteButton;
    
     private PersonalLogger personalLogger;

    private ObservableList<Shoe> listShoe;
    private Controller cont;
    private Profile profile;
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


    public Controller getCont() {
        return cont;
    }

    public void setCont(Controller cont) {
        this.cont = cont;
        loadShoes();
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableShoe.setEditable(true);
        listShoe = FXCollections.observableArrayList();

        // Enlazar columnas con atributos del modelo
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

    }

    private void loadShoes() {
        listShoe.setAll(cont.loadShoes());
        tableShoe.setItems(listShoe);
    }

    @FXML
    private void delete() {
        Shoe selectedShoe = tableShoe.getSelectionModel().getSelectedItem();

        if (cont.dropShoe(selectedShoe)) {
            listShoe.remove(selectedShoe);
        }
    }
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
    
    @FXML
    private void openReport(ActionEvent event) {
        try {
            Desktop.getDesktop().open(new File("../pdfs/MANUAL_DE_USUARIO.pdf"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void openManual(ActionEvent event) {
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Error modifying stock");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

}
