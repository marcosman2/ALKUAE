package Pages;

import Base.TestReport;
import Base.Wrappers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;


public class AEHomePage extends Wrappers {

    @FindBy (xpath = "//h2[@class='c-brand-department-list__title']")
    private WebElement lblDivisions;

    List<WebElement> divisions;

    Boolean areExpected = false;

    public AEHomePage(){

        PageFactory.initElements(driver, this);
    }

    public boolean waitForHomeToDisplays(){

        return waitForDisplayed(lblDivisions, "Issue trying to navigate to Home page");
    }

    public void selectDepartment(String departmentName){

        waitForDisplayed(lblDivisions, "Home page not displayed");
        clickElement(driver.findElement(By.xpath("//div[@class='c-department-card__inner false' and contains(., '"+departmentName+"')]")));
        TestReport.logInfo("Selected Department: "+departmentName);
    }

    public boolean divisionsDisplayed(int expectedDivisions){

        waitForDisplayed(lblDivisions, "Divisions not displayed");
        divisions =  driver.findElements(By.xpath("//h2[@class='c-brand-department-list__title']"));

        for(WebElement element: divisions){

            highlightLabel(element);
        }

        if(divisions.size() == expectedDivisions){

            areExpected = true;
        }
        else{

            TestReport.logFail("Failed - Displayed number of Divisions is not the expected");
        }

       return areExpected;
    }

    public boolean departmentsDisplayed(int expectedDepartments){

        waitForDisplayed(lblDivisions, "Departments not displayed");
        List<WebElement> departments =  driver.findElements(By.xpath("//div[@class='c-department-card__inner false']"));

        if(departments.size() == expectedDepartments){

            areExpected = true;
        }
        else{

            TestReport.logFail("Failed - Displayed number of Departments is not the expected");
        }

        return areExpected;
    }
}