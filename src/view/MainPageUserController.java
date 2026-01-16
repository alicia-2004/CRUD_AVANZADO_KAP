/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import model.Shoe;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Profile;

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
    private GridPane gridShoes;

    private Controller cont;
    private Profile profile;
    private List<Shoe> shoeList;

    /**
     * Initializes the controller class.
     */
    public void setCont(Controller cont) {
        this.cont = cont;

        if (cont != null) {
            loadShoesToGridPane(cont.loadShoes());
        }
    }

    public void setUser(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gridConfiguration();
    }

    private void gridConfiguration() {
        gridShoes.getChildren().clear();
        gridShoes.getColumnConstraints().clear();
        gridShoes.getRowConstraints().clear();

        for (int i = 0; i < 2; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(240);
            gridShoes.getColumnConstraints().add(col);
        }
    }

    private void loadShoesToGridPane(List<Shoe> list) {
        //Calculate how many columns 
        shoeList = list;
        if (!shoeList.isEmpty()) {
            System.out.println("Ha llegado dentro");
            //Calculate how wmany rows
            int neededRows = (int) Math.ceil(shoeList.size() / 2.0); //rounds the number up

            for (int i = 0; i > neededRows; i++) {
                RowConstraints row = new RowConstraints();
                row.setPrefHeight(164);
                gridShoes.getRowConstraints().add(row);
            }

            //load the vbox in the grid
            for (int i = 0; i < shoeList.size(); i++) {
                Shoe shoe = shoeList.get(i);
                VBox vbox = createVbox(shoe);

                int row = i / 2;
                int column = i % 2;

                gridShoes.add(vbox, column, row);
                System.out.println("Se ha creado el vbox");
            }
        }

    }

    private VBox createVbox(Shoe shoe) {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(180);
        vbox.setPrefHeight(200);
        vbox.setStyle("-fx-background-color:white; -fx-border-color:#ddd; -fx-border-radius: 8; -fx-padding: 15;");

        vbox.setUserData(shoe);

        vbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Shoe selectedShoe = (Shoe) vbox.getUserData();
                openNextWindow(selectedShoe);
            }
        });

        vbox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                vbox.setStyle("-fx-background-color:#f0f8ff; -fx-border-color: #1e90ff; fx-border-width:2; -fx-border-radious:8; -fx-padding:15; -fx-cursor-hand;");
            }

        });

        vbox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                vbox.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 8; -fx-padding: 15; -fx-cursor: default;");
            }
        });

        //Image of the shoe in the vbox
        ImageView imgView = new ImageView();
        String shoeImgPath = shoe.getImgPath();
        System.out.println(shoeImgPath);

        try {
            Image img = new Image(getClass().getResourceAsStream(shoeImgPath));
            imgView.setImage(img);
        } catch (Exception e) {
            Image imgDefault = new Image(getClass().getResourceAsStream("../images/default_shoe.png"));
            imgView.setImage(imgDefault);
        }

        imgView.setFitWidth(120);
        imgView.setFitHeight(80);
        imgView.setPreserveRatio(true);

        //THe name
        Label labelName = new Label(shoe.getBrand() + " " + shoe.getModel());
        labelName.setStyle("-fx-font-weight:bold; -fx-font-size:14px;");
        labelName.setWrapText(true);
        labelName.setMaxWidth(150);

        //The price
        Label labelPrice = new Label(String.format("€%.2f", shoe.getPrice()));
        labelPrice.setStyle("-fx-font-size: 16px; -fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        vbox.getChildren().addAll(imgView, labelName, labelPrice);

        return vbox;
    }

    private void openNextWindow(Shoe shoe) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DetallesZapato.fxml"));
            Parent root = loader.load();

            //PantallaPablo controller = loader.getController();
            //controller.setZapato(shoe);
            //controller.setUsuario(user);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Buy shoe window");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //filter
    @FXML
    private void filterShoes(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String textToSearch = searchTextField.getText();
            List<Shoe> filteredShoe = new ArrayList<>();
            shoeList = cont.loadShoes();

            if (textToSearch != "") {
                for (Shoe shoe : shoeList) {
                    if (shoe.getBrand().equalsIgnoreCase(textToSearch)) {
                        filteredShoe.add(shoe);
                    }
                }
                gridConfiguration();
                loadShoesToGridPane(filteredShoe);

            }
        } else {
            gridConfiguration();
            loadShoesToGridPane(cont.loadShoes());
        }

    }

    @FXML
    private void filterByPrice(MouseEvent event) {
        
    }

}
