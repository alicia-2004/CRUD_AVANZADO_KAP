package view;

import controller.Controller;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
 * FXML controller for the payment window.
 * <p>
 * This controller manages the purchase process of a selected shoe. It collects
 * the user's payment information (card number, CVV, and expiration date),
 * validates the payment through the {@link Controller}, and handles navigation
 * between the payment window and the shoe detail view.
 * </p>
 *
 * <p>
 * If the payment is successful, the purchase is confirmed and the user is
 * returned to the shoe detail screen. Otherwise, an error message is displayed.
 * </p>
 *
 * @author Deusto
 * @version 1.0
 * @since 2024
 */
public class PaymentWindowFXMLController implements Initializable {

    /**
     * Text field for entering the card CVV code.
     */
    @FXML
    private TextField textFieldCVV;

    /**
     * Text field for entering the card number.
     */
    @FXML
    private TextField textFieldNumTarjeta;

    /**
     * Button that confirms and processes the payment.
     */
    @FXML
    private Button buttonPay;

    /**
     * Button that returns to the previous screen without paying.
     */
    @FXML
    private Button backButton;

    /**
     * Date picker for selecting the card expiration date.
     */
    @FXML
    private DatePicker datePickerCaducidad;

    /**
     * Main application controller used to perform business logic.
     */
    private Controller cont;

    /**
     * Profile of the current user making the purchase.
     */
    private Profile profile;

    /**
     * Shoe selected for purchase.
     */
    private Shoe shoe;

    /**
     * Returns the selected shoe.
     *
     * @return the shoe being purchased
     */
    public Shoe getShoe() {
        return shoe;
    }

    /**
     * Sets the selected shoe.
     *
     * @param shoe the shoe to purchase
     */
    public void setShoe(Shoe shoe) {
        this.shoe = shoe;
    }

    /**
     * Sets the current user profile.
     *
     * @param profile the logged-in user's profile
     */
    public void setUsuario(Profile profile) {
        this.profile = profile;
    }

    /**
     * Sets the main application controller.
     *
     * @param cont the controller instance
     */
    public void setCont(Controller cont) {
        this.cont = cont;
    }

    /**
     * Returns to the shoe detail window without completing the payment.
     * <p>
     * Loads the {@code ShoeDetail.fxml} view and closes the current payment
     * window.
     * </p>
     */
    @FXML
    private void back() {
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

    /**
     * Processes the payment using the entered card information.
     * <p>
     * If the payment validation succeeds, a confirmation message is shown and
     * the user is redirected to the shoe detail window. If it fails, an error
     * message is displayed.
     * </p>
     */
    @FXML
    private void pay() {
        String cvv = textFieldCVV.getText();
        String numTarjeta = textFieldNumTarjeta.getText();
        LocalDate caducidad = datePickerCaducidad.getValue();

        Boolean bien = cont.checkPayments(cvv, numTarjeta, caducidad, (User) profile, shoe.getId());

        if (bien) {
            showInfoTrue("Purchase completed");

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
        } else {
            showInfoFalse("Payment failed");
        }
    }

    /**
     * Displays an information alert when the payment is successful.
     *
     * @param msg the message to display
     */
    private void showInfoTrue(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Payment");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    /**
     * Displays an information alert when the payment fails.
     *
     * @param msg the message to display
     */
    private void showInfoFalse(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Payment");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No initialization required
    }
}
