package Utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class UploadDocuments {

    public static void subirArchivoOculto(WebDriver driver, WebDriverWait wait, String ruta) {
        WebElement inputFile = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='file']")));
        inputFile.sendKeys(ruta);
    }


}
