import Utils.EnvConfig;
import Utils.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SupplierApproval {

    Settings settings = new Settings();
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void testLoginPayer() {
        Login.loginEnterprise(driver, wait, EnvConfig.get("EMAIL_AGRICOLA_QA"), EnvConfig.get("PASSWORD_PAGADOR"), settings.baseUrl);
    }

    @AfterEach
    public void tearDown() {
        //if (driver != null) {
        //driver.quit();
        //}
    }
}
