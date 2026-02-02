package view;

import controller.Controller;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Exclusive;
import model.Profile;
import model.Reserved;
import model.Shoe;


public class AddProductController implements Initializable {

    @FXML private Button btnAddProduct;

    @FXML private ComboBox<String> cmbBrand;
    @FXML
    private Button backButton;

    @FXML private TextField txtModel;
    @FXML private TextField txtSize;
    @FXML private TextField txtColor;
    @FXML private TextField txtOrigin;
    @FXML private TextField txtStock;
    @FXML private TextField txtPrice;
    @FXML private TextField txtDate;
    @FXML private TextField txtImgPath;

    @FXML private RadioButton rbExclusive;

    private Profile profile;
    private Controller cont;

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setCont(Controller cont) {
        this.cont = cont;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbBrand.getItems().addAll("Nike", "Adidas", "Puma", "New Balance", "Reebok");
        txtImgPath.setText("../images/default_shoe.png");
    }
    
    @FXML
    private void onBack(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void addProduct(ActionEvent event) {
        try {
            if (cont == null) {
                showError("Controller not set (cont is null).");
                return;
            }

            String brand = cmbBrand.getValue();
            String model = safe(txtModel.getText());
            String color = safe(txtColor.getText());
            String origin = safe(txtOrigin.getText());

            if (brand == null || brand.trim().isEmpty()) {
                showError("Select a brand.");
                return;
            }
            if (model.isEmpty() || color.isEmpty() || origin.isEmpty()) {
                showError("Fill Model, Color and Made in.");
                return;
            }

            double size = Double.parseDouble(safe(txtSize.getText()));
            int stock = Integer.parseInt(safe(txtStock.getText()));
            double price = Double.parseDouble(safe(txtPrice.getText()));

            if (size <= 0 || price <= 0 || stock < 0) {
                showError("Size and price must be > 0. Stock must be >= 0.");
                return;
            }

            Date sqlDate = null;
            String dateText = safe(txtDate.getText());
            if (!dateText.isEmpty()) {
                LocalDate ld = LocalDate.parse(dateText); 
                sqlDate = Date.valueOf(ld);
            }

            String imgPath = safe(txtImgPath.getText());
            if (imgPath.isEmpty()) imgPath = "../images/default_shoe.png";

            Exclusive exclusive = rbExclusive.isSelected() ? Exclusive.TRUE : Exclusive.FALSE;

            // Crear Shoe
            Shoe s = new Shoe();
            s.setBrand(brand);
            s.setModel(model);
            s.setColor(color);
            s.setOrigin(origin);
            s.setSize(size);
            s.setStock(stock);
            s.setPrice(price);
            s.setExclusive(exclusive);
            s.setImgPath(imgPath);

            s.setReserved(Reserved.FALSE);

            if (txtDate.getText() != null && !txtDate.getText().isEmpty()) {
                java.sql.Date d = java.sql.Date.valueOf(txtDate.getText().trim());
                s.setManufactorDate(d);
            }

            boolean ok = cont.addShoe(s);
            if (!ok) {
                showError("Database error: could not insert shoe.");
                return;
            }

            showInfo("Product added successfully!");
            clearForm();

        } catch (NumberFormatException nfe) {
            showError("Size/Stock/Price must be numeric.");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected error");
        }
    }
    

    private void clearForm() {
        cmbBrand.getSelectionModel().clearSelection();
        txtModel.clear();
        txtSize.clear();
        txtColor.clear();
        txtOrigin.clear();
        txtStock.clear();
        txtPrice.clear();
        txtDate.clear();
        txtImgPath.setText("../images/default_shoe.png");
        rbExclusive.setSelected(false);
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("OK");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
