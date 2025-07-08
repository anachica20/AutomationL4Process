package Utils;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class UrlChange {

    public static void validateUrl(WebDriver driver, WebDriverWait wait, String previousUrl, String expectedUrl ){

        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(previousUrl)));
        Assertions.assertEquals(expectedUrl, driver.getCurrentUrl());
    }

    public static void waitNewUrl (WebDriver driver, WebDriverWait wait, String expectedUrl){
        wait.until(ExpectedConditions.urlToBe(expectedUrl));
        Assertions.assertEquals(expectedUrl, driver.getCurrentUrl(),
        "La URL actual no coincide con la esperada después de la navegación.");
    }

    public static void switchToNewTabAndValidateUrl(WebDriver driver, String expectedUrl) {
        // Guardamos la pestaña original
        String originalWindow = driver.getWindowHandle();

        // Esperamos a que se abra una nueva pestaña (cuando hay más de una)
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver1 -> driver1.getWindowHandles().size() > 1);

        // Iteramos sobre las pestañas abiertas
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                // Cambiamos el foco a la nueva pestaña
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Validamos que la URL de la nueva pestaña sea la esperada
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe(expectedUrl));

        Assertions.assertEquals(expectedUrl, driver.getCurrentUrl());
    }

    public static void waitForSnackBarMessage(WebDriverWait wait, String expectedText, boolean mustDisappear) {
        // Asumiendo que el texto está en el componente simple-snack-bar
        By snackBarLocator = By.cssSelector("simple-snack-bar");

        // Espera a que aparezca el snack-bar con el texto deseado
        wait.until(ExpectedConditions.textToBePresentInElementLocated(snackBarLocator, expectedText));

        // Opcional: esperar a que desaparezca
        if (mustDisappear) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(snackBarLocator));
        }
    }

    public static boolean isModalTextVisible(WebDriver driver, WebDriverWait wait, String textoEsperado) {
        try {
            By xpath = By.xpath("//h2[@mat-dialog-title and normalize-space(text())='" + textoEsperado + "']");
            WebElement modalTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
            return modalTitle.isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("❌ El texto del modal no se encontró: " + textoEsperado);
            return false;
        }
    }



}
