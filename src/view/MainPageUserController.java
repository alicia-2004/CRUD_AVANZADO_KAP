/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.User;

/**
 * FXML Controller class
 *
 * @author 2dami
 */
public class MainPageUserController implements Initializable {

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
    @FXML
    private TextField searchTextField;
    @FXML
    private ImageView imgFilter;
    @FXML
    private ImageView imgShoe;
    @FXML
    private Label lblShoeName;
    @FXML
    private Label lblPrice;
    
    private Controller cont;
    private User user;

    /**
     * Initializes the controller class.
     */
    private void loadShoes(){
        
}
    public void setCont(Controller cont){
        this.cont = cont;
    }
    
    public void setUsuario(User User){
        this.user = user;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
