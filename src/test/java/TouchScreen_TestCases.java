import Base.TestReport;
import Base.Wrappers;
import Pages.*;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;

public class TouchScreen_TestCases extends Wrappers {

    AEHomePage pgHome;
    AETouchScreenPage pgTouch;
    AEDealsPage pgDeals;
    AEJobsPage pgJobs;
    AEStartsPage pgStarts;
    AEActivityPage pgActivity;
    AEWhereWeAtPage pgWWA;

    HashMap<String, String> testData;

    private Object[][] testCasesData;

    @BeforeClass
    public void deletePriorEvidences(){

        deleteScreenshots("Evidences/TouchScreen");
        TestReport.initialize("TouchScreen");
    }

    @BeforeMethod
    public void navigateToApplication(ITestResult result){

        TestReport.createTest(result.getMethod().getDescription());

        initialization();
        //browserStackInitialization();
        testCasesData = loadExcelTable("Credentials");
        logIntoApplication(testCasesData, 0);
        testCasesData = loadExcelTable("TS");

        pgHome = new AEHomePage();
        pgDeals = new AEDealsPage();
        pgJobs = new AEJobsPage();
        pgStarts = new AEStartsPage();
        pgActivity = new AEActivityPage();
        pgWWA = new AEWhereWeAtPage();
        pgTouch = new AETouchScreenPage();
    }

    public void goToTSAndBoard(int row){

        testData = (HashMap<String, String>) testCasesData[row][0];
        pgTouch.navigateToTS(testData.get("Department"));

        if(testData.get("Board").equalsIgnoreCase("Where We At")){

            pgTouch.navigateToBoard("where-we-at".toLowerCase());
        }
        else{

            pgTouch.navigateToBoard(testData.get("Board").toLowerCase());
        }

    }

    @Test(description = "Verify that 'Deals' board displays on TouchScreen mode", priority = 1)
    public void verifyDealsDisplaysOnTS(){

        goToTSAndBoard(0);

        Assert.assertTrue(pgTouch.isBoardDisplayed(testData.get("Board"), pgDeals.isDealsBoardDisplayed()), "'Deals' board not displayed on TS mode");
        TestReport.logPass("Passed - 'Deals' board successfully displayed on TS mode");
    }

    @Test(description = "Verify that 'Jobs' board displays on TouchScreen mode", priority = 2)
    public void verifyJobsDisplaysOnTS(){

        goToTSAndBoard(1);

        Assert.assertTrue(pgTouch.isBoardDisplayed(testData.get("Board"), pgJobs.isJobsBoardDisplayed()), "'Jobs' board not displayed on TS mode");
        TestReport.logPass("Passed - 'Jobs' board successfully displayed on TS mode");
    }

    @Test(description = "Verify that 'Activity' board displays on TouchScreen mode", priority = 3)
    public void verifyActivityDisplaysOnTS(){

        goToTSAndBoard(2);

        Assert.assertTrue(pgTouch.isBoardDisplayed(testData.get("Board"), pgActivity.isActivityBoardDisplayed()), "'Activity' board not displayed on TS mode");
        TestReport.logPass("Passed - 'Activity' board successfully displayed on TS mode");
    }

    @Test(description = "Verify that 'Starts' board displays on TouchScreen mode", priority = 4)
    public void verifyStartsDisplaysOnTS(){

        goToTSAndBoard(3);

        Assert.assertTrue(pgTouch.isBoardDisplayed(testData.get("Board"), pgStarts.isStartsBoardDisplayed()), "'Starts' board not displayed on TS mode");
        TestReport.logPass("Passed - 'Starts' board successfully displayed on TS mode");
    }

    @Test(description = "Verify that 'WWA' board displays on TouchScreen mode", priority = 5)
    public void verifyWWADisplaysOnTS(){

        goToTSAndBoard(4);

        Assert.assertTrue(pgTouch.isBoardDisplayed(testData.get("Board"), pgWWA.isWWABoardDisplayed()), "'WWA' board not displayed on TS mode");
        TestReport.logPass("Passed - 'WWA' board successfully displayed on TS mode");
    }

    @Test(description = "Verify that Boards buttons are expanded", priority = 6)
    public void verifyBoardsButtonsExpanded(){

        goToTSAndBoard(5);

        Assert.assertTrue(pgTouch.areBoardsButtonsExpanded(), "Boards buttons not expanded");
        TestReport.logPass("Passed - Boards buttons successfully expanded");
    }

    @Test(description = "Verify that navigation arrows are displayed", priority = 7)
    public void verifyNavigationArrowDisplayed(){

        goToTSAndBoard(6);

        Assert.assertTrue(pgTouch.areArrowsDisplayed(), "Navigation arrows not displayed");
        TestReport.logPass("Passed - Navigation arrows successfully displayed");
    }

   @AfterMethod
    public void tearDown(Method method) {

       takeScreenshot("Evidences/TouchScreen/TouchScreen_".concat(method.getName()).concat(".png"));
       finalization();
    }
}
