package Base;

import pages.AEHomePage;
import pages.LastPassPage;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wrappers {

    protected static WebDriver driver;
    private WebDriverWait wait;
    static XLSReader reader;

    @FindBy(id="i0116")
    private WebElement userName;

    @FindBy(id="i0118")
    private WebElement password;

    @FindBy(id="idSIButton9")
    private WebElement btnNext;

    @FindBy(id="idTxtBx_SAOTCC_OTC")
    private WebElement onePassCode;

    @FindBy(id="idSubmit_SAOTCC_Continue")
    private WebElement btnVerify;

    public static final String BS_USER = "marcosmanrique_d8GUFe";
    public static final String BS_PWD = "TpaWeDxW8q5ZFwuYidwV";
    public static final String URL = "https://"+ BS_USER +":"+ BS_PWD +"@hub-cloud.browserstack.com/wd/hub";

    public static void initialization() {

        driver = WebDriverSingleton.getInstance();
        driver.manage().window().maximize();
        driver.get("https://alku-uat.adkalpha.com/");
    }

    public static void browserStackInitialization() throws MalformedURLException {

        String url = "https://"+ BS_USER +":"+ BS_PWD +"@hub-cloud.browserstack.com/wd/hub";

        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("latest");
        Map<String, Object> cloudOptions = new HashMap<>();
        cloudOptions.put("name", "Alku Everywhere smoke test");
        browserOptions.setCapability("cloud:options", cloudOptions);

        driver = new RemoteWebDriver(new URL(url), browserOptions);
        driver.manage().window().maximize();
        driver.get("https://alku-uat.adkalpha.com/");
    }

    public boolean waitForDisplayed(WebElement element, String errorMessage){

        boolean isElement = false;

        try{

            wait = new WebDriverWait(driver, Duration.ofSeconds(45));
            wait.until(ExpectedConditions.visibilityOf(element));
            isElement = true;
        }
        catch(Exception e){

            TestReport.logFail("Failed - "+errorMessage);
        }

        return isElement;
    }

    public void fillOutCredentials(HashMap<String, String> dataCredentials) {

        try {

            PageFactory.initElements(driver, this);

            waitForEnabled(userName);
            type(userName, dataCredentials.get("AE_User"));
            clickElement(btnNext);

            waitAPause(1);
            PageFactory.initElements(driver, this);
            waitForEnabled(password);
            type(password, dataCredentials.get("AE_Password"));
            clickElement(btnNext);

            driver.switchTo().newWindow(WindowType.TAB);

            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1));
            driver.get("https://lastpass.com/");

            PageFactory.initElements(driver, this);
            LastPassPage pgLastPass = new LastPassPage();
            pgLastPass.logIntoLastPass(dataCredentials.get("LP_User"), dataCredentials.get("LP_Password"));
            String authCode = pgLastPass.getOneTimeCode();

            driver.close();
            driver.switchTo().window(tabs.get(0));

            PageFactory.initElements(driver, this);
            waitForEnabled(onePassCode);
            type(onePassCode, authCode);
            clickElement(btnVerify);
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying logging into the application");
        }

    }

    public boolean logIntoApplication(Object[][] dpLogin, int row){

        HashMap<String, String> dataCredentials = (HashMap<String, String>) dpLogin[row][0];
        fillOutCredentials(dataCredentials);

        AEHomePage pgHome = new AEHomePage();

        if(pgHome.waitForHomeToDisplays()){

            TestReport.logInfo("Navigated to ALKU AE");
            return true;
        }
        else{

            return false;
        }
    }

    public boolean waitForEnabled(WebElement element){

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        return element.isEnabled();
    }

    public void clickElement(WebElement element){

        waitForEnabled(element);
        element.click();
    }

    public void type(WebElement element, String text){

        waitForEnabled(element);
        element.sendKeys(text);
    }

    public void mouseOver(WebElement element){

        waitForDisplayed(element, "Expected element not displayed");
        waitAPause(2);

        Actions action = new Actions(driver);
        action.moveToElement(element).perform();
    }

    public static void finalization() {

        TestReport.flushReport();
        driver.quit();
        driver = WebDriverSingleton.setInstanceToNull();
    }

    public static Object[][] loadExcelTable(String tab) {

        reader = new XLSReader("TestData/TestData.xlsx");

        int totalRowCnt = reader.getRowCount(tab);
        int totalColCnt = reader.getColumnCount(tab);
        Object[][] data = new Object[totalRowCnt-1][1];

        ArrayList<String> colNames = new ArrayList<String>();

        for(int colCntr=0;colCntr<totalColCnt;colCntr++){
            colNames.add(reader.getCellData(tab, colCntr, 1).trim());
        }

        for(int rCntr=0;rCntr<totalRowCnt-1; rCntr++){
            HashMap<String, String> inpRec = new HashMap<String, String>();

            for (int colCntr=0;colCntr<totalColCnt;colCntr++){
                inpRec.put(colNames.get(colCntr), reader.getCellData(tab, colNames.get(colCntr), rCntr+2));
            }

            data[rCntr][0]=inpRec;
        }
        return data;
    }

    public void waitAPause(int seconds){

        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void takeScreenshot(String filePath){

        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(filePath);

        try {

            FileHandler.copy(srcFile, destinationFile);
        }
        catch (Exception e) {

            e.getMessage();
        }
    }

    public static void deleteScreenshots(String folderPath){

        File directory = new File(folderPath);

        if (directory.exists() && directory.isDirectory()) {

            File[] files = directory.listFiles();

            if (files != null) {

                for (File file : files) {
                    file.delete();
                }
            }
        } else {
            TestReport.logInfo("Folder does not exist");
        }
    }

    public static void scrollToElement(WebElement element) {

        try{

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", element);
        }
        catch(Exception e){

            TestReport.logFail("Issue trying to scroll to the element: "+element.toString());
        }
    }

    public static void highlightLabel(WebElement labelElement) {

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String script = "arguments[0].style.backgroundColor = 'yellow';"
           + "arguments[0].style.border = '2px solid red';";
        jsExecutor.executeScript(script, labelElement);
    }

}
