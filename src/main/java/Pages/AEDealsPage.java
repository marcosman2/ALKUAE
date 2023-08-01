package Pages;

import Base.TestReport;
import Base.Wrappers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AEDealsPage extends Wrappers {

    @FindBy (xpath = "//div[contains(text(), 'Total Deals:')]")
    private WebElement lblTotalDeals;

    @FindBy (xpath = "//div[contains(text(), 'Total Deals:')]/following-sibling::*")
    private WebElement lblTotalDealsValue;

    @FindBy (xpath = "//div[contains(text(), 'Total Spread:')]/following-sibling::*")
    private WebElement lblTotalSpreadValue;

    @FindBy (xpath = "//div[contains(text(), 'Average Spread:')]/following-sibling::*")
    private WebElement lblAverageSpreadValue;

    @FindBy (xpath = "//div[contains(text(), 'Average Margin:')]/following-sibling::*")
    private WebElement lblAverageMarginValue;

    @FindBy(xpath = "//div[@class='c-deals-fiscal-date']/div")
    private WebElement lblFiscalDate;

    boolean valueMatches = false;

    int row;

    boolean isADealsDisplayed = false;

    List<WebElement> dealsRecords;

    float totalMargin = 0;

    public AEDealsPage(){

        PageFactory.initElements(driver, this);
    }

    public boolean isDealsBoardDisplayed(){

        try{

            waitForDisplayed(lblTotalDeals);
            isADealsDisplayed = true;
            TestReport.logInfo("Navigated to Deals board");
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to navigate to Deals board");
        }

        return isADealsDisplayed;
    }

    public int getDealsRecords(){

        try{

            dealsRecords = driver.findElements(By.xpath("//div[@class='element-container']"));
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to get all Deals records");
        }

        return dealsRecords.size();
    }

    public float getTotalSpread(){

        float totalSpread = 0;

        try{

            waitAPause(2);
            Float[] spreads = new Float[getDealsRecords()];

            for(row = 1; row <= getDealsRecords(); row++){

                spreads[row-1] = Float.valueOf(driver.findElement(By.xpath("(//span[@class='mobile-name' and contains(text(), 'Spread')])["+row+"]/parent::div"))
                        .getAttribute("innerText").replace("$", ""));
            }

            for(row = 0; row< spreads.length; row++){

                totalSpread = totalSpread + spreads[row];
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to get Total Spread");
        }

       return totalSpread;
    }

    public float getAverageSpread(){

        return getTotalSpread()/getDealsRecords();
    }

    public float getAverageMargin(){

        try {

            waitAPause(2);

            Float[] margins = new Float[getDealsRecords()];

            for(row = 1; row <= getDealsRecords(); row++){

                margins[row-1] = Float.valueOf(driver.findElement(By.xpath("(//span[@class='mobile-name' and contains(text(), 'Per.')])["+row+"]/parent::div"))
                        .getAttribute("innerText").replace("%", ""));
            }

            for(row = 0; row< margins.length; row++){

                totalMargin = totalMargin + margins[row];
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to get Average Margin");
        }

        return totalMargin/getDealsRecords();

    }

    public boolean totalDealsMatchesWithRecords(){

        waitForDisplayed(lblTotalDealsValue);

        if(getDealsRecords() == Integer.valueOf(lblTotalDealsValue.getText())){

            valueMatches = true;
            highlightLabel(lblTotalDealsValue);
        }
        else{

            TestReport.logFail("Failed - 'Total Deals' does not match with number of records");
        }

        return valueMatches;
    }

    public boolean totalSpreadMatchesWithRecords(){

        try{

            if(getTotalSpread() == Float.valueOf(lblTotalSpreadValue.getText().replace("$", ""))){

                valueMatches = true;
                highlightLabel(lblTotalSpreadValue);
            }
            else{

                TestReport.logFail("Failed - 'Total Spread' does not match with column values sum");
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to check Total Spread value");
        }

        return valueMatches;
    }

    public boolean averageSpreadMatchesWithRecords(){

       try{

           if(getAverageSpread() == Float.valueOf(lblAverageSpreadValue.getText().replace("$", ""))){

               valueMatches = true;
               highlightLabel(lblAverageSpreadValue);
           }
           else{

               TestReport.logFail("Failed - 'Average Spread' does not match with column values average");
           }
       }
       catch(Exception e){

           TestReport.logFail("Failed - Issue trying to check Average Spread value");
       }



        return valueMatches;
    }

    public boolean averageMarginMatchesWithRecords(){

        try{

            waitAPause(2);
            float averageMargin = Float.valueOf(lblAverageMarginValue.getText().replace("%", ""));

            if((int)getAverageMargin() == (int)averageMargin){

                valueMatches = true;
                highlightLabel(lblAverageMarginValue);
            }
            else{

                TestReport.logFail("Failed - 'Average Margin' does not match with column values average");
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to check Average Margin value");
        }

        return valueMatches;
    }

    public boolean isFiscalDateTheExpected(String month){

        try{

            AEBoardsPage pgBoards = new AEBoardsPage();

            if(lblFiscalDate.getText().replace(" ", "").equalsIgnoreCase(pgBoards.getPeriodDateRange(month))){

                valueMatches = true;
                highlightLabel(lblFiscalDate);
            }
            else{

                TestReport.logFail("Failed - Fiscal date range is not the expected one");
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to check Fiscal Date value");
        }

        return valueMatches;
    }
}
