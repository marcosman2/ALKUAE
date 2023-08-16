import base.TestReport;
import base.Wrappers;
import pages.*;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;

public class WWABoard_TestCases extends Wrappers {

    AEHomePage pgHome;
    AEManagersPage pgManagers;
    AEBoardsPage pgBoards;
    AEWhereWeAtPage pgWWA;
    HashMap<String, String> testData;

    private Object[][] testCasesData;

    @BeforeClass
    public void deletePriorEvidences(){

        deleteScreenshots("Evidences/WWABoard");
        TestReport.initialize("WWABoard");
    }

    @BeforeMethod
    public void navigateToJobsBoard(ITestResult result){

        TestReport.createTest(result.getMethod().getDescription());

        initialization();
        //browserStackInitialization();
        testCasesData = loadExcelTable("Credentials");
        logIntoApplication(testCasesData, 0);
        testCasesData = loadExcelTable("WWA");

        pgHome = new AEHomePage();
        pgManagers = new AEManagersPage();
        pgBoards = new AEBoardsPage();
        pgWWA = new AEWhereWeAtPage();
    }

    public void goToBoardAndSelectManagerAndPeriod(int row){

        testData = (HashMap<String, String>) testCasesData[row][0];

        pgHome.selectDepartment(testData.get("Department"));
        pgManagers.selectManager(testData.get("User"));
        pgBoards.selectBoard(testData.get("Board"));

        if(pgWWA.isWWABoardDisplayed()){

            pgBoards.expandPeriodDropdown();
            waitAPause(1);
            pgBoards.selectPeriod(testData.get("Period"));
            waitAPause(1);
            pgBoards.expandPeriodDropdown();
            waitAPause(1);
        }
    }

    @Test(description = "Verify that 'Deals Month' date matches with selected period", priority = 1)
    public void verifyDealsMonthDate(){

        goToBoardAndSelectManagerAndPeriod(0);

        Assert.assertTrue(pgWWA.isDealsMonthDateTheExpected(testData.get("Period")), "Deals Month date does not match");
        TestReport.logPass("Passed - 'Deals Month' date matches with selected period");
    }

    @Test(description = "Verify that 'Deals Progress' date matches with selected period", priority = 2)
    public void verifyDealsProgressDate(){

        goToBoardAndSelectManagerAndPeriod(0);

        Assert.assertTrue(pgWWA.isDealsProgressDateTheExpected(testData.get("Period")), "Deals Progress date does not match");
        TestReport.logPass("Passed - 'Deals Progress' date matches with selected period");
    }

    @Test(description = "Verify that 'Deals Stack Rank' date matches with selected period", priority = 3)
    public void verifyDealsStackRankDate(){

        goToBoardAndSelectManagerAndPeriod(0);

        Assert.assertTrue(pgWWA.isDealsStackRankDateTheExpected(testData.get("Period")), "Deals Stack Rank date does not match");
        TestReport.logPass("Passed - 'Deals Stack Rank' date matches with selected period");
    }


   @AfterMethod
    public void tearDown(Method method) {

       takeScreenshot("Evidences/WWABoard/WWABoardTest_".concat(method.getName()).concat(".png"));
       finalization();
    }
}
