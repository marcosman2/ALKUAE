import base.TestReport;
import base.Wrappers;
import pages.*;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.HashMap;

public class SmokeTest_TestCases extends Wrappers {

    AEHomePage pgHome;
    AEManagersPage pgManagers;
    AEDealsPage pgDeals;
    AEBoardsPage pgBoards;
    AEJobsPage pgJobs;
    AEStartsPage pgStarts;
    AEActivityPage pgActivity;
    AEWhereWeAtPage pgWWA;
    AEAssignedAccountsPage pgAA;
    AEUserGuidePage pgUserGuide;

    HashMap<String, String> testData;

    private Object[][] testCasesData;

    @BeforeClass
    public void deletePriorEvidences(){

        deleteScreenshots("Evidences/Smoke");
        TestReport.initialize("SmokeTest");
    }

    @BeforeMethod
    public void navigateToHome(ITestResult result){

        TestReport.createTest(result.getMethod().getDescription());

        initialization();
        //browserStackInitialization();
        testCasesData = loadExcelTable("Credentials");
    }

    public boolean isHome(){

        if(logIntoApplication(testCasesData, 0)){

            testCasesData = loadExcelTable("Smoke_Test");

            pgHome = new AEHomePage();
            pgManagers = new AEManagersPage();
            pgDeals = new AEDealsPage();
            pgBoards = new AEBoardsPage();
            pgJobs = new AEJobsPage();
            pgStarts = new AEStartsPage();
            pgActivity = new AEActivityPage();
            pgWWA = new AEWhereWeAtPage();
            pgAA = new AEAssignedAccountsPage();
            pgUserGuide = new AEUserGuidePage();

            return true;
        }
        else{

            return false;
        }
    }

    @Test(description = "Verify that home page displays divisions and departments", priority = 1)
    public void verifyHomePage(){

        if(isHome()){

            testData = (HashMap<String, String>) testCasesData[0][0];

            Assert.assertTrue(pgHome.divisionsDisplayed(Integer.valueOf(testData.get("Expected_Divisions"))), "Expected number of divisions not displayed");
            Assert.assertTrue(pgHome.departmentsDisplayed(Integer.valueOf(testData.get("Expected_Departments"))), "Expected number of departments not displayed");
            TestReport.logPass("Passed - Home page successfully displayed");
        }
    }

    @Test(description = "Verify that managers are displayed under the department", priority = 2)
    public void verifyUsersUnderADepartment(){

        if(isHome()){

            testData = (HashMap<String, String>) testCasesData[1][0];

            pgHome.selectDepartment(testData.get("Department"));

            Assert.assertTrue(pgManagers.managersDisplayed(Integer.valueOf(testData.get("Expected_Managers"))), "Expected number of managers not displayed");
            TestReport.logPass("Passed - Expected Users successfully displayed");
        }
    }

    @Test(description = "Verify that 'Change Department' button takes back to the home page", priority = 3)
    public void verifyChangeDepartmentFunctionality(){

        if(isHome()){

            testData = (HashMap<String, String>) testCasesData[1][0];

            pgHome.selectDepartment(testData.get("Department"));
            pgManagers.clickOnChangeDepartment();

            Assert.assertTrue(pgHome.divisionsDisplayed(Integer.valueOf(testData.get("Expected_Divisions"))), "Home page not displayed");
            TestReport.logPass("Passed - Home page successfully displayed after clicking on 'Change Department' button");
        }
    }

    @Test(description = "Verify that 'Deals' board is displayed", priority = 4)
    public void verifyDealsBoard(){

        if(isHome()){

            testData = (HashMap<String, String>) testCasesData[2][0];

            pgHome.selectDepartment(testData.get("Department"));
            pgManagers.selectManager(testData.get("User"));

            Assert.assertTrue(pgDeals.isDealsBoardDisplayed(), "Deals board not displayed");
            TestReport.logPass("Passed - Deals board successfully displayed");
        }
    }

    public void goToBoard(int row){

        testData = (HashMap<String, String>) testCasesData[row][0];

        pgHome.selectDepartment(testData.get("Department"));
        pgManagers.selectManager(testData.get("User"));
        pgBoards.selectBoard(testData.get("Board"));
    }

    @Test(description = "Verify that 'Jobs' board is displayed", priority = 5)
    public void verifyJobsBoard(){

        if(isHome()){

            goToBoard(3);

            Assert.assertTrue(pgJobs.isJobsBoardDisplayed(), "Jobs board not displayed");
            TestReport.logPass("Passed - Jobs board successfully displayed");
        }
    }

    @Test(description = "Verify that 'Starts' board is displayed", priority = 6)
    public void verifyStartsBoard(){

        if(isHome()){

            goToBoard(4);

            Assert.assertTrue(pgStarts.isStartsBoardDisplayed(), "Starts board not displayed");
            TestReport.logPass("Passed - Starts board successfully displayed");
        }
    }

