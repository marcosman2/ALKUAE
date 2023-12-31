package Pages;

import Base.TestReport;
import Base.Wrappers;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AEWhereWeAtPage extends Wrappers {

    @FindBy(xpath = "//div[@class='c-dashboard__updatedate ' and contains(text(), 'Last updated on')]")
    private WebElement lblLastUpdated;

    @FindBy(xpath = "//div[@id='DealsMonth']/span")
    private WebElement lblDealsMonth;

    @FindBy(xpath = "//div[@id='DealProgressMonth']/span")
    private WebElement lblDealsProgress;

    @FindBy(xpath = "//div[@id='StackRank']/span")
    private WebElement lblDealsStackRank;

    boolean isWWADisplayed = false;
    boolean dateMatches = false;

    AEBoardsPage pgBoards;

    public AEWhereWeAtPage(){

        PageFactory.initElements(driver, this);
        pgBoards = new AEBoardsPage();
    }

    public boolean isWWABoardDisplayed(){

        waitForDisplayed(lblLastUpdated, "Issue trying to navigate to Where We At board");
        isWWADisplayed = true;
        TestReport.logInfo("Navigated to Where We At board");

        return isWWADisplayed;
    }

    public boolean isDealsMonthDateTheExpected(String month){

        scrollToElement(lblDealsMonth);
        highlightLabel(lblDealsMonth);

        if(lblDealsMonth.getText().replace("(", "").replace(")", "").equalsIgnoreCase(pgBoards.getPeriodDateRange(month))){

            dateMatches = true;
        }
        else{

            TestReport.logFail("Failed - 'Deals Month' date does not match with selected period");
        }


        return dateMatches;
    }

    public boolean isDealsProgressDateTheExpected(String month){

        scrollToElement(lblDealsProgress);
        highlightLabel(lblDealsProgress);

        if(lblDealsProgress.getText().replace("(", "").replace(")", "").equalsIgnoreCase(pgBoards.getPeriodDateRange(month))){

            dateMatches = true;
        }
        else{

            TestReport.logFail("Failed - 'Deals Progress' date does not match with selected period");
        }

        return dateMatches;
    }

    public boolean isDealsStackRankDateTheExpected(String month){

        scrollToElement(lblDealsStackRank);
        highlightLabel(lblDealsStackRank);

        if(lblDealsStackRank.getText().replace("(", "").replace(")", "").equalsIgnoreCase(pgBoards.getPeriodDateRange(month))){

            dateMatches = true;
        }
        else{

            TestReport.logFail("Failed - 'Deals Stack Rank' date does not match with selected period");
        }

        return dateMatches;
    }
}