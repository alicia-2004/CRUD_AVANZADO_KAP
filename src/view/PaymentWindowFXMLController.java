/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Profile;
import model.Shoe;
import model.User;

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
    private Button backButton;
    @FXML
    private DatePicker datePickerCaducidad;
    private Controller cont;
    private Profile profile;
    
    private Shoe shoe;

    public Shoe getShoe() {
        return shoe;
    }

    public void setShoe(Shoe shoe) {
        this.shoe = shoe;
    }
    
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
    private void back(){
        try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ShoeDetail.fxml"));
                        Parent root = fxmlLoader.load();
                        view.ShoeDetailController controller = fxmlLoader.getController();
                        controller.setCont(cont);
                        controller.setUser(profile);
                        controller.setShoe(shoe);
                        
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();

                        Stage currentStage = (Stage) buttonPay.getScene().getWindow();
                        currentStage.close();

                    } catch (IOException ex) {
                        Logger.getLogger(PaymentWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                    }
    }
    @FXML
    private void pay(){
        String cvv = textFieldCVV.getText();
        String numTarjeta = textFieldNumTarjeta.getText();
        LocalDate caducidad = datePickerCaducidad.getValue();
        Boolean bien = cont.checkPayments(cvv, numTarjeta, caducidad, (User) profile, shoe.getId());
        if (bien){ 
            showInfoTrue("Comprado");
            
            try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ShoeDetail.fxml"));
                        Parent root = fxmlLoader.load();
                        view.ShoeDetailController controller = fxmlLoader.getController();
                        controller.setCont(cont);
                        controller.setUser(profile);
                        controller.setShoe(shoe);
                        
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();

                        Stage currentStage = (Stage) buttonPay.getScene().getWindow();
                        currentStage.close();

                    } catch (IOException ex) {
                        Logger.getLogger(PaymentWindowFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                    }
        }else{
            showInfoFalse("Problemas en la compra");
        }
    }
    private void showInfoTrue(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Payment");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
    
    private void showInfoFalse(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Payment");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
