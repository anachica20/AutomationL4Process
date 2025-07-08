package Utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;


public class ClicButton {

    public static void clicByLabel (WebDriver driver, WebDriverWait wait, String label) {
        WebElement selectedButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='" + label + "']")));
        selectedButton.click();
    }

    public static void clicByAriaLabel(WebDriver driver, WebDriverWait wait, String ariaLabel) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@aria-label='" + ariaLabel + "']")));
        button.click();
    }

    public static void clicByHiddenLabel (WebDriver driver, WebDriverWait wait, String label) {
        WebElement selectedButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[normalize-space(text())='" + label + "']]")));
        selectedButton.click();
    }


    public static void clicById(WebDriver driver, WebDriverWait wait, String id) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
        element.click();
    }


    public static void clicByIcon (WebDriver driver, WebDriverWait wait, String iconName) {
        WebElement selectedButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//mat-icon[contains(text(),'" + iconName + "')]]")));
        selectedButton.click();
    }

    public static void clicByButtonSpanText(WebDriver driver, WebDriverWait wait, String buttonText) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[normalize-space()='" + buttonText + "']]")));
        button.click();
    }

    public static void clicByLink (WebDriver driver, WebDriverWait wait, String linkTest) {
        WebElement enlace = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[img[@alt='" + linkTest + "']]")));
        enlace.click();
    }

    public static void clicByTitle(WebDriver driver, WebDriverWait wait, String titleText) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[title='" + titleText + "']")));
        element.click();
    }


    public static void clicByExpansionPanelTitle(WebDriver driver, WebDriverWait wait, String title) {
        // Espera a que el header sea clickeable
        WebElement expansionPanelHeader = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//mat-expansion-panel-header[.//mat-panel-title[normalize-space()='" + title + "']]")));

        // Clic en el header
        expansionPanelHeader.click();

        // Espera a que el atributo aria-expanded del header cambie a true
        wait.until(ExpectedConditions.attributeToBe(expansionPanelHeader, "aria-expanded", "true"));

        // Espera a que el contenido expandido sea visible (opcional si quieres confirmar)
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//mat-expansion-panel-header[.//mat-panel-title[normalize-space()='" + title + "']]/following-sibling::div[contains(@class, 'mat-expansion-panel-content')]")));
    }

    public static void clicByCssSelector(WebDriver driver, WebDriverWait wait, String cssSelector) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssSelector)));
        element.click();
    }

    public static void clicCheckboxPorTexto(WebDriver driver, WebDriverWait wait, String textoVisible) {
        WebElement checkboxContainer = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//mat-checkbox[.//span[contains(text(),'" + textoVisible + "')]]//div[contains(@class, 'mat-checkbox-inner-container')]")
        ));
        checkboxContainer.click();
    }

    public static void clicToggleConTextoPorIndice(WebDriver driver, WebDriverWait wait, String textoBoton, int indice) {
        try {
            List<WebElement> botones = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.xpath("//button[@type='button']//div[normalize-space(text())='" + textoBoton + "']/parent::button")
            ));

            if (botones.size() > indice) {
                WebElement boton = botones.get(indice);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", boton);
                Thread.sleep(200);

                if (!"true".equals(boton.getAttribute("aria-pressed"))) {
                    boton.click();
                    System.out.println("✅ Se hizo clic en el botón '" + textoBoton + "', índice " + indice);
                } else {
                    System.out.println("ℹ️ El botón ya estaba activo.");
                }
            } else {
                System.out.println("❌ No se encontró botón '" + textoBoton + "' en el índice " + indice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}

