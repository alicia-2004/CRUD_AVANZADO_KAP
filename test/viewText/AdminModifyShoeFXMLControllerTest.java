package viewText;

import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import main.Main;
import model.Shoe;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminModifyShoeFXMLControllerTest extends ApplicationTest {

    private TableView<Shoe> tableShoe;
    private Button deleteButton;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Inicializa la app JavaFX
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class); // tu clase principal
        Main.crearTablasHibernate(); // si tienes datos de prueba
        Main.insertarDatosPrueba();
    }

    @Test
    public void test01_login() {
        clickOn("#TextField_Username");
        write("rluna");
        clickOn("#PasswordField_Password");
        write("zxcvbn");
        clickOn("#Button_LogIn");
        sleep(2000);

    }

    @Test
    public void test02_elementsVisible() {
       
        verifyThat("#tableShoe", isVisible());
        verifyThat("#deleteButton", isVisible());

        tableShoe = lookup("#tableShoe").queryTableView();
        deleteButton = lookup("#deleteButton").queryButton();

        assertTrue("La tabla debería contener zapatos", tableShoe.getItems().size() > 0);
    }

    @Test
    public void test03_deleteShoe() {
        tableShoe = lookup("#tableShoe").queryTableView();
        deleteButton = lookup("#deleteButton").queryButton();

        Shoe shoe = tableShoe.getItems().get(0);
        tableShoe.getSelectionModel().select(shoe);

        clickOn(deleteButton);
        sleep(500);

        assertFalse("El zapato debería haber sido eliminado", tableShoe.getItems().contains(shoe));
    }

    @Test
    public void test04_openAddNewModal() {
        Button addButton = lookup("#addButton").queryButton();
        clickOn(addButton);
        sleep(500);

        clickOn("#backButton");
    }

    @Test
    public void test05_openModifyProfile() {
        clickOn("#menuSettings");
        verifyThat("#ModifyWindow", isVisible());
        clickOn("#Button_Cancel"); 
    }

    @Test
    public void test06_openManualPDF() {
        clickOn("#menuReport"); 
       
    }

   

}