    @Test(description = "Verify that 'Activity' board is displayed", priority = 7)
    public void verifyActivityBoard(){

        if(isHome()){

            goToBoard(5);

            Assert.assertTrue(pgActivity.isActivityBoardDisplayed(), "Activity board not displayed");
            TestReport.logPass("Passed - Activity board successfully displayed");
        }
    }

    @Test(description = "Verify that 'Where We At' board is displayed", priority = 8)
    public void verifyWWABoard(){

        if(isHome()){

            goToBoard(6);

            Assert.assertTrue(pgWWA.isWWABoardDisplayed(), "WWA board not displayed");
            TestReport.logPass("Passed - Where We At board successfully displayed");
        }
    }

    @Test(description = "Verify that 'Assigned Accounts'  board is displayed", priority = 9)
    public void verifyAABoard(){

        if(isHome()){

            goToBoard(7);

            Assert.assertTrue(pgAA.isAABoardDisplayed(), "AA board not displayed");
            TestReport.logPass("Passed - Assigned Accounts board successfully displayed");
        }
    }

    @Test(description = "Verify that user guide is displayed", priority = 10)
    public void verifyUserGuide(){

        if(isHome()){

            goToBoard(8);
            waitAPause(3);

            Assert.assertTrue(pgUserGuide.isUserGuideDisplayed("https://adk-alku-uat.s3.us-east-2.amazonaws.com/documents/Whiteboard_legend_rev2021-10.pdf"), "User guide not displayed");
            TestReport.logPass("Passed - User Guide successfully displayed");
        }
    }

    public void navigateToHeaderlessBoard(int row){

        try{

            goToBoard(row);
            pgBoards.selectHeaderlessBoard();
            TestReport.logInfo("Navigated to Headerless mode");
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to navigate to Headerless mode");
        }
    }

    @Test(description = "Verify that headerless 'Deals' board is displayed", priority = 11)
    public void verifyHeaderlessDealsBoard(){

        if(isHome()){

            navigateToHeaderlessBoard(9);

            Assert.assertTrue(pgBoards.getHeaderlessBoardTitle("Deals"), "Headerless Deals board not displayed");
            TestReport.logPass("Passed - Deals board successfully displayed on headerless");
        }
    }

    @Test(description = "Verify that headerless 'Jobs' board is displayed", priority = 12)
    public void verifyHeaderlessJobsBoard(){

        if(isHome()){

            navigateToHeaderlessBoard(10);

            Assert.assertTrue(pgBoards.getHeaderlessBoardTitle("Jobs"), "Headerless Jobs board not displayed");
            TestReport.logPass("Passed - Jobs board successfully displayed on headerless");
        }
    }

    @Test(description = "Verify that headerless 'Starts' board is displayed", priority = 13)
    public void verifyHeaderlessStartsBoard(){

        if(isHome()){

            navigateToHeaderlessBoard(11);

            Assert.assertTrue(pgBoards.getHeaderlessBoardTitle("Starts"), "Headerless Starts board not displayed");
            TestReport.logPass("Passed - Starts board successfully displayed on headerless");
        }
    }

    @Test(description = "Verify that headerless 'Activity' board is displayed", priority = 14)
    public void verifyHeaderlessActivityBoard(){

        if(isHome()){

            navigateToHeaderlessBoard(12);

            Assert.assertTrue(pgBoards.getHeaderlessBoardTitle("Activity"), "Headerless Activity board not displayed");
            TestReport.logPass("Passed - Activity board successfully displayed on headerless");
        }
    }

    @Test(description = "Verify that headerless 'Where We At' board is displayed", priority = 15)
    public void verifyHeaderlessWWABoard(){

        if(isHome()){

            navigateToHeaderlessBoard(13);

            Assert.assertTrue(pgBoards.getHeaderlessBoardTitle("Where We At"), "Headerless WWA board not displayed");
            TestReport.logPass("Passed - Where We At board successfully displayed on headerless");
        }
    }

    @Test(description = "Verify that headerless 'Assigned Accounts' board is displayed", priority = 16)
    public void verifyHeaderlessAABoard(){

        if(isHome()){

            navigateToHeaderlessBoard(14);

            Assert.assertTrue(pgBoards.isHeaderlessLogoDisplayed(), "Headerless AA board not displayed");
            TestReport.logPass("Passed - Assigned Accounts board successfully displayed on headerless");
        }
    }

   @AfterMethod
    public void tearDown(Method method) {

       takeScreenshot("Evidences/Smoke/SmokeTest_".concat(method.getName()).concat(".png"));
       finalization();
    }
}
