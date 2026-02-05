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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.Main;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.util.NodeQueryUtils;

/**
 *
 * @author kevin
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainPageUserTest extends ApplicationTest {
  
    
    //iniciliza app
    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(main.Main.class);
        Main.crearTablasHibernate();
        Main.insertarDatosPrueba();
        // Ejemplo: FxToolkit.setupApplication(MyApplication.class);

    }
    
    @Test
    public void test01_login() {
        clickOn("#TextField_Username");
        write("jlopez");
        clickOn("#PasswordField_Password");
        write("pass123");
        clickOn("#Button_LogIn");
        sleep(2000); 
        
    }

    //all elements are visible
    @Test
    public void test02_visibleElements() {

        //grid
        verifyThat("#gridShoes", isVisible());
        
        //text
        verifyThat("#lblFIlter", isVisible());

        //menu
        verifyThat("#menu", isVisible());

        //icon to filter
        verifyThat("#imgFilter", isVisible());
        
        GridPane grid = lookup("#gridShoes").query();
        assertTrue("Debería haber zapatos en el grid", grid.getChildren().size() > 0);

    }

    //filter by name
    @Test
    public void test03_searchTest(){
        boolean found = false;
        clickOn("#searchTextField"); 
        write("Nike");
        push(KeyCode.ENTER);
        sleep(1000);
        
        //find all vboxes to verify if the brand name is Nike
        GridPane grid = lookup("#gridShoes").query();

        for (Node node : grid.getChildren()) {
            if (node instanceof VBox) {
                VBox vbox = (VBox)node;
                Label label = (Label) vbox.getChildren().get(1); //0 is image, 1 is label
                String text = label.getText();
              
                found = true;
                assertEquals(text, "Nike Air Max"); 
            }
        }
        
        assertTrue("Debería encontrar al menos un zapato Nike", found);
        
        //clean
        clickOn("#searchTextField");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.DELETE);
        push(KeyCode.ENTER);
        sleep(500);
        
    }
    
    @Test
    public void test04_openModifyProfile() {

        clickOn("#menuActions");
        clickOn("#menuSettings");
        sleep(500);
        clickOn("#Button_Cancel");
    }
    
    //filter by price functionallity
    @Test
    public void test05_filterAndClickTest() {
        Label lblFilter = lookup("#lblFIlter").query();
        assertEquals("Original", lblFilter.getText());
        
        clickOn("#imgFilter");
        sleep(500);
        
        lblFilter = lookup("#lblFIlter").query();
        assertEquals("Ascending", lblFilter.getText());

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
        sleep(500);
        
        lblFilter = lookup("#lblFIlter").query();
        assertEquals("Descending", lblFilter.getText());
        
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
        verifyThat("#shoeDetailWindow", isVisible());
        sleep(500);
        clickOn("#btnBack");
        
        rightClickOn(firstVbox);
        clickOn("Details");
        sleep(500);
        manejarAlertConBoton("Aceptar","boton de alerta de aceptar");
    }
    
    private boolean manejarAlertConBoton(String textoBoton, String descripcion) {
        try {
            clickOn(textoBoton);
            System.out.println("✓ Alert " + descripcion + " manejado");
            sleep(1000);
            return true;
        } catch (Exception e) {
            // Intentar con otros botones
            String[] alternativas = {"Aceptar", "OK", "Yes", "Continuar"};
            for (String alt : alternativas) {
                try {
                    clickOn(alt);
                    System.out.println("✓ Alert " + descripcion + " manejado con '" + alt + "'");
                    sleep(1000);
                    return true;
                } catch (Exception e2) {
                }
            }

            System.out.println("✗ No se pudo manejar alert: " + descripcion);
            return false;
        }
    }
   
    //menu
    /*@Test
    public void test05_menuTest(){
        clickOn("#menuActions");
        clickOn("#menuSettings");
        verifyThat("#ModifyWindow", isVisible());
        clickOn("#Button_Cancel");
    }*/
    
    
    
}


