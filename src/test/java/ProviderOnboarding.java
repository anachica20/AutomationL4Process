import Utils.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.SQLOutput;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProviderOnboarding {

    Settings settings = new Settings();
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
    public void startOnboarding() {

        //driver.get("https://www.yopmail.com/"); //AQUI EMPIEZA
        driver.get(settings.baseUrl + "sign-in"); //ESTE MIENTRAS SE HACE LA PRUEBA

        //Escribir el correo usado para la invitación
//        CompleteInput.selectById(wait, "login", EnvConfig.get("CORREO_PROV"));
//        ClicButton.clicByTitle(driver, wait, "Revisa el correo @yopmail.com");
//        String[] credenciales = ReadEmails.readEmail(driver,wait,"Invitación a Portal de Proveedores", "correoContenido", "Ingresar a la plataforma", settings.baseUrl + "sign-in");
//        String correo = credenciales[0];
//        String passwordTemporal = credenciales[1];
        //Login.LoginNewProvider(driver, wait, correo, passwordTemporal, EnvConfig.get("PASSWORD_PROV"), settings.baseUrl);
        Login.loginEnterprise(driver, wait, EnvConfig.get("CORREO_PROV"), EnvConfig.get("PASSWORD_PROV"), settings.baseUrl);
        UrlChange.validateUrl(driver,wait, settings.baseUrl + "sign-in", settings.baseUrl + "enterprise");
        UrlChange.waitNewUrl(driver,wait, settings.baseUrl + "enterprise/forms/registration");
        registerBasicInformation();
    }

    public void registerBasicInformation() {

        //INFORMACION BASICA
        driver.get(settings.baseUrl + "enterprise/forms/registration");

        RetryUtils.retryUntilSuccess(() -> {
            ClicButton.clicByExpansionPanelTitle(driver, wait, "Información básica");
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[@class='description' and normalize-space()='Persona.']")));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//svg[circle[@style[contains(.,'stroke-dasharray')]]]")));
            return null;
        }, 4, 5000);

        RetryUtils.retryUntilSuccess(() -> {
            CompleteInput.selectById(wait, "mat-input-5", "CLL 50 70-30");
            return null;
        }, 4, 5000);

        RetryUtils.retryUntilSuccess(() -> {
            //SelectFunctions.selectListOptionById(wait, "mat-select-2", "ABEJORRAL-ANTIOQUIA");
            SelectFunctions.selectListOptionByXPath(driver, wait, "Ciudad.", "ABEJORRAL-ANTIOQUIA");
            return null;
        }, 3, 5000);

        CompleteInput.selectById(wait, "mat-input-6", "correocorp@yopmail.com");
        CompleteInput.selectById(wait, "mat-input-7", "correoorden@yopmail.com");
        //CompleteInput.selectById(wait,"mat-input-8","Representante Legal");

        RetryUtils.retryUntilSuccess(() -> {
            //SelectFunctions.selectListOptionById(wait, "mat-select-2", "CEDULA DE CIUDADANIA");
            SelectFunctions.selectListOptionByXPath(driver, wait, "Tipo de Documento", "CEDULA DE CIUDADANIA");
            return null;
        }, 3, 5000);

        CompleteInput.selectById(wait, "mat-input-9", "12345678");
        CompleteInput.selectById(wait, "mat-input-11", EnvConfig.get("CORREO_REP_LEGAL"));
        CompleteInput.selectById(wait, "mat-input-12", "Bienes y suministros");
        ClicButton.clicByLabel(driver, wait, "GUARDAR");
        UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);

        registerAccountingInformation();
    }

    //INFORMACION CONTABLE Y TRIBUTARIA

    public void registerAccountingInformation() {

        RetryUtils.retryUntilSuccess(() -> {
            ClicButton.clicByExpansionPanelTitle(driver, wait, "Información contable y tributaria");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//svg[circle[@style[contains(.,'stroke-dasharray')]]]")));
            return null;
        }, 3, 9000);

        RetryUtils.retryUntilSuccess(() -> {
            CompleteInput.selectByCss(wait, "mat-form-field .mat-form-field-infix input.mat-input-element", "0127 - Cultivo de plantas con las que se preparan bebidas");
            return null;
        }, 3, 9000);

        ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 0);
        ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 1);

        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectListOptionByXPath(driver, wait, "Régimen contribuyente", "No contribuyente");
            return null;
        }, 3, 5000);

        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectListOptionByLabel(driver, wait, "Información Fiscal", "NO DECLARANTE");
            return null;
        }, 3, 5000);

        RetryUtils.retryUntilSuccess(() -> {
            CompleteInput.selectAutocompleteByLabelAndOption(driver, wait, "Código de Actividad Económica ICA", "0112 - Cultivo de arroz");
            return null;
        }, 3, 5000);

        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectListOptionByXPath(driver, wait, "Ciudad donde declara industria y comercio", "ABEJORRAL-ANTIOQUIA");
            return null;
        }, 3, 5000);

        ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 2);
        ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 3);

        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectListOptionByXPath(driver, wait, "Ciudad + Dpto.", "ABEJORRAL-ANTIOQUIA");
            return null;
        }, 3, 5000);

        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectListOptionByXPath(driver, wait, "Clasificación de su empresa según Decreto 957 del 05/06/19 de acuerdo a sus ingresos por actividades ordinarias anuales (Manufacturera/Servicios/Comercial)", "Servicios-Microempresa (1 - 32.988 UVT)");
            return null;
        }, 3, 5000);

        ClicButton.clicByLabel(driver, wait, "GUARDAR");
        UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);

        registerBillingInformation();
    }

    //INFORMACION DE FACTURACION ELECTRONICA
    public void registerBillingInformation() {

        RetryUtils.retryUntilSuccess(() -> {
            ClicButton.clicByExpansionPanelTitle(driver, wait, "Información de facturación electrónica");
            return null;
        }, 3, 9000);

        ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 0);
        ClicButton.clicByLabel(driver, wait, "GUARDAR");
        UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);

        registerBankingInformation();
    }


    //INFORMACION BANCARIA
    public void registerBankingInformation() {
        RetryUtils.retryUntilSuccess(() -> {
            ClicButton.clicByExpansionPanelTitle(driver, wait, "Información bancaria");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//svg[circle[@style[contains(.,'stroke-dasharray')]]]")));
            return null;
        }, 3, 9000);

        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectListOptionByXPath(driver, wait, "Entidad Bancaria", "AV Villas");
            return null;
        }, 3, 9000);

        ClicButton.clicByLabel(driver, wait, "AHORRO");

        RetryUtils.retryUntilSuccess(() -> {
            CompleteInput.selectAutocompleteByLabelAndOption(driver, wait, "Número de cuenta", "1111111111");
            return null;
        }, 3, 9000);


        ClicButton.clicByLabel(driver, wait, "GUARDAR");
        UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);

        saveForeignCurrencyOperations();
    }

    //OPERACIONES EN MONEDA EXTRANJERA
    public void saveForeignCurrencyOperations() {
        RetryUtils.retryUntilSuccess(() -> {
            ClicButton.clicByExpansionPanelTitle(driver, wait, "Operaciones en moneda extranjera");
            return null;
        }, 3, 9000);

        ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 0);

        ClicButton.clicByLabel(driver, wait, "GUARDAR");
        UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);
        saveShareholdingComposition();
    }

    //COMPOSICION ACCIONARIA

    public void saveShareholdingComposition() {

        RetryUtils.retryUntilSuccess(() -> {
            ClicButton.clicByExpansionPanelTitle(driver, wait, "Composición accionaria");
            return null;
        }, 3, 9000);

        ClicButton.clicByButtonSpanText(driver, wait, "Agregar accionista");

        RetryUtils.retryUntilSuccess(() -> {
            CompleteInput.inputByLabel(wait, driver,"Nombre completo del accionista", "Nombre Accionista");
            return null;
        }, 3, 9000);

        RetryUtils.retryUntilSuccess(() -> {
            SelectFunctions.selectMatOption(driver, wait,"Tipo", "CEDULA DE CIUDADANIA");
            return null;
        }, 3, 9000);

        RetryUtils.retryUntilSuccess(() -> {
            CompleteInput.inputByLabel(wait, driver,"Número de documento", "10101010");

            return null;
        }, 3, 9000);

        RetryUtils.retryUntilSuccess(() -> {
            CompleteInput.inputByLabel(wait, driver, "Participación accionaria 5%", "100");
            return null;
        }, 3, 9000);

        ClicButton.clicById(driver, wait, "confirm-button");
        UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);
        addRelatedPersons();
    }

    //PERSONAS VINCULADAS
    public void addRelatedPersons() {

        RetryUtils.retryUntilSuccess(() -> {
            ClicButton.clicByExpansionPanelTitle(driver, wait, "Personas vinculadas");
            return null;
        }, 3, 9000);
        ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 0);
        ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 1);
        ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 2);
        ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 3);

        ClicButton.clicByLabel(driver, wait, "GUARDAR");
        UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);

        completarReferenciasComerciales();
    }

        //REFERENCIAS COMERCIALES (ARREGLAR)

    public void completarReferenciasComerciales() {

        RetryUtils.retryUntilSuccess(() -> {
            ClicButton.clicByExpansionPanelTitle(driver, wait, "Referencias comerciales");
            return null;
        }, 3, 9000);

        String xpathTexto = "//span[contains(text(),'Referencias comerciales agregadas')]";
        WebElement span = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathTexto)));
        String texto = span.getText(); // Ejemplo: "Referencias comerciales agregadas: (0/3)"

        // Obtener el número actual y el máximo del texto
        Pattern pattern = Pattern.compile("\\((\\d+)/(\\d+)\\)");
        Matcher matcher = pattern.matcher(texto);

        if (matcher.find()) {
            int actuales = Integer.parseInt(matcher.group(1));
            int maximos = Integer.parseInt(matcher.group(2));

            for (int i = actuales; i < maximos; i++) {
                System.out.println("➡️ Agregando referencia comercial #" + (i + 1));
                AddComercialReference.addCommercialReference(driver, wait);

                // Esperar un segundo antes de leer el nuevo valor actualizado
                RetryUtils.retryUntilSuccess(() -> {
                    ClicButton.clicByExpansionPanelTitle(driver, wait, "Referencias comerciales");
                    return null;
                }, 3, 9000);

                // Actualizar valores tras guardar
                span = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathTexto)));
                texto = span.getText();
                matcher = pattern.matcher(texto);
                if (matcher.find()) {
                    actuales = Integer.parseInt(matcher.group(1));
                }
            }
        } else {
            System.out.println("❌ No se pudo encontrar el formato de cantidad de referencias.");
        }
        ClicButton.clicByLabel(driver, wait, "GUARDAR");
        UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);

        addPoliticallyExposedPerson();
    }

        //PERSONAS EXPUESTAS POLITICAMENTE

        public void addPoliticallyExposedPerson () {
            RetryUtils.retryUntilSuccess(() -> {
                ClicButton.clicByExpansionPanelTitle(driver, wait, "Persona Expuesta Políticamente (PEP)");
                return null;
            }, 3, 9000);

            ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 0);
            ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 1);
            ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 2);
            ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 3);
            ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 4);

            ClicButton.clicByLabel(driver, wait, "GUARDAR");
            UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);

            saveOSHSystemInfo();
        }

        //SISTEMA DE SEGURIDAD Y Y SALUD EN EL TRABAJO

        public void saveOSHSystemInfo () {
            RetryUtils.retryUntilSuccess(() -> {
                ClicButton.clicByExpansionPanelTitle(driver, wait, "Sistema de seguridad y salud en el trabajo");
                return null;
            }, 3, 9000);

            ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 0);
            ClicButton.clicByLabel(driver, wait, "GUARDAR");
            UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);

            saveDeclarationsAuthorizationsAndWarranties();

        }

        // Declaraciones, autorizaciones y garantías

        public void saveDeclarationsAuthorizationsAndWarranties () {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            RetryUtils.retryUntilSuccess(() -> {

                ClicButton.clicByExpansionPanelTitle(driver, wait, "Declaraciones, autorizaciones y garantías");
//                wait.until(ExpectedConditions.invisibilityOfElementLocated(
//                        By.xpath("//svg[circle[@style[contains(.,'stroke-dasharray')]]]")));
                return null;
            }, 3, 9000);

            Settings settings = new Settings();

            // Buscar la sección por nombre
            Optional<Settings.Section> seccionEncontrada = settings.sections.stream()
                    .filter(section -> section.nombre.equals("declaracion"))
                    .findFirst();

            if (seccionEncontrada.isPresent()) {
                Settings.Section section = seccionEncontrada.get();
                System.out.println("Sección: " + section.nombre);
                for (Settings.DeclaracionItem item : section.items) {
                    if (item.active == 1) {
                        RetryUtils.retryUntilSuccess(() -> {
//                            wait.until(ExpectedConditions.visibilityOfElementLocated(
//                                    By.xpath("//mat-checkbox[.//span[normalize-space()='"+item.value+"']]")));
                            ClicButton.clicCheckboxPorTexto(driver, wait, item.value);
                            if (item.confirm == 1) {
                                ClicButton.clicById(driver, wait, "confirm-button");
                            }

                            return null;
                        }, 3, 9000);
                    }
                }
            } else {
                System.out.println("Sección no encontrada");
            }
            ClicButton.clicByLabel(driver, wait, "GUARDAR");
            UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);
            registerMandateRecord();
        }

        //CONTANCIA DE MANDATO

        public void registerMandateRecord () {
            RetryUtils.retryUntilSuccess(() -> {
                ClicButton.clicByExpansionPanelTitle(driver, wait, "Constancia de mandato");
                return null;
            }, 3, 9000);
            //ClicButton.clicById(driver, wait, "mat-button-toggle-2-button");
            ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 0);
            ClicButton.clicByLabel(driver, wait, "GUARDAR");
            UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);
            registerAntiCorruptionSystem();
        }

        //SISTEMA DE PREVENCION DE LAVADO DE ACTIVOS

        public void registerAntiCorruptionSystem () {
            RetryUtils.retryUntilSuccess(() -> {
                ClicButton.clicByExpansionPanelTitle(driver, wait, "Sistemas de Prevención de Lavado de Activos, Corrupción y Soborno Trasnacional");
                return null;
            }, 3, 9000);
            ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 0);
            ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 1);
            ClicButton.clicToggleConTextoPorIndice(driver, wait, "No", 2);

            ClicButton.clicByLabel(driver, wait, "GUARDAR");
            UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);
            uploadDocuments();
        }

        public void uploadDocuments () {

            Settings settings = new Settings();

            Optional<Settings.Section> seccionEncontrada = settings.sections.stream()
                    .filter(section -> section.nombre.equals("documentos"))
                    .findFirst();

            if (seccionEncontrada.isPresent()) {
                Settings.Section section = seccionEncontrada.get();
                System.out.println("Sección: " + section.nombre);
                RetryUtils.retryUntilSuccess(() -> {
                    ClicButton.clicByExpansionPanelTitle(driver, wait, section.selector);
                    return null;
                }, 3, 9000);

                for (Settings.DeclaracionItem doc : section.items) {
                    if (doc.active == 1) {
                        RetryUtils.retryUntilSuccess(() -> {

                            String ruta = doc.value;
                            UploadDocuments.subirArchivoOculto(driver, wait, ruta);
                            try {
                                WebElement calendarioBtn = driver.findElement(By.cssSelector("button[aria-label='Open calendar']"));
                                if (calendarioBtn.isDisplayed()) {
                                    ClicButton.clicByAriaLabel(driver, wait, "Open calendar");
                                    CompleteInput.sendCurrentDate(wait);
                                    ClicButton.clicByLabel(driver, wait, "Agregar");
                                }
                            } catch (NoSuchElementException e) {
                            }
                            UrlChange.waitForSnackBarMessage(wait, "El archivo ha sido cargado exitosamente.", true);


                            return null;
                        }, 3, 9000);
                    }
                }
            } else {
                System.out.println("Sección no encontrada");
            }
            ClicButton.clicByLabel(driver, wait, "GUARDAR");
            UrlChange.waitForSnackBarMessage(wait, "La sección se ha guardado correctamente.", true);

            acceptTermsAndConditions();
        }

        public void acceptTermsAndConditions () {
            ClicButton.clicById(driver, wait, "mat-checkbox-1");
            ClicButton.clicByButtonSpanText(driver, wait, "FIRMA DIGITAL"); // FIRMA DIGITAL
            UrlChange.isModalTextVisible(driver, wait, "Verifique su correo electrónico para proceder con la firma del formato");
            ClicButton.clicById(driver, wait, "confirm-button");
            UrlChange.waitNewUrl(driver,wait, settings.baseUrl + "enterprise/roles");
            ClicButton.clicById(driver, wait, "confirm-button");
        }

        @AfterEach
        public void tearDown () {
//        if (driver != null) {
//            driver.quit();
//        }
        }
    }