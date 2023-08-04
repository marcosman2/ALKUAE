package Base;

import Pages.LastPassPage;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.lang.reflect.Field;

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

    public static final String bsUser = "marcosmanrique_d8GUFe";
    public static final String bsPwd = "TpaWeDxW8q5ZFwuYidwV";
    public static final String URL = "https://"+bsUser+":"+bsPwd+"@hub-cloud.browserstack.com/wd/hub";

    public static void initialization() {

        try{

            driver = WebDriverSingleton.getInstance();
            //driver.manage().window().setPosition(new Point(1920, 0));
			driver.manage().window().setSize(new Dimension(1920, 1080));
            driver.manage().window().maximize();
            driver.get("https://alku-uat.adkalpha.com/");
        }
        catch(Exception e){

            TestReport.logFail("Issue trying to navigate to ALKU AE");
        }
    }

    public static void browserStackInitialization() throws MalformedURLException {

        String URL = "https://"+bsUser+":"+bsPwd+"@hub-cloud.browserstack.com/wd/hub";

        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("latest");
        Map<String, Object> cloudOptions = new HashMap<>();
        cloudOptions.put("name", "Alku Everywhere smoke test");
        browserOptions.setCapability("cloud:options", cloudOptions);

        driver = new RemoteWebDriver(new URL(URL), browserOptions);
        driver.manage().window().maximize();
        driver.get("https://alku-uat.adkalpha.com/");
    }

    public boolean waitForDisplayed(WebElement element){

        wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.isDisplayed();
    }

    public void fillOutCredentials(HashMap<String, String> dataCredentials /*String lpUser, String lpPassword*/) {

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
			takeScreenshot("Evidences/WWABoard/WWA_verifyDealsMonthDate_AfterLP3.png");
            String authCode = pgLastPass.getOneTimeCode();
			takeScreenshot("Evidences/WWABoard/WWA_verifyDealsMonthDate_AfterLP4.png");

            driver.close();
            driver.switchTo().window(tabs.get(0));

            PageFactory.initElements(driver, this);
            waitForEnabled(onePassCode);
            type(onePassCode, authCode);
			takeScreenshot("Evidences/WWABoard/WWA_verifyDealsMonthDate_AfterLP3.png");
            clickElement(btnVerify);
			takeScreenshot("Evidences/WWABoard/WWA_verifyDealsMonthDate_AfterLP4.png");
        }
        catch(Exception e){

            TestReport.logFail("Issue trying to fill out credentials");
        }
    }

    public void logIntoApplication(Object[][] dpLogin, int row){

        HashMap<String, String> dataCredentials = (HashMap<String, String>) dpLogin[row][0];
        fillOutCredentials(dataCredentials);
    }

    /*private static String getWebElementName(WebElement element) {
        try {
            Field field = element.getClass().getDeclaredField("name");
            field.setAccessible(true);

            FindBy findByAnnotation = field.getAnnotation(FindBy.class);
            if (findByAnnotation != null) {
                return findByAnnotation.id(); // or you can use other attributes like name, xpath, etc.
            }
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        return null;
    }*/

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

        try{

            waitForDisplayed(element);
            waitAPause(2);

            Actions action = new Actions(driver);
            action.moveToElement(element).perform();
        }
        catch(Exception e){

            TestReport.logFail("Issue trying to apply a moseOver on element: "+element.toString());
        }
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
            throw new RuntimeException(e);
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
            System.out.println("Folder does not exist");
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

        try{

            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            String script = "arguments[0].style.backgroundColor = 'yellow';"
                    + "arguments[0].style.border = '2px solid red';";
            jsExecutor.executeScript(script, labelElement);
        }
        catch(Exception e){

            TestReport.logFail("Issue trying to highlight the element: "+ labelElement.toString());
        }
    }

}
