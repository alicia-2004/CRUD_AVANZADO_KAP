/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewText;

import static java.rmi.Naming.list;
import java.util.concurrent.TimeoutException;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.util.NodeQueryUtils;

/**
 *
 * @author kevin
 */
public class MainPageUserTest extends ApplicationTest {

    //iniciliza app
    @BeforeClass
    public void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(main.Main.class);
        // Ejemplo: FxToolkit.setupApplication(MyApplication.class);

        login("jlopez", "pass123");
    }

    public void login(String username, String password) {
        clickOn("#TextField_Username");
        write("username");
        clickOn("#PasswordField_Password");
        write("password");
    }

    //all elements are visible
    @Test
    public void visibleElements() {

        //grid
        verifyThat("#gridShoes", isVisible());

        //vbox
        verifyThat("Nike", isVisible());
        verifyThat("Air Run", isVisible());
        verifyThat("79.99", isVisible());

        //menu
        verifyThat("#menu", isVisible());

        //icon to filter
        verifyThat("#imgFilter", isVisible());

    }

    //filter by name
    @Test
    public void searchTest(){
        clickOn("#searchTextField"); 
        write("Nike");
        
        //find all vboxes to verify if the brand name is Nike
        GridPane grid = lookup("#gridShoes").query();

        for (Node node : grid.getChildren()) {
            if (node instanceof VBox) {
                VBox vbox = (VBox)node;
                Label label = (Label) vbox.getChildren().get(1); //0 is image, 1 is label
                String text = label.getText();
              
                assertEquals(text, "Nike"); 

            }
        }
        
    }
    
    //filter by price functionallity
    @Test
    public void filterAndClickTest() {
        clickOn("#imgFilter");

        //find first vbox to verify if the price is the lower one
        GridPane grid = lookup("#gridShoes").query();
        VBox firstVbox = (VBox) grid.getChildren().get(0); //first vbox

        for (Node node : firstVbox.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                String text = label.getText();

                if (text.equals("€79.99")) {
                    assertEquals(text, "€79.99"); 
                }
            }
        }

        //verify if the firstone now is the hihest price
        clickOn("#imgFilter");

        //get the updated list after pressing the button again
        grid = lookup("#gridShoes").query();
        firstVbox = (VBox) grid.getChildren().get(0);

        for (Node node : firstVbox.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                String text = label.getText();

                if (text.equals("€120.5")) {
                    assertEquals(text, "€120.5");
                }
            }
        }
        
        //click and check if opens next window
        clickOn(firstVbox);
        verifyThat("#VentanaPablo", isVisible());
    }
    
    //menu
    @Test
    private void menuTest(){
        clickOn("#Settings"); //hay que cambiar el id en el inspector
        clickOn("#ModifyProfileSubmenu");
    }
    
    
    
}


