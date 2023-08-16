package Pages;

import Base.TestReport;
import Base.Wrappers;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class AEActivityPage extends Wrappers {


    @FindBy(xpath = "//div[@class='activity-board__filters']/div[contains(text(), 'Display Timeframe')]")
    private WebElement lblDisplayTimeframe;

    @FindBy(xpath = "(//span[contains(text(), 'Add Job')])[1]")
    private WebElement btnAddJob;

    @FindBy(xpath = "//h2[contains(text(), 'Add Job')]")
    private WebElement lblAddJobModalTitle;

    @FindBy(xpath = "//h2[contains(text(), 'Add Candidate')]")
    private WebElement lblAddCandidateModalTitle;

    @FindBy(xpath = "//button[contains(@class, 'today')]")
    private WebElement btnCurrentDate;

    @FindBy(xpath = "(//button[contains(@class, 'today')]/parent::*/following-sibling::div/*)[1]")
    private WebElement btnNextDate;

    @FindBy(xpath = "//button[contains(@aria-label, ' 1, ')]")
    private WebElement btnFirstDay;

    @FindBy(xpath = "//button[text()='OK']")
    private WebElement btnOK;

    @FindBy(xpath = "//span[contains(text(), 'Select time')]")
    private WebElement lblSelectTime;

    @FindBy(xpath = "//div[@class='dialog-btn' and contains(text(), 'EST')]")
    private WebElement btnTimeZone;

    @FindBy(xpath = "//div[contains(text(), 'Phone')]")
    private WebElement btnPhone;

    @FindBy(xpath = "//div[@class='job-slot-header-leftbox']")
    private WebElement lblInterviewDateTime;

    @FindBy(xpath = "//button[contains(@class, 'add')]/following-sibling::div")
    private WebElement lblInterviewId;

    @FindBy(xpath = "(//button[@class='c-btn--responsive c-btn--pointer-cursor c-btn--bare'])[4]")
    private WebElement btnDeleteJob;

    @FindBy(xpath = "(//button[@class='c-btn--responsive c-btn--pointer-cursor c-btn--bare'])[5]")
    private WebElement btnAddCandidate;

    @FindBy(xpath = "//div[@class='jobs-container']")
    private WebElement panelJob;

    @FindBy(xpath = "//li[contains(text(), 'Remove From Slot')]")
    private WebElement optionRemoveFromSlot;

    @FindBy(xpath = "//li[contains(text(), 'View History')]")
    private WebElement optionViewHistory;

    @FindBy(xpath = "(//div[@class='job-id']/a)[1]")
    private WebElement linkJobId;

    @FindBy(xpath = "(//div[contains(@class, 'job-conf-line')])[1]")
    private WebElement linkConferenceLine;

    @FindBy(xpath = "(//span[contains(@class, 'job-conf-line')])[1]")
    private WebElement lblConferenceLine;

    @FindBy(xpath = "//input[@placeholder='1234']")
    private WebElement txtConferenceLine;

    @FindBy(xpath = "//input[@placeholder='Zoom']")
    private WebElement txtZoomDisplayedText;

    @FindBy(xpath = "//input[@placeholder='https://alku.zoom.us/']")
    private WebElement txtZoomLink;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement btnSaveConferenceLine;

    @FindBy(id = "rc-tabs-0-tab-2")
    private WebElement tabLink;

    @FindBy(id = "rc-tabs-0-tab-1")
    private WebElement tabLine;

    @FindBy(xpath = "//button[contains(text(), 'Today')]")
    private WebElement btnToday;

    @FindBy(xpath = "//button[contains(text(), 'Next Day')]")
    private WebElement btnNextDay;

    @FindBy(xpath = "//button[contains(text(), 'All')]")
    private WebElement btnAll;

    @FindBy(xpath = "//button[@aria-label='Next month']")
    private WebElement btnNextMonth;

    List<WebElement> jobsBefore;
    List<WebElement> jobsAfter;

    boolean isActivityDisplayed = false;

    boolean isAdded = false;
    boolean isDeleted = false;
    boolean isDisplayed = false;

    public AEActivityPage(){

        PageFactory.initElements(driver, this);
    }

    public boolean isActivityBoardDisplayed(){

        waitForDisplayed(lblDisplayTimeframe, "Issue trying to navigate to Activity board");
        isActivityDisplayed = true;
        TestReport.logInfo("Navigate to Activity board");

        return isActivityDisplayed;
    }

    public void clickOnAddJob(String recruiter){

        clickElement(driver.findElement(By.xpath("//*[contains(., '"+recruiter+"')]/following-sibling::div/div[@class='add-job']")));
        waitForDisplayed(lblAddJobModalTitle, "'Add Job' modal not displayed");
    }

    public void selectNextDate(){

        if(btnNextDate.getAttribute("class").contains("hidden")){

            clickElement(btnNextMonth);
            clickElement(btnFirstDay);
        }
        else{

            clickElement(btnNextDate);
        }
    }

    public void selectCompanyAndPosition(String company, String position){

        waitAPause(1);
        clickElement(driver.findElement(By.xpath("//div[@class='item-label' and text()= '"+company+"'  and text()='"+position+"']")));
    }

    public void selectDate(String date, String dayNumber){

        switch (date){

            case "Next":
                selectNextDate(); break;

            case "Current":
                clickElement(btnCurrentDate); break;

            default:
                clickElement(driver.findElement(By.xpath("//button[contains(@aria-label, '"+dayNumber+"')]")));
        }

        clickElement(btnOK);
        waitAPause(1);
    }

    public void selectTimeZoneAndMeetingWay(String timeZone, String phoneOrF2F){

        waitForDisplayed(lblSelectTime, "'Select Time' modal not displayed");
        clickElement(btnOK);
        waitForDisplayed(btnTimeZone, "'Select Timezone' modal not displayed");
        clickElement(driver.findElement(By.xpath("//div[@class='dialog-btn' and contains(text(), '"+timeZone+"')]")));
        waitForDisplayed(btnPhone, "'Phone or F2F' modal not displayed");
        clickElement(driver.findElement(By.xpath("//div[@class='dialog-btn' and contains(text(), '"+phoneOrF2F+"')]")));
        waitAPause(2);
    }

    public boolean isInterviewInformationDisplayed(String company, String position){

       try{

           String positionName = driver.findElement(By.xpath("//div[@class='job-header__job-title']")).getAttribute("innerText");
           highlightLabel(driver.findElement(By.xpath("//div[@class='job-header-leftbox']/descendant::span[contains(text(), '"+company+"')]")));

           if(waitForDisplayed(driver.findElement(By.xpath("//div[@class='job-header-leftbox']/descendant::span[contains(text(), '"+company+"')]")), "Company not displayed") &&
                   positionName.contains(position) && waitForDisplayed(lblInterviewDateTime, "Date and Time not displayed")
                   && waitForDisplayed(lblInterviewId, "Interview ID not displayed")){

               isAdded = true;
           }
           else{

               TestReport.logFail("Failed - Displayed job information is not the expected");
               deleteJob();
           }
       }
       catch(Exception e){

           TestReport.logFail("Failed - Issue trying to display the Job");
           deleteJob();
       }

        return isAdded;
    }

    public void deleteJob(){

       jobsBefore = driver.findElements(By.xpath("//div[@class='jobs-container']"));

       clickElement(btnDeleteJob);
       Alert alert = driver.switchTo().alert();
       alert.accept();
       waitAPause(1);
       TestReport.logInfo("Process to delete Job completed");
    }

    public boolean isJobDeleted(){

        jobsAfter = driver.findElements(By.xpath("//div[@class='jobs-container']"));

        if(jobsAfter.size() == jobsBefore.size()-1){
            isDeleted = true;
        }
        else{

            TestReport.logFail("Failed - Job not deleted");
        }

        return isDeleted;
    }

    public void addCandidate(String firstName, String lastName){

        clickElement(btnAddCandidate);
        waitForDisplayed(lblAddCandidateModalTitle, "'Add Candidate' modal not displayed");
        clickElement(driver.findElement(By.xpath("//div[@class='item-label' and text()='"+firstName+"' and '"+lastName+"']")));
        waitAPause(1);
        TestReport.logInfo("Information needed to add a Candidate entered");
    }

    public boolean isCandidateAdded(String firstName, String lastName){

        try{

            driver.findElement(By.xpath("//span[@class='candidate-name' and text()='"+firstName.concat(" ".concat(lastName))+"']")).isDisplayed();
            isAdded = true;
            highlightLabel(driver.findElement(By.xpath("//span[@class='candidate-name' and text()='"+firstName.concat(" ".concat(lastName))+"']")));
        }
        catch(Exception e){

            TestReport.logFail("Failed - Candidate not added");
            deleteJob();
        }

        return isAdded;
    }

    public void deleteCandidate(String firstName, String lastName){

        clickElement(driver.findElement(By.xpath("//span[@class='candidate-name' and text()='"+firstName.concat(" ".concat(lastName))+"']")));
        waitAPause(1);
        clickElement(optionRemoveFromSlot);
        waitAPause(1);
        TestReport.logInfo("'Delete Candidate' process completed");
    }

    public boolean isCandidateDeleted(String firstName, String lastName){

        try{

            waitAPause(1);
            driver.findElement(By.xpath("//span[@class='candidate-name' and text()='"+firstName.concat(" ".concat(lastName))+"']"));
            TestReport.logFail("Failed - Candidate not deleted");
        }
        catch(Exception e){

            isDeleted = true;
        }

        return isDeleted;
    }

    public void viewCandidateHistory(String firstName, String lastName){

        clickElement( driver.findElement(By.xpath("//span[@class='candidate-name' and text()='"+firstName.concat(" ".concat(lastName))+"']")));
        waitAPause(1);
        clickElement(optionViewHistory);
        waitAPause(1);
        TestReport.logInfo("'View Candidate History' process completed");
    }

    public boolean isCandidateHistoryDisplayed(String firstName, String lastName){

        try{

            driver.findElement(By.xpath("//h1[contains(text(), 'Candidate Name: "+firstName.concat(" ").concat(lastName)+"')]")).isDisplayed();
            isDisplayed = true;
        }
        catch(Exception e){

            TestReport.logFail("Failed - Candidate History not displayed");
        }

        driver.navigate().refresh();
        isActivityBoardDisplayed();
        deleteCandidate(firstName, lastName);
        deleteJob();

        return isDisplayed;
    }

    public void clickOnJobId(){

        clickElement(linkJobId);
        TestReport.logInfo("Clicked on Job ID link");
    }

    public Boolean isBullhornDisplayed(){

        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());

        try{


            driver.switchTo().window(tabs.get(1));

            if(driver.getCurrentUrl().contains("bullhornstaffing.com")){

                isDisplayed = true;
            }
            else{

                TestReport.logFail("Failed - Bullhorn login screen not displayed");
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to validate if Bullhorn is displayed");
        }

        driver.close();
        driver.switchTo().window(tabs.get(0));
        deleteJob();

        return isDisplayed;
    }

    public void clickOnConferenceLine(){

       clickElement(linkConferenceLine);
       TestReport.logInfo("Clicked on Conference Line link");
    }

    public void setConferenceLine(String conferenceLine){

        waitForEnabled(txtConferenceLine);
        txtConferenceLine.sendKeys(Keys.CONTROL+"a");
        txtConferenceLine.sendKeys(Keys.BACK_SPACE);
        type(txtConferenceLine, conferenceLine);
        clickElement(btnSaveConferenceLine);
        waitAPause(1);
        TestReport.logInfo("Information needed to set a Conference Line entered");
    }

    public void setZoomInformation(String text, String link){

        clickElement(tabLink);
        type(txtZoomDisplayedText, text);
        type(txtZoomLink, link);
        clickElement(btnSaveConferenceLine);
        waitAPause(1);
        TestReport.logInfo("Information needed to set a Zoom call entered");
    }

    public boolean isConferenceInfoTheExpected(String infoExpected){

        try{

            highlightLabel(lblConferenceLine);

            if(lblConferenceLine.getText().equalsIgnoreCase(infoExpected)){

                isDisplayed = true;
            }
            else{

                TestReport.logFail("Failed - Expected Conference Line/Zoom information not displayed");
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to check Conference information");
        }

        clickOnConferenceLine();
        deleteConferenceInfo();
        deleteJob();

        return isDisplayed;
    }

    public void deleteConferenceInfo(){

        clickElement(tabLink);
        type(txtZoomDisplayedText, "");
        type(txtZoomLink, "");
        clickElement(tabLine);
        txtConferenceLine.sendKeys(Keys.CONTROL+"a");
        txtConferenceLine.sendKeys(Keys.BACK_SPACE);
        clickElement(btnSaveConferenceLine);
        TestReport.logInfo("'Delete Conference Information' process completed");
    }

    public void clickOnToday(){

        clickElement(btnToday);
        TestReport.logInfo("Click on 'Today' tab");
    }

    public void clickOnNextDay(){

        clickElement(btnNextDay);
        TestReport.logInfo("Click on 'Next Day' tab");
    }

    public void clickOnAll(){

        clickElement(btnAll);
        TestReport.logInfo("Click on 'All' button");
    }
}
