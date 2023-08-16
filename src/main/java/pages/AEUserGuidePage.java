package pages;

import base.TestReport;
import base.Wrappers;

import java.util.ArrayList;

public class AEUserGuidePage extends Wrappers {

    boolean isUGDisplayed = false;

    public Boolean isUserGuideDisplayed(String expectedUrl){

        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        if(driver.getCurrentUrl().equalsIgnoreCase(expectedUrl)){

            isUGDisplayed = true;
        }
        else{

            TestReport.logFail("Failed - User Guide not displayed");
        }

        return isUGDisplayed;
    }
}
