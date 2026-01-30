package view;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Profile;
import model.Shoe;

public class ShoeDetailController {

    @FXML private ImageView imgShoe;
    @FXML private Label lblName;
    @FXML private Label lblSubtitle;
    @FXML private Label lblPrice;
    @FXML private Label lblStock;
    @FXML private ComboBox<String> cmbSize;

    private Controller cont;
    private Profile profile;
    private Shoe shoe;

    public void setCont(Controller cont) {
        this.cont = cont;
    }

    public void setUser(Profile profile) {
        this.profile = profile;
    }

    public void setShoe(Shoe shoe) {
        this.shoe = shoe;
        paintShoe();
    }

    private void paintShoe() {
        if (shoe == null) return;

        lblName.setText(shoe.getBrand() + " " + shoe.getModel());
        lblSubtitle.setText(shoe.getColor() + " (" + shoe.getOrigin() + ")");
        lblPrice.setText(String.format("€%.2f", shoe.getPrice()));
        lblStock.setText(shoe.getStock()+" Units in stock");

        // Imagen
        try {
            Image img = new Image(getClass().getResourceAsStream(shoe.getImgPath()));
            imgShoe.setImage(img);
        } catch (Exception e) {
            Image imgDefault = new Image(getClass().getResourceAsStream("../images/default_shoe.png"));
            imgShoe.setImage(imgDefault);
        }

        cmbSize.getItems().clear();
        cmbSize.getItems().add(String.valueOf(shoe.getSize()));
        cmbSize.getSelectionModel().selectFirst();
    }
}
