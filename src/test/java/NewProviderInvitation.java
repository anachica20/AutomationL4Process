import Utils.RetryUtils;
import Utils.UploadDocuments;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import Utils.SelectFunctions;
import Utils.ClicButton;
import Utils.CompleteInput;
import Utils.UrlChange;
import Utils.EnvConfig;

public class NewProviderInvitation {

    Settings settings = new Settings();
    private WebDriver driver;
    private WebDriverWait wait;

    // Localizadores
    By rolPayerLocator = By.xpath("//div[contains(@class, 'content')]//span[contains(text(),'Pagador')]/..");

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void testLoginPayer() {
        login(
                EnvConfig.get("EMAIL_AGRICOLA_QA"),
                EnvConfig.get("PASSWORD_PAGADOR")
        );

        seleccionarRolPagador();

        agregarPersonaJuridica(EnvConfig.get("NIT"), EnvConfig.get("DIGITO"),  EnvConfig.get("RAZON_SOCIAL"), EnvConfig.get("TELEFONO") );
        diligenciarInfoPersonal(EnvConfig.get("CORREO_PROV"),EnvConfig.get("NOMBRE"),EnvConfig.get("PRIMER_APELLIDO"),EnvConfig.get("SEGUNDO_APELLIDO"));

        String ruta = "C://Users//ana.chica//Documents//imagenprueba.png";

        UploadDocuments.subirArchivoOculto(driver, wait, ruta);
        ClicButton.clicByLabel(driver,wait,"Subir");
        ClicButton.clicByLabel(driver,wait,"Enviar");
    }


    private void login(String username, String password) {
        //driver.get(settings.baseUrl + "sign-in"); //DEV
        driver.get( settings.baseUrl + "sign-in"); //QA

        CompleteInput.selectById(wait,"mat-input-0",username);
        CompleteInput.selectById(wait,"mat-input-1",password);
        ClicButton.clicByLabel(driver, wait, "Iniciar sesión");
        UrlChange.validateUrl(driver,wait,settings.baseUrl + "sign-in", settings.baseUrl+ "enterprise"); //DEV
//        UrlChange.validateUrl(driver,wait,settings.baseUrl + "sign-in", settings.baseUrl + "enterprise"); QA
    }

    private void seleccionarRolPagador() {
        RetryUtils.retryUntilSuccess(() -> {
            wait.until(ExpectedConditions.urlToBe( settings.baseUrl + "enterprise/roles"));
            Assertions.assertEquals( settings.baseUrl + "enterprise/roles", driver.getCurrentUrl());

            if (!driver.findElements(By.id("close-button")).isEmpty()) {
                ClicButton.clicById(driver, wait, "close-button");
            }

            wait.until(ExpectedConditions.elementToBeClickable(rolPayerLocator)).click();
            UrlChange.validateUrl(driver,wait, settings.baseUrl + "enterprise/roles",  settings.baseUrl + "enterprise/core/providers/gestion");
            return null;
        }, 3, 300);

        RetryUtils.retryUntilSuccess(() -> {
            Assertions.assertEquals( settings.baseUrl + "enterprise/core/providers/gestion", driver.getCurrentUrl());
            return null;
        }, 3, 300);

       }

        private void agregarPersonaJuridica(String nit, String digito, String razonSocial, String telefono) {

        ClicButton.clicByIcon(driver,wait,"person_add");
        ClicButton.clicByLabel(driver,wait,"Jurídica");

        //Tipo de documento
        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectListOption(driver, wait, "documentType", "NIT");
            return null;
        }, 3, 500);

        CompleteInput.selectById(wait,"mat-input-0",nit);
        CompleteInput.selectById(wait,"mat-input-7",digito);
        CompleteInput.selectById(wait,"mat-input-2",razonSocial);

        //Tipo de proveedor
        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectListOption(driver, wait, "providerType", EnvConfig.get("TIPO_PROVEEDOR") );
            return null;
        }, 3, 500);

        //Condiciones de pago
        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectListOption(driver, wait, "paymentConditionsType", EnvConfig.get("CONDICION_PAGO"));
            return null;
        }, 3, 500);

        //Codigo de pais
        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectListOption(driver, wait, "countryCode", EnvConfig.get("PAIS")); //country-calling-code
            return null;
        }, 3, 500);

        CompleteInput.selectById(wait,"mat-input-1",telefono);
        }

        private void diligenciarInfoPersonal (String email, String nombre, String primerApellido, String segundoApellido) {

        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectListOption(driver, wait, "area",EnvConfig.get("AREA"), true);
            return null;
        }, 3, 500);

        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectListOption(driver, wait, "responsible",EnvConfig.get("EMAIL_RESPONSABLE"), true);
            return null;
        }, 3, 500);

        CompleteInput.selectById(wait,"mat-input-3",email);
        CompleteInput.selectById(wait,"mat-input-4",nombre);
        CompleteInput.selectById(wait,"mat-input-5",primerApellido);
        CompleteInput.selectById(wait,"mat-input-6",segundoApellido);
    }

    @AfterEach
    public void tearDown() {
        //if (driver != null) {
            //driver.quit();
        //}
    }
}
