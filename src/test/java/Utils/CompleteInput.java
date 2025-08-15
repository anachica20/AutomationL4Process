package Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CompleteInput {

    public static void selectById(WebDriverWait wait, String id, String value){
        By locator = By.id(id);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(value);
    }

    public static void selectByName (WebDriverWait wait, String name, String value){
        By locator = By.name(name);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(value);
    }

    public static void sendKeysByAriaLabel(WebDriverWait wait, String ariaLabel, String value) {
        By locator = By.cssSelector("input[aria-label='" + ariaLabel + "']");
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(locator));
        input.clear();
        input.sendKeys(value);
    }

    public static void selectByCss(WebDriverWait wait, String cssSelector, String valueToSend) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(cssSelector)
        ));
        input.sendKeys(valueToSend);
    }

    public static void selectByLabel(WebDriverWait wait, WebDriver driver, String labelText, String valueToSend) {
        // Espera a que aparezca el label con ese texto normalizado (evita espacios ocultos)
        WebElement label = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//mat-form-field//label[normalize-space(text())='" + labelText + "']")
        ));

        // Asegura que esté en pantalla
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", label);

        // Ahora encuentra el input asociado a ese label
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//mat-form-field[.//label[normalize-space(text())='" + labelText + "']]//input")
        ));

        // Envía el texto
        input.sendKeys(valueToSend);
    }

    public static void inputByLabel(WebDriverWait wait, WebDriver driver, String labelText, String valueToSend) {
        // Espera y encuentra el input basado en mat-label
        String xpathInput = "//mat-form-field[.//mat-label[contains(normalize-space(.), '" + labelText + "')]]//input";

        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathInput)));

        // Scroll al input
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);

        // Limpia y envía
        input.clear();
        input.sendKeys(valueToSend);

        System.out.println("✅ Campo '" + labelText + "' completado con: " + valueToSend);
    }





    public static void sendCurrentDate(WebDriverWait wait) {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        String fecha = hoy.format(formatter);
        fecha = fecha.substring(0, 1).toLowerCase() + fecha.substring(1);

        By locator = By.cssSelector("td[aria-label='" + fecha + "'] div.mat-calendar-body-cell-content");
        WebElement dia = wait.until(ExpectedConditions.elementToBeClickable(locator));
        dia.click();
    }

    public static void selectAutocompleteByLabelAndOption(WebDriver driver, WebDriverWait wait, String labelText, String textoCompleto) {
        try {
            // Localiza el input basado en el label asociado
            String inputXPath = "//label[.//mat-label[contains(normalize-space(.), '" + labelText + "')]]/ancestor::mat-form-field//input";

            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(inputXPath)));

            // Scroll + escribir el valor completo
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", input);
            input.clear();
            input.sendKeys(textoCompleto);

            // Espera y selecciona la opción visible con ese texto exacto
            String opcionXPath = "//mat-option//span[normalize-space(text())='" + textoCompleto + "']";
            WebElement opcion = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(opcionXPath)));
            opcion.click();

            System.out.println("✅ Autocompletado '" + textoCompleto + "' en campo '" + labelText + "'");

        } catch (Exception e) {
            System.out.println("❌ Error al autocompletar '" + textoCompleto + "' para '" + labelText + "'");
            e.printStackTrace();
        }
    }

}