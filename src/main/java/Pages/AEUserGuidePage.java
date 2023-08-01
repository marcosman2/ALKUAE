package Pages;

import Base.TestReport;
import Base.Wrappers;

import java.util.ArrayList;

public class AEUserGuidePage extends Wrappers {

    boolean isUGDisplayed = false;

    public Boolean isUserGuideDisplayed(String expectedUrl){

        try{

            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(1));

            if(driver.getCurrentUrl().equalsIgnoreCase(expectedUrl)){

                isUGDisplayed = true;
            }
            else{

                TestReport.logFail("Failed - User Guide not displayed");
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to navigate to User Guide");
        }

        return isUGDisplayed;
    }
}
