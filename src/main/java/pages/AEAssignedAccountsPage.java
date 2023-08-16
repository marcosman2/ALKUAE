package pages;

import base.TestReport;
import base.Wrappers;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AEAssignedAccountsPage extends Wrappers {

    @FindBy(xpath = "//div[@class='react-switch-icon-container-text active']")
    private WebElement btnCompany;

    @FindBy(xpath = "//span[contains(text(), 'Select Company')]/parent::*")
    private WebElement ddSelectCompany;

    @FindBy(xpath = "(//input[@type='search'])[1]")
    private WebElement txtSelectCompany;

    @FindBy(xpath = "//span[contains(text(), 'Select AM')]/parent::*")
    private WebElement ddSelectAM;

    @FindBy(xpath = "(//input[@type='search'])[2]")
    private WebElement txtSelectAM;

    @FindBy(xpath = "//span[contains(text(), 'Select Division')]/parent::*")
    private WebElement ddSelectDivision;

    @FindBy(xpath = "(//input[@type='search'])[3]")
    private WebElement txtSelectDivision;

    @FindBy(xpath = "//span[contains(text(), 'Select Program')]/parent::*")
    private WebElement ddSelectProgram;

    @FindBy(xpath = "(//input[@type='search'])[4]")
    private WebElement txtSelectProgram;

    List<WebElement> records;
    Boolean areAllRecords = false;
    boolean isAADisplayed = false;

    public AEAssignedAccountsPage(){

        PageFactory.initElements(driver, this);
    }

    public boolean isAABoardDisplayed(){

        waitForDisplayed(btnCompany, "Issue trying to navigate to Assigned Accounts board");
        isAADisplayed = true;
        TestReport.logInfo("Navigated to Assigned Accounts board");

        return isAADisplayed;
    }

    public void selectFromDropdown(WebElement dd, WebElement input, String value){

        clickElement(dd);
        type(input, value);
        clickElement(driver.findElement(By.xpath("//div[@title='"+value+"']")));
        waitAPause(1);
        input.sendKeys(Keys.TAB);

        records = driver.findElements(By.xpath("//div[@id='assigned-accounts']/div[contains(@class, 'card')]"));
    }

    public void selectCompany(String company){

        selectFromDropdown(ddSelectCompany, txtSelectCompany, company);
        TestReport.logInfo("Selected Company: "+company);
    }

    public void selectAM(String accountManager){

        selectFromDropdown(ddSelectAM, txtSelectAM, accountManager);
        TestReport.logInfo("Selected Account Manager: "+accountManager);
    }

    public void selectDivision(String division){

       selectFromDropdown(ddSelectDivision, txtSelectDivision, division);
       TestReport.logInfo("Selected Division: "+division);
    }

    public void setDdSelectProgram(String program){

       selectFromDropdown(ddSelectProgram, txtSelectProgram, program);
       TestReport.logInfo("Selected Program: "+program);
    }

    public boolean allRecordsBelong(List<WebElement> list, String filter){

        for (WebElement element: list){

            highlightLabel(element);
        }

        waitAPause(1);

        if(records.size() == list.size()){

            areAllRecords = true;
        }
        else{

            TestReport.logFail("Failed - Not All displayed records belong to the selected ".concat(filter));
        }

        return areAllRecords;
    }

    public boolean allRecordsBelongToSelectedCompany(String company){

        List<WebElement> companyName = driver.findElements(By.xpath("//span[@class='c-menu__popover-pointer' and contains(text(), '"+company+"')]"));
        return allRecordsBelong(companyName, "Company");
    }

    public boolean allRecordsBelongToSelectedAM(String acFirstName, String acLastName){

        List<WebElement> accountManagers = driver.findElements(By.xpath("//div[contains(@style, '38')]/following-sibling::div[@class='header-username right' and text()='"+acFirstName+"' and text()='"+acLastName+"']"));
        return allRecordsBelong(accountManagers, "Account Manager");
    }

    public boolean allRecordsBelongToSelectedDivision(String division){

        List<WebElement> divisions = driver.findElements(By.xpath("//td[@class='assigned-accounts-card-table-cell' and contains(text(), '"+division+"')]"));
        return allRecordsBelong(divisions, "Division");
    }

    public boolean allRecordsBelongToSelectedProgram(String program){

        List<WebElement> programs = driver.findElements(By.xpath("//td[@class='assigned-accounts-card-table-cell' and contains(text(), '"+program+"')]"));
        return allRecordsBelong(programs, "Program");
    }
}
