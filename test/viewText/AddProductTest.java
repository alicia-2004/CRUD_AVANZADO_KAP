package viewText;

import java.util.concurrent.TimeoutException;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import main.Main;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

public class AddProductTest extends ApplicationTest {
    
    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(main.Main.class);
        Main.crearTablasHibernate();
        Main.insertarDatosPrueba();
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        FxToolkit.hideStage();
    }
    
    private boolean loginAndNavigateToAddProduct() throws InterruptedException {
        if (lookup("#TextField_Username").tryQuery().isPresent()) {
            clickOn("#TextField_Username");
            write("rluna");
            clickOn("#PasswordField_Password");
            write("zxcvbn");
            clickOn("#Button_LogIn");
            sleep(3000);
        } else if (lookup("#Button_LogOut").tryQuery().isPresent()) {
            // Already logged in as admin
        } else {
            if (lookup(".button").match(btn -> 
                btn instanceof Button && "Back".equals(((Button)btn).getText())
            ).tryQuery().isPresent()) {
                clickOn(".button");
                sleep(2000);
            }
        }

        Button addButton = null;
        for (Node node : lookup(".button").queryAll()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (btn.isVisible() && btn.getText() != null) {
                    String text = btn.getText().toLowerCase();
                    
                    if (text.contains("add") || text.contains("añadir") || 
                        text.contains("nuevo") || text.contains("crear")) {
                        addButton = btn;
                        break;
                    }
                }
            }
        }
        
        if (addButton != null) {
            clickOn(addButton);
            sleep(2000);
            
            if (lookup("#btnAddProduct").tryQuery().isPresent()) {
                return true;
            }
        }
        
        return false;
    }
    
    @Test
    public void testAddProductValidations() throws InterruptedException {
        assertTrue("Debe poder navegar a Add Product", loginAndNavigateToAddProduct());
        
        // Test 1: Try to submit with empty form
        clickOn("#btnAddProduct");
        sleep(1500);
        push(KeyCode.ENTER);
        sleep(500);
        
        // Test 2: Select brand but leave other required fields empty
        clickOn("#cmbBrand");
        sleep(300);
        push(KeyCode.DOWN);
        push(KeyCode.ENTER);
        sleep(500);
        
        clickOn("#btnAddProduct");
        sleep(1500);
        push(KeyCode.ENTER);
        sleep(500);
        
        // Test 3: Invalid size (text instead of number)
        clickOn("#txtModel");
        write("Test Model");
        
        clickOn("#txtSize");
        write("not a number");

        clickOn("#txtColor");
        write("Red");
        
        clickOn("#txtOrigin");
        write("China");

        clickOn("#btnAddProduct");
        sleep(1500);
        push(KeyCode.ENTER);
        sleep(500);
        
        // Test 4: Negative stock
        clickOn("#txtSize");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.DELETE);
        write("42");
        
        clickOn("#txtStock");
        write("-10");

        clickOn("#txtPrice");
        write("99.99");
        
        clickOn("#btnAddProduct");
        sleep(1500);
        push(KeyCode.ENTER);
        sleep(500);
        
        // Test 5: Zero price
        clickOn("#txtStock");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.DELETE);
        write("100");
        
        clickOn("#txtPrice");
        push(KeyCode.CONTROL, KeyCode.A);
        push(KeyCode.DELETE);
        write("0");
        
        clickOn("#btnAddProduct");
        sleep(1500);
        push(KeyCode.ENTER);
        sleep(500);
        
        // Find and click Back button
        Button backButton = findButtonByText("Back");
        if (backButton != null) {
            clickOn(backButton);
            sleep(2000);
        }
    }
    
    @Test
    public void testAddProductSuccessAndVerify() throws InterruptedException {
        assertTrue("Debe poder navegar a Add Product", loginAndNavigateToAddProduct());
        
        // Select brand
        clickOn("#cmbBrand");
        sleep(500);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.ENTER);
        sleep(500);
        
        // Fill form with unique data
        long timestamp = System.currentTimeMillis();
        String uniqueModel = "TestModel_2";
        
        clickOn("#txtModel");
        write(uniqueModel);
        
        clickOn("#txtSize");
        write("44.0");
        
        clickOn("#txtColor");
        write("Green");
        
        clickOn("#txtOrigin");
        write("Italy");
        
        clickOn("#txtStock");
        write("25");
        
        clickOn("#txtPrice");
        write("79.50");
        
        clickOn("#txtDate");
        write("2025-03-01");
        
        clickOn("#btnAddProduct");
        sleep(3000);
        
        push(KeyCode.ENTER);
        sleep(1000);
        
        // Verify form is cleared or changed
        TextField modelField = lookup("#txtModel").query();
        String currentModelText = modelField.getText();
        
        assertFalse("El formulario debería haberse actualizado", 
                   currentModelText.equals(uniqueModel));
        
        // Find and click Back button
        Button backButton = findButtonByText("Back");
        assertNotNull("Debe encontrar botón Back", backButton);
        clickOn(backButton);
        sleep(2000);
        
        assertFalse("No debe estar en Add Product después de Back",
                   lookup("#btnAddProduct").tryQuery().isPresent());
    }
    
    
    
    private Button findButtonByText(String textToFind) {
        for (Node node : lookup(".button").queryAll()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (btn.isVisible() && btn.getText() != null && textToFind.equals(btn.getText())) {
                    return btn;
                }
            }
        }
        
        for (Node node : lookup(node -> {
            if (node instanceof Button) {
                Button btn = (Button) node;
                return btn.isVisible() && textToFind.equals(btn.getText());
            }
            return false;
        }).queryAll()) {
            return (Button) node;
        }
        
        return null;
    }
}