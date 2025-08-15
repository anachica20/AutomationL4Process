package Utils;

import org.bouncycastle.pqc.crypto.newhope.NHSecretKeyProcessor;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReadEmails {

    public static String[] readEmail (WebDriver driver, WebDriverWait wait, String asuntoCorreo,String idContenido, String linkTest, String expectedUrl ){
        //String asuntoCorreo = "Invitación a Portal de Proveedores";
        try {
            WebElement correoElectronico = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(), '" + asuntoCorreo + "')]")));
            correoElectronico.click();
        } catch (Exception e) {
            System.err.println("No se pudo encontrar o clickear el correo electrónico. Error: " + e.getMessage());
        }

        // 4. Verificar que el correo se abrió
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("correoContenido")));
            System.out.println("El correo electrónico se abrió correctamente.");
        } catch (Exception e) {
            System.err.println("No se pudo verificar que el correo electrónico se abrió. Error: " + e.getMessage());
        }

        driver.switchTo().frame("ifmail");

        String correoExtraido = "";
        String contraseñaTemporal = "";

        try {
            correoExtraido = driver.findElement(By.xpath("//li[contains(text(),'Nombre de usuario')]//b")).getText();
            contraseñaTemporal = driver.findElement(By.xpath("//li[contains(text(),'Contraseña temporal')]//b")).getText();

            System.out.println("Correo extraído: " + correoExtraido);
            System.out.println("Contraseña temporal: " + contraseñaTemporal);
            System.out.println(expectedUrl);


        } catch (Exception e) {
            System.err.println("Error al extraer credenciales: " + e.getMessage());
        }

        ClicButton.clicByLink(driver, wait, linkTest);
        UrlChange.switchToNewTabAndValidateUrl(driver,expectedUrl); //(CORTO FUNCIONAL)

        //Login.LoginNewProvider(driver, wait,correoExtraido,contraseñaTemporal,EnvConfig.get("PASSWORD_PROV"),EnvConfig.get("URL_QA"));

        return new String[] {correoExtraido, contraseñaTemporal};
    }

    public static void readAdobeSign(WebDriver driver, WebDriverWait wait, String linkText, String expectedUrlFragment) {
        // Cambiar al iframe donde se visualiza el correo
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ifmail"));

        // Hacer clic en el enlace con el texto "Revisar y firmar"
        WebElement link = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), '" + linkText + "')]"))
        );
        link.click();

        // Volver al contenido principal (por si se requiere)
        driver.switchTo().defaultContent();

        // Manejar si se abre una nueva pestaña
        String originalWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Validar que la URL contiene el fragmento esperado (ej: "adobe.com/public/esign")
        wait.until(ExpectedConditions.urlContains(expectedUrlFragment));
    }



}
