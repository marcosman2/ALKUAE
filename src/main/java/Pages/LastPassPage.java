package Pages;

import Base.TestReport;
import Base.Wrappers;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LastPassPage extends Wrappers {

    @FindBy(xpath="//h3[contains(text(), 'Log In')]")
    private WebElement linkLogIn;

    @FindBy(xpath="(//input[@name='username'])[2]")
    private WebElement txtUsername;

    @FindBy(xpath="(//input[@name='password'])[2]")
    private WebElement txtPassword;

    @FindBy(xpath="//button[@type='submit']")
    private WebElement btnLogIn;

    @FindBy(xpath="//input[@id='vaultSearch']")
    private WebElement txtSearch;

    @FindBy(xpath="//p[@class='name' and contains(., 'ALKU Azure Test User')]")
    private WebElement containerPassword;

    @FindBy(xpath="//iframe[@id='newvault']")
    private WebElement iframe;

    @FindBy(xpath="(//div[@data-alert-id='8840533688358952605'])[2]/following-sibling::div/button[@vaultaction='edit']")
    private WebElement iconEdit;

    String code = "";



    public LastPassPage(){

        PageFactory.initElements(driver, this);
    }

    public void logIntoLastPass(String user, String pwd){

        try{

            clickElement(driver.findElement(By.xpath("//div[@class='lp-header__mobile-menu']")));
            			
			takeScreenshot("Evidences/WWABoard/WWA_verifyDealsMonthDate_AfterLP.png");
			
			clickElement(linkLogIn);
			takeScreenshot("Evidences/WWABoard/WWA_verifyDealsMonthDate_AfterLP2.png");
            type(txtUsername, user);
            type(txtPassword, pwd);
            clickElement(btnLogIn);
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to log into LastPass");
        }
    }

    public String getOneTimeCode(){

        try{

            waitForDisplayed(iframe);
            driver.switchTo().frame("newvault");
            type(txtSearch,"ALKU Azure Test User");
            mouseOver(containerPassword);
            clickElement(iconEdit);
            waitAPause(2);

            WebElement shadowHost = driver.findElement(By.cssSelector("#reactOverlayContent > div"));
            SearchContext shadowRootElement =(SearchContext) ((JavascriptExecutor)driver).executeScript("return arguments[0].shadowRoot", shadowHost);
            code = shadowRootElement.findElement(By.cssSelector(".legacy-vault-dialog-portal-zeboxj.e9y86r134 > [type='password']")).getAttribute("value");
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to get the One Time code");
        }

        return code;
    }
}
