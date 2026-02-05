package viewText;

import java.util.concurrent.TimeoutException;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.Main;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class ShoeDetailTest extends ApplicationTest {
    
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
    
    @Test
    public void testComboBoxAndNavigation() throws InterruptedException {
        
        // Login
        if (lookup("#TextField_Username").tryQuery().isPresent()) {
            clickOn("#TextField_Username");
            write("jlopez");
            clickOn("#PasswordField_Password");
            write("pass123");
            clickOn("#Button_LogIn");
            sleep(3000);
        }
        
        // Verificar ventana principal
        assertTrue("Debería estar en ventana principal", 
                  lookup("#gridShoes").tryQuery().isPresent());
        
        // Buscar y hacer clic en primer zapato
        Node shoeVBox = null;
        GridPane grid = lookup("#gridShoes").query();
        
        for (Node node : grid.getChildren()) {
            if (node instanceof VBox) {
                shoeVBox = node;
                break;
            }
        }
        
        assertNotNull("Debe encontrar un VBox de zapato", shoeVBox);
        clickOn(shoeVBox);
        sleep(3000);
        
        // Verificar que estamos en Shoe Detail
        assertTrue("Debería estar en ShoeDetail",
                  lookup("#shoeDetailWindow").tryQuery().isPresent());
        
        // Verificar elementos básicos
        verifyThat("#shoeDetailWindow", isVisible());
        verifyThat("#imgShoe", isVisible());
        verifyThat("#lblName", isVisible());
        verifyThat("#lblStock", isVisible());
        verifyThat("#cmbSize", isVisible());
        verifyThat("#btnBack", isVisible());
        
        // Probar ComboBox
        ComboBox<Double> sizeCombo = lookup("#cmbSize").query();
        
        // Verificar que tiene opciones
        assertFalse("ComboBox debe tener opciones", sizeCombo.getItems().isEmpty());
        int comboBoxSize = sizeCombo.getItems().size();
        
        // Verificar talla seleccionada por defecto
        Double initialSelectedSize = sizeCombo.getValue();
        assertNotNull("Debe haber una talla seleccionada", initialSelectedSize);
        
        // Obtener stock inicial
        Label stockLabel = lookup("#lblStock").query();
        String initialStockText = stockLabel.getText();
        assertNotNull("Stock label debe tener texto", initialStockText);
        assertFalse("Stock label no debe estar vacío", initialStockText.isEmpty());
        
        // Intentar cambiar de talla si hay más de una opción
        if (comboBoxSize > 1) {
            // Encontrar una talla diferente
            Double alternativeSize = null;
            for (Double size : sizeCombo.getItems()) {
                if (!size.equals(initialSelectedSize)) {
                    alternativeSize = size;
                    break;
                }
            }
            
            if (alternativeSize != null) {
                // Intentar cambiar la talla
                clickOn("#cmbSize");
                sleep(1000);
                
                // Usar flecha para seleccionar opción diferente
                if (alternativeSize > initialSelectedSize) {
                    push(KeyCode.DOWN);
                } else {
                    push(KeyCode.UP);
                }
                sleep(500);
                
                // Confirmar selección
                push(KeyCode.ENTER);
                sleep(2000);
                
                // Verificar si la talla cambió
                Double currentSize = sizeCombo.getValue();
                
                // Verificar stock actual
                String currentStockText = stockLabel.getText();
                assertNotNull("Stock actual no debe ser null", currentStockText);
                assertFalse("Stock actual no debe estar vacío", currentStockText.isEmpty());
            }
        }
        
        // Verificar información adicional
        Label nameLabel = lookup("#lblName").query();
        Label priceLabel = lookup("#lblPrice").query();
        
        assertNotNull("Nombre no debe ser null", nameLabel.getText());
        assertFalse("Nombre no debe estar vacío", nameLabel.getText().isEmpty());
        assertTrue("Precio debe empezar con €", priceLabel.getText().startsWith("€"));
        
        // Probar botón Back
        clickOn("#btnBack");
        sleep(3000);
        
        // Verificar que volvimos
        assertTrue("Debería estar de vuelta en ventana principal",
                  lookup("#gridShoes").tryQuery().isPresent());
    }
}