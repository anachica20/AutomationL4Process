package Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login {

    public static void loginEnterprise (WebDriver driver, WebDriverWait wait, String username, String password, String baseUrl){
        driver.get( baseUrl + "sign-in");
        CompleteInput.selectById(wait,"mat-input-0",username);
        CompleteInput.selectById(wait,"mat-input-1",password);
        ClicButton.clicByLabel(driver, wait, "Iniciar sesión");
        //UrlChange.validateUrl(driver,wait, baseUrl + "sign-in",  baseUrl + "enterprise");
    }

    public static void LoginNewProvider(WebDriver driver, WebDriverWait wait, String username, String password, String newPassword, String baseUrl){
        driver.get( baseUrl + "sign-in");
        CompleteInput.selectById(wait,"mat-input-0",username);
        CompleteInput.selectById(wait,"mat-input-1",password);//password
        ClicButton.clicByLabel(driver,wait,"Iniciar sesión");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Confirmación de cuenta')]")));
        UrlChange.validateUrl(driver,wait, baseUrl + "sign-in",  baseUrl + "complete-new-password?email="+ username);

        CompleteInput.selectByName(wait,"password", newPassword);
        CompleteInput.selectByName(wait,"confirm-password", newPassword);
        ClicButton.clicByLabel(driver, wait, "Actualizar");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Iniciar sesión')]")));
        UrlChange.validateUrl(driver,wait, baseUrl + "complete-new-password?email="+ username,  baseUrl + "sign-in");
        RetryUtils.retryUntilSuccess(() -> {
            loginEnterprise(driver, wait, username, newPassword, baseUrl);
            return null;
        },3, 5000);




    }
}
