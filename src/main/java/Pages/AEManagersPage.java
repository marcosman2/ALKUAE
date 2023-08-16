package Pages;

import Base.TestReport;
import Base.Wrappers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AEManagersPage extends Wrappers {

    @FindBy (xpath = "//div[contains(text(), 'Change Department')]")
    private WebElement btnChangeDept;

    List<WebElement> managers;

    boolean areManagers = false;

    public AEManagersPage(){

        PageFactory.initElements(driver, this);
    }

    public void selectManager(String name){

        waitForDisplayed(btnChangeDept, "Managers not displayed");
        clickElement(driver.findElement(By.xpath("//div[@class='header-username bottom' and contains(text(), '"+name+"')]")));
        TestReport.logInfo("Selected Manager: "+name);
    }

    public boolean managersDisplayed(int managersExpected){

        waitForDisplayed(btnChangeDept, "Issue trying to check the number of Managers displayed");
        managers =  driver.findElements(By.xpath("//div[@class='header-username bottom']"));

        for(WebElement element: managers){

            highlightLabel(element);
        }

        if(managers.size() == managersExpected){

            areManagers = true;
        }
        else{

            TestReport.logFail("Failed - Number of Managers displayed is not the expected");
        }

        return areManagers;
    }

    public void clickOnChangeDepartment(){

        clickElement(btnChangeDept);
        TestReport.logInfo("Clicked on 'Change Department' button");
    }
}
