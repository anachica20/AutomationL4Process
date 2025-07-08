package Utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddComercialReference {



    public static void addCommercialReference (WebDriver driver, WebDriverWait wait) {

        ClicButton.clicByButtonSpanText(driver, wait, "Agregar referencia");

        RetryUtils.retryUntilSuccess(() -> {
            CompleteInput.inputByLabel(wait, driver,"Nombre o Razón social", "Nombre o Razon Social");
            return null;
        }, 3, 9000);
        RetryUtils.retryUntilSuccess(() -> {
            CompleteInput.inputByLabel(wait, driver, "Nombre persona de contacto", "Persona de Contacto");
            return null;
        }, 3, 9000);

        RetryUtils.retryUntilSuccess(() -> {
            CompleteInput.inputByLabel(wait, driver,"Teléfono de contacto", "3000000000");
            return null;
        }, 3, 9000);

        RetryUtils.retryUntilSuccess(() -> {
            CompleteInput.inputByLabel(wait, driver,"Correo electrónico", "correoref@yopmail.com");
            return null;
        }, 3, 9000);

        RetryUtils.retryUntilSuccess(() -> {
            ClicButton.clicByCssSelector(driver, wait, "mat-dialog-container > commercial-reference-form > mat-dialog-content > form > div:nth-child(3) > button");
            return null;
        }, 3, 9000);
    }
}
