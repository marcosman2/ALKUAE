package Pages;

import Base.TestReport;
import Base.Wrappers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AEStartsPage extends Wrappers {

    @FindBy(xpath = "//div[@class='filter-header-input']/div[contains(text(), 'Status')]")
    private WebElement lblStatus;

    @FindBy(xpath = "//div[@class='filter-header-input']/div[contains(text(), 'Status')]/parent::*/descendant::input")
    private WebElement ddStatus;

    @FindBy(xpath = "//div[@class='filter-header-input']/div[contains(text(), 'Company')]/parent::*/descendant::input")
    private WebElement ddCompany;

    @FindBy(xpath = "//button[contains(text(), 'Consultant')]")
    private WebElement btnConsultant;

    @FindBy(xpath = "//button[contains(text(), 'Client')]")
    private WebElement btnClient;

    @FindBy(xpath = "//span[contains(text(), 'Onboarding Paperwork')]/preceding-sibling::button")
    private WebElement btnOnboardingPW;

    @FindBy(xpath = "//div[@class='c-starts-modal__header']/div[contains(text(), 'Onboarding Paperwork')]")
    private WebElement modalOnboardingPW;

    boolean isStartsDisplayed = false;
    boolean isHidden = false;
    boolean isDisplayed = false;
    boolean areRecordsFiltered = false;

    public AEStartsPage(){

        PageFactory.initElements(driver, this);
    }

    public boolean isStartsBoardDisplayed(){

        waitForDisplayed(lblStatus, "Issue trying to navigate to Starts board");
        isStartsDisplayed = true;
        TestReport.logInfo("Navigated to Starts board");

        return isStartsDisplayed;
    }

    public void selectStatus(String status){

        try{

            clickElement(ddStatus);
            waitAPause(2);
            clickElement(driver.findElement(By.xpath("//div[@class='ant-select-item-option-content' and contains(text(), '"+status+"')]")));
            TestReport.logInfo("Selected Status: "+status);
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to select the Status");
        }

    }

    public boolean areRecordsFilteredByStatus(String status){

        try{

            List<WebElement> recordsStatus = driver.findElements(By.xpath("//div[@class='c-consultant-card__candidate-block-wrapper']/descendant::span[contains(text(), '"+status+"')]"));
            List<WebElement> records = driver.findElements(By.xpath("//li[@class='c-consultant-card c-consultant-card--starts c-consultant-card--w-progress element-container']"));

            if(recordsStatus.size() == records.size()){

                areRecordsFiltered = true;
            }
            else{

                TestReport.logFail("Failed - Not all displayed records belong to the selected Status");
            }

            for(WebElement element: recordsStatus){

                highlightLabel(element);
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to check if all displayed records belong to the selected Status");
        }

        return areRecordsFiltered;
    }

    public void selectCompany(String company){

        try{

            clickElement(ddCompany);
            waitAPause(2);
            clickElement(driver.findElement(By.xpath("//div[@class='ant-select-item-option-content' and contains(text(), '"+company+"')]")));
            TestReport.logInfo("Selected Company: "+company);
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to select the Company");
        }
    }

    public boolean areRecordsFilteredByCompany(String company){

        try{

            List<WebElement> recordsCompany = driver.findElements(By.xpath("//div[@class='c-consultant-card__field-header ' and contains(text(), 'Company')]/following-sibling::div[contains(text(), '"+company+"')]"));
            List<WebElement> records = driver.findElements(By.xpath("//li[@class='c-consultant-card c-consultant-card--starts c-consultant-card--w-progress element-container']"));

            if(recordsCompany.size() == records.size()){

                areRecordsFiltered = true;
            }
            else{

                TestReport.logFail("Failed - Not all displayed records belong to the selected Company");
            }

            for(WebElement element: recordsCompany){

                highlightLabel(element);
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to check if all displayed records belong to the selected Company");
        }

        return areRecordsFiltered;
    }

    public void selectConsultantFilter(){

        clickElement(btnConsultant);
        waitAPause(1);
        TestReport.logInfo("Clicked on 'Consultant' button");
    }

    public void selectClientFilter(){

        clickElement(btnClient);
        waitAPause(1);
        TestReport.logInfo("Clicked on 'Client' button");
    }

    public boolean isConsultantInfoHidden(){

        waitForDisplayed(lblStatus, "Issue trying to navigate to Starts board");

        List<WebElement> contactNumber = driver.findElements(By.xpath("//div[@class='c-consultant-card__field-header ' and contains(text(), 'Contact')]/parent::*"));
        List<WebElement> bottomCardNumber = driver.findElements(By.xpath("//div[contains(@class, 'bottom')]/descendant::ul[@class='c-consultant-card__section']"));

        if(contactNumber.size() > 0 && bottomCardNumber.size() > 0){

            selectConsultantFilter();

            try{

                driver.findElement(By.xpath("//div[@class='c-consultant-card__field-header ' and contains(text(), 'Contact')]/parent::*"));
                TestReport.logFail("Failed - Consultant information not hidden");
            }
            catch(Exception e){

                try{

                    driver.findElement(By.xpath("//div[contains(@class, 'bottom')]/descendant::ul[@class='c-consultant-card__section']"));
                    TestReport.logFail("Failed - Consultant information not hidden");
                }
                catch(Exception j){

                    isHidden = true;
                }
            }
        }

        return isHidden;
    }

    public boolean isClientInfoHidden(){

        waitForDisplayed(lblStatus, "Issue trying to navigate to Starts board");

        List<WebElement> empTypeNumber = driver.findElements(By.xpath("//div[@class='c-consultant-card__sub-date']/div"));
        List<WebElement> topCardNumber = driver.findElements(By.xpath("//div[contains(@class, 'top')]/descendant::ul[@class='c-consultant-card__section']"));
        List<WebElement> middleCardNumber = driver.findElements(By.xpath("//div[@class='c-consultant-card__section c-consultant-card__info-block c-consultant-card__section--w-divisor-before']"));
        List<WebElement> startDateNumber = driver.findElements(By.xpath("//div[contains(@class, 'field--date')]"));

        if(empTypeNumber.size() > 0 && topCardNumber.size() > 0 && middleCardNumber.size() > 0 && startDateNumber.size() > 0){

            selectClientFilter();

            try{

                driver.findElement(By.xpath("//div[contains(@class, 'top')]/descendant::ul[@class='c-consultant-card__section']"));
                TestReport.logFail("Failed - Client information not hidden");
            }
            catch(Exception e){

                try{

                    driver.findElement(By.xpath("//div[@class='c-consultant-card__section c-consultant-card__info-block c-consultant-card__section--w-divisor-before']"));
                    TestReport.logFail("Failed - Client information not hidden");
                }
                catch(Exception j){

                    try{

                        driver.findElement(By.xpath("//div[contains(@class, 'field--date')]"));
                        TestReport.logFail("Failed - Client information not hidden");
                    }
                    catch(Exception k){

                        try{

                            driver.findElement(By.xpath("//div[@class='c-consultant-card__sub-date']/div"));
                            TestReport.logFail("Failed - Client information not hidden");
                        }
                        catch(Exception t){

                            isHidden = true;
                        }
                    }
                }
            }
        }

        return isHidden;
    }

    public void openOPWModal(){

        clickElement(btnOnboardingPW);
        TestReport.logInfo("Clicked on 'Open Onboarding Paperwork' button");
    }

    public boolean isOPWModalDisplayed() {

        return waitForDisplayed(modalOnboardingPW, "'Onboarding PW' modal not displayed");
    }


}