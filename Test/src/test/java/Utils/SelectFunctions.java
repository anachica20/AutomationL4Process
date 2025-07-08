package Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class SelectFunctions {

    public static String selectListOption(WebDriver driver, WebDriverWait wait, String nameSelect, String selectedOption, boolean isOptional) {
        System.out.println("Starting selectListOption" + nameSelect);

        By selectOptions = By.xpath("//mat-select[@formcontrolname='" + nameSelect + "']");
        List<WebElement> elements = driver.findElements(selectOptions);

        System.out.println("ELEMENTO: "+elements);

        if (elements.isEmpty() || !elements.get(0).isDisplayed()) {
            if (isOptional) {
                System.out.println("Elemento " + nameSelect + " es opcional y no está visible. Se omite.");
                System.out.println("Finish selectListOption");
                return "Omitido (opcional no visible)";
            } else {
                throw new RuntimeException("Elemento obligatorio '" + nameSelect + "' no está visible.");
            }
        }

        WebElement matSelect = wait.until(ExpectedConditions.elementToBeClickable(selectOptions));
        matSelect.click();

        try {
            Thread.sleep(800); // opcional: mejor con espera explícita si lo optimizas luego
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("mat-option span")));

        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(selectedOption)) {
                option.click();
                System.out.println("Ending selectListOption");
                return selectedOption;
            }
        }

        return "Opción " + selectedOption + " no encontrada";

        // Versión simplificada: por defecto obligatorio

    }

    //solo aplica para los campos obligatorios
    public static String selectListOption(WebDriver driver, WebDriverWait wait, String nameSelect, String selectedOption){
            return selectListOption(driver, wait, nameSelect, selectedOption, false);
    }


    public static String selectByLabel(WebDriverWait wait, String labelText, String selectedOption) {
        WebElement matSelect = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//mat-label[normalize-space()='" + labelText + "']/ancestor::label/following-sibling::div//mat-select")));
        matSelect.click();
        try { Thread.sleep(800); } catch (InterruptedException e) { }
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("mat-option span")));
        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(selectedOption)) {
                option.click();
                return selectedOption;
            }
        }
        return "Opción " + selectedOption + " no encontrada";
    }

    public static String selectListOptionById(WebDriverWait wait, String idSelect, String selectedOption) {
        By selectOptions = By.id(idSelect);
        WebElement matSelect = wait.until(ExpectedConditions.elementToBeClickable(selectOptions));
        matSelect.click();
        // Espera breve (mejor usar esperas explícitas si el DOM es lento)
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("mat-option span")));
        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(selectedOption)) {
                option.click();
                return selectedOption;
            }
        }
        return "Opción " + selectedOption + " no encontrada";
    }

    public static void selectListOptionByXPath(WebDriver driver, WebDriverWait wait, String labelText, String opcionVisible) {
        try {
            // Paso 1: encontrar el mat-select basado en el texto del mat-label
            String selectXPath = "//mat-form-field[.//mat-label[contains(normalize-space(.), '" + labelText + "')]]//mat-select";
            WebElement select = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(selectXPath)));

            // Scroll y clic para abrir el select
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", select);
            Thread.sleep(300); // esperar a que se estabilice
            select.click();

            // Paso 2: esperar explícitamente a que el panel de opciones se abra
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("mat-select-panel")));

            // Paso 3: esperar la opción deseada dentro del panel y hacer clic
            String opcionXPath = "//mat-option//span[contains(normalize-space(.), '" + opcionVisible + "')]";
            WebElement opcion = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(opcionXPath)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", opcion);
            Thread.sleep(200); // opcional, por seguridad
            opcion.click();

            System.out.println("✅ Se seleccionó: '" + opcionVisible + "' en '" + labelText + "'");

        } catch (Exception e) {
            System.out.println("❌ Error al seleccionar '" + opcionVisible + "' para '" + labelText + "'");
            e.printStackTrace();
        }
    }

    public static void selectListOptionByLabel(WebDriver driver,WebDriverWait wait,String labelText,String opcionVisible) {
        // 1) Encuentra el mat-select buscando el mat-label con el texto dado
        WebElement matSelect = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath(
                        "//mat-label[normalize-space(text())='" + labelText + "']" +
                                "/ancestor::mat-form-field//mat-select"
                )
        ));

        // 2) Scroll + clic para abrir la lista
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", matSelect);
        matSelect.click();

        // 3) Espera a que el panel de opciones abra y elige la opción
        WebElement opcion = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//mat-option//span[normalize-space(text())='" + opcionVisible + "']")
        ));
        opcion.click();
    }



}
