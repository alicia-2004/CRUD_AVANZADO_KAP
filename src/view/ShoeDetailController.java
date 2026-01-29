package view;

import controller.Controller;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Profile;
import model.Shoe;

public class ShoeDetailController {

    @FXML private ImageView imgShoe;
    @FXML private Label lblName;
    @FXML private Label lblSubtitle;
    @FXML private Label lblPrice;
    @FXML private Label lblStock;
    @FXML private ComboBox<Double> cmbSize;

    private Controller cont;
    private Profile profile;
    private Shoe shoe;
    private java.util.List<Shoe> variants = new java.util.ArrayList<>();
    private Shoe selectedVariant;

    public void setCont(Controller cont) {
        this.cont = cont;
    }

    public void setUser(Profile profile) {
        this.profile = profile;
    }

   public void setShoe(Shoe shoe) {
        this.shoe = shoe;

        variants = cont.loadShoeVariants(shoe);
        paintBaseInfo(shoe);

        fillSizesComboAndSelectInitial(shoe.getSize());
    }

    @FXML
    private void onBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("MainPageUser.fxml")
            );
            Parent root = loader.load();


            MainPageUserController mainCtrl = loader.getController();
            mainCtrl.setCont(cont);
            mainCtrl.setUser(profile);
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void paintBaseInfo(Shoe s) {
        if (s == null) return;

        lblName.setText(s.getBrand() + " " + s.getModel());
        lblSubtitle.setText(s.getColor() + " (" + s.getOrigin() + ")");
        lblPrice.setText(String.format("€%.2f", s.getPrice()));

        setImageSafe(s.getImgPath());
    }
    private void fillSizesComboAndSelectInitial(double initialSize) {
        cmbSize.getItems().clear();

        for (Shoe s : variants) {
            Double size = s.getSize();
            if (!cmbSize.getItems().contains(size)) {
                cmbSize.getItems().add(size);
            }
        }

        cmbSize.setOnAction(e -> {
            Double size = cmbSize.getValue();
            if (size != null) {
                selectVariantBySize(size);
            }
        });
        cmbSize.getSelectionModel().select(initialSize);
        selectVariantBySize(initialSize);
    }   
    private void selectVariantBySize(double size) {
        selectedVariant = null;

        for (Shoe s : variants) {
            if (Double.compare(s.getSize(), size) == 0) {
                selectedVariant = s;
                break;
            }
        }

        if (selectedVariant == null) {
            lblStock.setText("No stock info");
            return;
        }

        lblStock.setText(selectedVariant.getStock() + " Units in stock");
    }
    
   private void setImageSafe(String path) {
        try {
            Image img = new Image(getClass().getResourceAsStream(path));
            imgShoe.setImage(img);
        } catch (Exception e) {
            Image imgDefault = new Image(getClass().getResourceAsStream("../images/default_shoe.png"));
            imgShoe.setImage(imgDefault);
        }
    }




}
