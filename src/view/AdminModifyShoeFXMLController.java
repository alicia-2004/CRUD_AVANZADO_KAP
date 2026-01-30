/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    private ObservableList<Shoe> listShoe;
    private Controller cont;
    private Profile profile;
    @FXML
    private MenuBar menu;
    @FXML
    private Menu menuHome;
    @FXML
    private Menu actionsMenu;
    @FXML
    private MenuItem ModifyProfileSubmenu;
    @FXML
    private Menu menuHelp;

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
            tableShoe.refresh();
            return;
        }
        cont.updateStockShoe(shoe, newStock);
    }

}
