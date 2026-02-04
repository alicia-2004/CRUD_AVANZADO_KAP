package viewText;

import java.time.LocalDate;
import java.util.concurrent.TimeoutException;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.control.DatePicker;

import main.Main;

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
public class PaymentWindowFXMLTest extends ApplicationTest {


    @BeforeClass
    public static void setUpClass() throws TimeoutException {

        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Main.class);

        Main.crearTablasHibernate();
        Main.insertarDatosPrueba();
    }

    @Test
    public void test01_login() {

        clickOn("#TextField_Username").write("jlopez");
        clickOn("#PasswordField_Password").write("pass123");
        clickOn("#Button_LogIn");

        sleep(1500);

        verifyThat("#gridShoes", isVisible());
    }

    @Test
    public void test02_openShoeDetail() {

        GridPane grid = lookup("#gridShoes").query();

        assertTrue("Debe haber zapatos cargados", grid.getChildren().size() > 0);

        Node firstShoe = grid.getChildren().get(0);

        clickOn(firstShoe);

        sleep(1000);

        verifyThat("#buyProductButton", isVisible());
    }

    @Test
    public void test03_openPaymentWindow() {

        clickOn("#buyProductButton");

        sleep(1000);

        verifyThat("#buttonPay", isVisible());
        verifyThat("#textFieldCVV", isVisible());
    }


    @Test
    public void test04_wrongPayment() {

        clickOn("#textFieldNumTarjeta").write("123");
        clickOn("#textFieldCVV").write("11");

        DatePicker dp = lookup("#datePickerCaducidad").query();
        interact(() -> dp.setValue(LocalDate.now().minusDays(1)));

        clickOn("#buttonPay");

        sleep(500);

        verifyThat("Payment failed", isVisible());
        clickOn(".button");
    }

    @Test
    public void test05_correctPayment() {

        clickOn("#textFieldNumTarjeta").eraseText(10).write("AA1234567890123456789012");
        clickOn("#textFieldCVV").eraseText(3).write("123");

        DatePicker dp = lookup("#datePickerCaducidad").query();
        interact(() -> dp.setValue(LocalDate.of(2027, 5, 31)));


        clickOn("#buttonPay");

        sleep(500);

        verifyThat("Purchase completed", isVisible());
        clickOn(".button");
        sleep(500);
        verifyThat("#shoeDetailWindow", isVisible());
    }


    @Test
    public void test06_backButton() {

        clickOn("#buyProductButton");
        sleep(800);

        clickOn("#backButton");
        sleep(800);

        verifyThat("#shoeDetailWindow", isVisible());
    }
}
