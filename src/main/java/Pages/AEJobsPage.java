package Pages;

import Base.TestReport;
import Base.Wrappers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AEJobsPage extends Wrappers {

    @FindBy(xpath = "//div[@class='filter-header-input']/div[contains(text(), 'Company')]")
    private WebElement lblCompany;

    @FindBy(xpath = "//div[@class='filter-header-input']/div[contains(text(), 'Company')]/parent::*/descendant::input")
    private WebElement ddCompany;

    @FindBy(xpath = "//div[@class='filter-header-input']/div[contains(text(), 'Tags')]/parent::div/descendant::div[@class='ant-select-selector']")
    private WebElement ddTags;

    @FindBy(xpath = "(//span[contains(text(), 'Multi-Division')]/preceding-sibling::*/div)[1]")
    private WebElement btnMultiDivision;

    @FindBy(xpath = "//button[contains(@class, 'view-history')]")
    private WebElement btnJobHistory;

    @FindBy(xpath = "(//div[@class='job-block-actions'])[1]/descendant::p[text()=' TOGGLE TC']")
    private WebElement btnToggleTC;

    @FindBy(xpath = "(//div[@class='job-block-actions'])[1]/descendant::p[text()=' SHOW/WIPE CANDIDATES']")
    private WebElement btnShowWipeCandidates;

    @FindBy(xpath = "(//div[@class='job-block-actions'])[1]/preceding::div[@class='job-block']/preceding-sibling::div/div[@class='toptab rtc']/p[text()='TC']")
    private WebElement tabTC;

    @FindBy(xpath = "//div[@class='popover-block']/*[contains(text(), 'History')]")
    private WebElement popupHistory;

    @FindBy(xpath = "//div[@class='item-label has-children' and contains(text(), 'Hiring Manager')]")
    private WebElement lblHistoryManager;

    @FindBy(xpath = "//div[@class='item-label left' and contains(text(), 'Jobs')]")
    private WebElement lblHistoryJobs;

    @FindBy(xpath = "//div[@class='item-label left' and contains(text(), 'Hires')]")
    private WebElement lblHistoryHires;

    @FindBy(xpath = "//div[@class='item-label has-children' and contains(text(), 'Interviews')]")
    private WebElement lblHistoryInterviews;

    boolean isJobsDisplayed = false;

    boolean recordsExpected = false;
    int record;
    boolean areEqual = false;
    boolean isHistory = false;
    boolean isTabNotDisplayed = true;
    boolean isTabDisplayed = false;
    boolean isWiped = false;
    boolean isShown = false;

    private final String recordsXpath = "//div[@class='c-menu__popover-pointer']";

    public AEJobsPage(){

        PageFactory.initElements(driver, this);
    }

    public boolean isJobsBoardDisplayed(){

        waitForDisplayed(lblCompany, "Issue trying to navigate to Jobs board");
        isJobsDisplayed = true;
        TestReport.logInfo("Navigated to Jobs board");

        return isJobsDisplayed;
    }

    public void selectCompany(String company){

        clickElement(ddCompany);
        clickElement(driver.findElement(By.xpath("//div[@title='"+company+"']")));
        TestReport.logInfo("Selected Company: "+company);
    }

    public String[] getRecordsCompanyName(){

        waitAPause(2);

        List<WebElement> records = driver.findElements(By.xpath(recordsXpath));
        String[] recordsCompany = new String[records.size()];

        for(record = 0; record < records.size(); record++){

            recordsCompany[record] = records.get(record).getText();
            highlightLabel(records.get(record));
        }

        return recordsCompany;
    }

    public boolean allCompanyNamesEqual(String[] companies, String expectedCompany){

        areEqual = true;
        record = 0;

        while (areEqual && record < companies.length){

            if(!companies[record].equalsIgnoreCase(expectedCompany)){

                areEqual = false;
                TestReport.logFail("Failed - Not all displayed records belong to selected Company");
                break;
            }
            record++;
        }

        return areEqual;
    }

    public void selectMultiDivisionOnly(){

        clickElement(btnMultiDivision);
        waitAPause(1);
        TestReport.logInfo("Clicked on Multidivision");
    }

    public boolean areAllMultiDivisionRecords(){

        recordsExpected = false;

        List<WebElement> records = driver.findElements(By.xpath(recordsXpath));
        List<WebElement> multiDivisionTabs = driver.findElements(By.xpath("//div[@class='toptab multi-division']"));

        if(records.size() == multiDivisionTabs.size()){

            recordsExpected = true;
        }
        else{

            TestReport.logFail("Failed - Not all displayed records are Multidivision");
        }

        for(WebElement tabs: multiDivisionTabs){

            highlightLabel(tabs);
        }

        return recordsExpected;
    }

    public void viewJobHistory(){

        clickElement(btnJobHistory);
        TestReport.logInfo("Clicked on Job History");
    }

    public boolean isHistoryPopupDisplayed(){

        if(waitForDisplayed(popupHistory, "'Job History' popup not displayed") && waitForDisplayed(lblHistoryManager, "History Manager not displayed")
            && waitForDisplayed(lblHistoryJobs, "Job History not displayed") && waitForDisplayed(lblHistoryHires, "Hires History not displayed")
                && waitForDisplayed(lblHistoryInterviews, "Interviews History not displayed")){

            isHistory = true;
        }
        else{

            TestReport.logFail("Failed - Job History popup not displayed");
        }

        return isHistory;
    }

    public void clickToggleTC(){

        clickElement(btnToggleTC);
        TestReport.logInfo("Clicked on Toggle TC");
    }

    public boolean isTCTabNotDisplayed() {

        try{

            if(tabTC.isDisplayed()){

                isTabNotDisplayed = false;
            }
        }
        catch(Exception e){
            e.getMessage();
        }

        return isTabNotDisplayed;
    }



    public boolean isTCTabDisplayed() {

        if(isTCTabNotDisplayed()){

            clickToggleTC();

            if(waitForDisplayed(tabTC, "TC tab not displayed")){

                isTabDisplayed = true;
                highlightLabel(tabTC);
            }
        }
        else{

            TestReport.logFail("Failed - TC tab already displayed");
        }

        return isTabDisplayed;
    }

    public void selectTag(String tag){

        clickElement(ddTags);
        clickElement(driver.findElement(By.xpath("//div[@class='ant-select-item-option-content' and contains(text(), '"+tag+"')]")));
        TestReport.logInfo("Selected tag: "+tag);
    }

    public boolean areAllTCRecords(){

       List<WebElement> records = driver.findElements(By.xpath("//div[@class='c-menu__popover-pointer']"));
       List<WebElement> tcTabs = driver.findElements(By.xpath("//div[@class='tabs-container']/descendant::p[contains(text(), 'TC')]"));

       if(records.size() == tcTabs.size()){

           recordsExpected = true;
       }
       else{

           TestReport.logFail("Failed - Not all displayed records are TC");
       }

       for(WebElement element: tcTabs){

           highlightLabel(element);
       }

        return recordsExpected;
    }

    public void showWipeCandidate(String firstName, String lastName){

        clickElement(btnShowWipeCandidates);
        waitAPause(2);
        clickElement(driver.findElement(By.xpath("//span[@class='candidate-name' and text()='"+firstName+"' and text()='"+lastName+"']/parent::td/following-sibling::*/button")));
        driver.navigate().refresh();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='filter-header-input']/div[contains(text(), 'Company')]")));

    }

    public boolean isCandidateWiped(String firstName, String lastName){

       waitAPause(3);

       try {

           if (driver.findElement(By.xpath("//span[text()='" + firstName + "' and text()='" + lastName + "']")).isDisplayed()) {

               showWipeCandidate(firstName, lastName);
               TestReport.logInfo("Clicked on Wipe Candidate");

               try {

                   driver.findElement(By.xpath("//span[text()='" + firstName + "' and text()='" + lastName + "']")).isDisplayed();
                   TestReport.logFail("Failed - Issue trying to wipe Candidate");
               } catch (Exception e) {
                   isWiped = true;
               }
           }
       }
       catch(Exception e){

           TestReport.logFail("Failed - Candidate to be wiped not found");
       }

        return isWiped;
    }

    public boolean isCandidateShown(String firstName, String lastName){

        try{

            waitAPause(3);
            driver.findElement(By.xpath("//span[text()='"+firstName+"' and text()='"+lastName+"']")).isDisplayed();

        }
        catch(Exception e){

            showWipeCandidate(firstName, lastName);
            TestReport.logInfo("Clicked on Show Candidate");

            try {

                driver.findElement(By.xpath("//span[text()='" + firstName + "' and text()='" + lastName + "']")).isDisplayed();
                isShown = true;
                highlightLabel(driver.findElement(By.xpath("//span[text()='" + firstName + "' and text()='" + lastName + "']")));
            }
            catch (Exception j){
                TestReport.logFail("Failed - Candidate not shown");
            }
        }
        return isShown;
    }
}