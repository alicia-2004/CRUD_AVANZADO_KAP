/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    private List<Shoe> listShoe;
    private Controller cont;
    private Profile profile; 

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
        listShoe = FXCollections.observableArrayList();

        // Enlazar columnas con atributos del modelo
        columnBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        columnModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        columnColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        columnSize.setCellValueFactory(new PropertyValueFactory<>("edad"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("size"));
        columnStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

    }
    
    private void loadShoes() {
    ObservableList<Shoe> shoes =
        FXCollections.observableArrayList(cont.loadShoes());
    tableShoe.setItems(shoes);
}

    

}
