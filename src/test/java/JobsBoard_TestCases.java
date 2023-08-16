import Base.TestReport;
import Base.Wrappers;
import Pages.*;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;

public class JobsBoard_TestCases extends Wrappers {

    AEHomePage pgHome;
    AEManagersPage pgManagers;
    AEBoardsPage pgBoards;
    AEJobsPage pgJobs;
    HashMap<String, String> testData;

    private Object[][] testCasesData;

    @BeforeClass
    public void deletePriorEvidences(){

        deleteScreenshots("Evidences/JobsBoard");
        TestReport.initialize("JobsBoard");
    }

    @BeforeMethod
    public void navigateToJobsBoard(ITestResult result){

        TestReport.createTest(result.getMethod().getDescription());

        initialization();
        //browserStackInitialization();
        testCasesData = loadExcelTable("Credentials");
        logIntoApplication(testCasesData, 0);
        testCasesData = loadExcelTable("Jobs");

        pgHome = new AEHomePage();
        pgManagers = new AEManagersPage();
        pgBoards = new AEBoardsPage();
        pgJobs = new AEJobsPage();
    }

    public boolean goToBoard(int row){

        testData = (HashMap<String, String>) testCasesData[row][0];

        pgHome.selectDepartment(testData.get("Department"));
        pgManagers.selectManager(testData.get("User"));
        pgBoards.selectBoard(testData.get("Board"));

        return pgJobs.isJobsBoardDisplayed();
    }

    @Test(description = "Verify filter by Company", priority = 1)
    public void verifyFilterByCompany(){

        if(goToBoard(0)){

            pgJobs.selectCompany(testData.get("Company"));
            pgJobs.getRecordsCompanyName();

            Assert.assertTrue(pgJobs.allCompanyNamesEqual(pgJobs.getRecordsCompanyName(), testData.get("Company")), "Not all displayed records belong to selected company");
            TestReport.logPass("Passed - All displayed records belong to selected Company");
            takeScreenshot("Evidences/JobsBoard/JobsBoardTest_verifyFilterByCompany.png");
        }
    }

    @Test(description = "Verify filter by Multidivision", priority = 2)
    public void verifyFilterByMultiDivision(){

        if(goToBoard(0)){

            pgJobs.selectMultiDivisionOnly();

            Assert.assertTrue(pgJobs.areAllMultiDivisionRecords(), "Not all records are MultiDivision");
            TestReport.logPass("Passed - All displayed records are Multidivision");
            takeScreenshot("Evidences/JobsBoard/JobsBoardTest_verifyFilterByMultiDivision.png");
        }
    }

    @Test(description = "Verify Job History popup is displayed", priority = 3)
    public void verifyJobHistoryIsDisplayed(){

        if(goToBoard(0)){

            pgJobs.viewJobHistory();

            Assert.assertTrue(pgJobs.isHistoryPopupDisplayed(), "Job History popup not displayed");
            TestReport.logPass("Passed - Job History popup successfully displayed");
            takeScreenshot("Evidences/JobsBoard/JobsBoardTest_verifyJobHistoryIsDisplayed.png");
        }
    }

    @Test(description = "Verify TC tab is displayed", priority = 4)
    public void verifyTCTabDisplayed(){

        if(goToBoard(0)){

            Assert.assertTrue(pgJobs.isTCTabDisplayed(), "TC tab not displayed");
            TestReport.logPass("Passed - 'TC' tab successfully displayed");
            takeScreenshot("Evidences/JobsBoard/JobsBoardTest_verifyTCTabDisplayed.png");

            /*
             * Step needed to run repeatedly the test cases
             *
             * */
            pgJobs.clickToggleTC();
        }
    }

    @Test(description = "Verify filter by TC", priority = 5)
    public void verifyOnlyTCRecordsDisplayed(){

        if(goToBoard(0)){

            pgJobs.isTCTabDisplayed();
            takeScreenshot("Evidences/JobsBoard/JobsBoardTest_verifyOnlyTCRecordsDisplayed_TabDisplayed.png");

            pgJobs.selectTag(testData.get("Tag"));

            Assert.assertTrue(pgJobs.areAllTCRecords(), "Not all records are TC");
            TestReport.logPass("Passed - All displayed records are TC");
            takeScreenshot("Evidences/JobsBoard/JobsBoardTest_verifyOnlyTCRecordsDisplayed_OnlyTCDisplayed.png");

            /*
             * Step needed to run repeatedly the test cases
             *
             * */
            pgJobs.clickToggleTC();
        }
    }

    @Test(description = "Verify Candidate is wiped and shown", priority = 6)
    public void verifyWipingShowingCandidate(){

        if(goToBoard(0)){

            Assert.assertTrue(pgJobs.isCandidateWiped(testData.get("Candidate_FirstName"), testData.get("Candidate_LastName")), "Candidate not wiped");
            TestReport.logPass("Passed - Candidate successfully wiped");
            takeScreenshot("Evidences/JobsBoard/JobsBoardTest_verifyWipingShowingCandidate_Wiped.png");

            Assert.assertTrue(pgJobs.isCandidateShown(testData.get("Candidate_FirstName"), testData.get("Candidate_LastName")), "Candidate not shown");
            TestReport.logPass("Passed - Candidate successfully shown");
            takeScreenshot("Evidences/JobsBoard/JobsBoardTest_verifyWipingShowingCandidate_Shown.png");
        }
    }

   @AfterMethod
    public void tearDown() {

       finalization();
    }
}
