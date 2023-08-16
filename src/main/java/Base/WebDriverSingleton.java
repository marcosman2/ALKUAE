package Base;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverSingleton {

    private static WebDriver driver;

    private WebDriverSingleton(){

    }

    public static WebDriver getInstance() {

        if (driver == null) {

            System.setProperty("webdriver.chrome.driver", "Drivers/chromedriver.exe");

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");

            Dimension screenSize = new Dimension(1920, 1080);
            options.addArguments("--window-size=" + screenSize.getWidth() + "," + screenSize.getHeight());

            driver = new ChromeDriver(options);
        }

        return driver;
    }

    public static WebDriver setInstanceToNull(){

        return driver = null;
    }
}
