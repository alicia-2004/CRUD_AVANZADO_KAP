/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Profile;

/**
 * FXML Controller class
 *
 * @author Deusto
 */
public class PaymentWindowFXMLController implements Initializable {
    @FXML
    private TextField textFieldCVV;
    @FXML
    private TextField textFieldNumTarjeta;
    @FXML
    private Button buttonPay;
    @FXML
    private Date datePickerCaducidad;
    private Controller cont;
    private Profile profile;
    
    public void setUsuario(Profile profile) {
        this.profile = profile;
    }
    public void setCont(Controller cont) {
        this.cont = cont;
    }
    /**
     * Initializes the controller class.
     */
    
    @FXML
    private void pay(){
        String cvv = textFieldCVV.getText();
        String numTarjeta = textFieldNumTarjeta.getText();
        Date caducidad = (Date) datePickerCaducidad.clone();
        cont.checkPayments(cvv, numTarjeta, caducidad, profile.getUsername());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
