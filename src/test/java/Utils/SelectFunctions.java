package Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
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

    public static void selectAutocompleteByLabelAndOption(WebDriver driver, WebDriverWait wait, String labelText, String textoCompleto) {
        try {
            // XPath base: mat-label -> sube a mat-form-field -> busca input
            String inputXPath = "//mat-label[normalize-space(text())='" + labelText + "']" +
                    "/ancestor::mat-form-field//input";

            // 1️⃣ Esperar que el input esté visible y clickeable
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(inputXPath)));

            // 2️⃣ Scroll al campo
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", input);

            // 3️⃣ Limpiar y escribir el texto
            input.clear();
            input.sendKeys(textoCompleto);

            // 4️⃣ Esperar que aparezca la opción exacta
            String opcionXPath = "//mat-option//span[normalize-space(text())='" + textoCompleto + "']";
            WebElement opcion = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(opcionXPath)));

            // 5️⃣ Clic en la opción
            opcion.click();

            System.out.println("✅ Autocompletado '" + textoCompleto + "' en campo '" + labelText + "'");
        } catch (Exception e) {
            System.out.println("❌ Error al autocompletar '" + textoCompleto + "' en '" + labelText + "'");
            e.printStackTrace();
            throw e; // Lo relanzo para que retry o junit lo detecte
        }
    }


    public static void selectListOptionByXPath(WebDriver driver, WebDriverWait wait, String labelText, String opcionVisible) {
        try {
            // 1️⃣ Localizar el mat-select por la etiqueta del mat-label
            String selectXPath = "//mat-form-field[.//mat-label[normalize-space()='" + labelText + "']]//mat-select";
            WebElement select = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(selectXPath)));

            // Scroll y click por JavaScript (más seguro que click normal)
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", select);
//            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", select);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", select);
            select.click();

            // 2️⃣ Esperar a que el panel aparezca (está fuera del mat-form-field en cdk-overlay-container)
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mat-select-panel")));

            // 3️⃣ Localizar y hacer click en la opción deseada
            String opcionXPath = "//mat-option//span[normalize-space()='" + opcionVisible + "']";
            WebElement opcion = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(opcionXPath)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", opcion);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opcion);

            // 4️⃣ Esperar que el mat-select ya tenga el valor seleccionado (opcional)
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.xpath(selectXPath + "//span[contains(@class,'mat-select-value-text')]"), opcionVisible
            ));

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

    public static void selectMatOption(WebDriver driver, WebDriverWait wait, String labelText, String opcionVisible) {
        String selectXPath = "//mat-form-field[.//mat-label[normalize-space()='" + labelText + "']]//mat-select";

        for (int intento = 1; intento <= 3; intento++) {
            try {
                // 1️⃣ Localizar el mat-select
                WebElement select = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(selectXPath)));

                // 2️⃣ Scroll y primer click con JS
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", select);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", select);

                // 3️⃣ Si no abre, intentar click normal
                try { select.click(); } catch (Exception ignored) {}

                // 4️⃣ Esperar panel abierto (máx 2 seg para evitar bloqueos)
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
                WebElement panel = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mat-select-panel")));

                // 5️⃣ Seleccionar la opción
                String opcionXPath = "//mat-option//span[normalize-space()='" + opcionVisible + "']";
                WebElement opcion = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(opcionXPath)));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", opcion);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opcion);

                // 6️⃣ Verificar que quedó seleccionado
                wait.until(ExpectedConditions.textToBePresentInElementLocated(
                        By.xpath(selectXPath + "//span[contains(@class,'mat-select-value-text')]"), opcionVisible
                ));

                System.out.println("✅ Se seleccionó '" + opcionVisible + "' en '" + labelText + "' correctamente.");
                return; // Salir si todo bien

            } catch (Exception e) {
                System.out.println("⚠️ Intento " + intento + " fallido para '" + labelText + "' → " + e.getMessage());
            }
        }

        throw new RuntimeException("❌ No se pudo seleccionar '" + opcionVisible + "' para '" + labelText + "' después de 3 intentos.");
    }




}
