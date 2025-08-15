import Utils.ClicButton;
import Utils.CompleteInput;
import Utils.ReadEmails;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utils.*;

import java.time.Duration;

public class DigitalSignature {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        System.out.println("Iniciando driver...");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void DigitallySign(){
        driver.get("https://www.yopmail.com/");

        Settings settings = new Settings();

        //logica para ir a yopmail

        CompleteInput.selectById(wait, "login", "correoreplegal@yopmail.com");
        ClicButton.clicByTitle(driver, wait, "Revisa el correo @yopmail.com");
        ReadEmails.readAdobeSign(driver, wait, "Revisar y firmar", "adobe.com/public/esign");

        ClicButton.clicById(driver, wait,"lhp-continue-btn");
        ClicButton.clicByCssSelector(driver, wait,"div[data-testid='header-signature-form-field']" );
        CompleteInput.sendKeysByAriaLabel(wait, "Firma arriba", "Firma Proveedor");
        ClicButton.clicByCssSelector(driver, wait, "button[aria-label='Aplicar']");
        ClicButton.clicByCssSelector(driver, wait, "button[data-testid='footer-submit-button']");
        UrlChange.waitForSnackBarMessage(wait, "Has firmado correctamente.", true);

    }
    @AfterEach
    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
    }
}
