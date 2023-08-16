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

public class DealsBoard_TestCases extends Wrappers {

    AEHomePage pgHome;
    AEManagersPage pgManagers;
    AEDealsPage pgDeals;
    AEBoardsPage pgBoards;
    HashMap<String, String> testData;

    private Object[][] testCasesData;

    @BeforeClass
    public void deletePriorEvidences(){

        deleteScreenshots("Evidences/DealsBoard");
        TestReport.initialize("DealsBoard");
    }

    @BeforeMethod
    public void navigateToHome(ITestResult result) {

        TestReport.createTest(result.getMethod().getDescription());

        initialization();
        //browserStackInitialization();
        testCasesData = loadExcelTable("Credentials");
        logIntoApplication(testCasesData, 0);
        testCasesData = loadExcelTable("Deals");

        pgHome = new AEHomePage();
        pgManagers = new AEManagersPage();
        pgDeals = new AEDealsPage();
        pgBoards = new AEBoardsPage();
    }

    public void goToBoardAndSelectManagerAndPeriod(int row){

        try{

            if(pgHome.waitForHomeToDisplays()){

                TestReport.logInfo("Navigated to ALKU AE");

                testData = (HashMap<String, String>) testCasesData[row][0];

                pgHome.selectDepartment(testData.get("Department"));
                pgManagers.selectManager(testData.get("User"));

                if(pgDeals.isDealsBoardDisplayed()){

                    pgBoards.expandPeriodDropdown();
                    waitAPause(1);
                    pgBoards.selectPeriod(testData.get("Period"));
                    waitAPause(2);
                }
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to navigate to application, go to Deals board and select the Period");
        }
    }

    @Test(description = "Verify 'Total Deals' matches with number of records", priority = 1)
    public void verifyTotalDeals(){

        goToBoardAndSelectManagerAndPeriod(0);

        Assert.assertTrue(pgDeals.totalDealsMatchesWithRecords(), "Total Deals does not match");
        TestReport.logPass("Passed - 'Total Deals' matches with number of records");
    }

    @Test(description = "Verify 'Total Spread' matches with column sum", priority = 2)
    public void verifyTotalSpread(){

        goToBoardAndSelectManagerAndPeriod(0);

        Assert.assertTrue(pgDeals.totalSpreadMatchesWithRecords(), "Total Spread value is not the expected");
        TestReport.logPass("Passed - 'Total Spread' matches with column values sum");
    }

    @Test(description = "Verify 'Average Spread' matches with column sum", priority = 3)
    public void verifyAverageSpread(){

        goToBoardAndSelectManagerAndPeriod(0);

        Assert.assertTrue(pgDeals.averageSpreadMatchesWithRecords(), "Average Spread value is not the expected");
        TestReport.logPass("Passed - 'Average Spread' matches with column values average");
    }

    @Test(description = "Verify that 'Average Margin' matches with column sum", priority = 4)
    public void verifyAverageMargin(){

        goToBoardAndSelectManagerAndPeriod(0);

        Assert.assertTrue(pgDeals.averageMarginMatchesWithRecords(), "Average Margin value is not the expected");
        TestReport.logPass("Passed - 'Average Margin' matches with column values average");
    }

    @Test(description = "Verify 'Fiscal Date' matches with selected period", priority = 5)
    public void verifyFiscalDate(){

        goToBoardAndSelectManagerAndPeriod(0);

        pgBoards.expandPeriodDropdown();
        Assert.assertTrue(pgDeals.isFiscalDateTheExpected(testData.get("Period")), "Fiscal Date does not match");
        TestReport.logPass("Passed - Fiscal date range is the expected one");
    }

   @AfterMethod
    public void tearDown(Method method) {

       takeScreenshot("Evidences/DealsBoard/DealsBoardTest_".concat(method.getName()).concat(".png"));
       finalization();
    }
}
