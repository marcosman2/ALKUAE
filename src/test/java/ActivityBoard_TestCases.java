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
import java.util.ArrayList;
import java.util.HashMap;

public class ActivityBoard_TestCases extends Wrappers {

    AEHomePage pgHome;
    AEManagersPage pgManagers;
    AEBoardsPage pgBoards;
    AEActivityPage pgActivity;
    HashMap<String, String> testData;

    private Object[][] testCasesData;

    @BeforeClass
    public void deletePriorEvidences(){

        deleteScreenshots("Evidences/ActivityBoard");
        TestReport.initialize("ActivityBoard");
    }

    @BeforeMethod
    public void navigateToHome(ITestResult result){

        TestReport.createTest(result.getMethod().getDescription());

        initialization();
        //browserStackInitialization();
        testCasesData = loadExcelTable("Credentials");
        logIntoApplication(testCasesData, 0);
        testCasesData = loadExcelTable("Activity");

        pgHome = new AEHomePage();
        pgManagers = new AEManagersPage();
        pgBoards = new AEBoardsPage();
        pgActivity = new AEActivityPage();
    }

    public void goToBoardAndAddJob(int row){

        testData = (HashMap<String, String>) testCasesData[row][0];

        pgHome.selectDepartment(testData.get("Department"));
        pgManagers.selectManager(testData.get("User"));
        pgBoards.selectBoard(testData.get("Board"));

        if(pgActivity.isActivityBoardDisplayed()){

            waitAPause(3);
            pgActivity.clickOnAddJob(testData.get("Recruiter"));
            pgActivity.selectCompanyAndPosition(testData.get("Company"), testData.get("Position"));
            pgActivity.selectDate(testData.get("Date"), testData.get("Day"));
            pgActivity.selectTimeZoneAndMeetingWay( testData.get("TimeZone"), testData.get("Phone_F2F"));
            TestReport.logInfo("Information needed to add a Job entered");
        }
    }

    @Test(description = "Verify adding and deleting job", priority = 1)
    public void verifyAddDeleteJob(){

        goToBoardAndAddJob(0);

        Assert.assertTrue(pgActivity.isInterviewInformationDisplayed(testData.get("Company"), testData.get("Position")), "Job not added");
        TestReport.logPass("Passed - Job successfully added");
        takeScreenshot("Evidences/ActivityBoard/ActivityBoardTest_verifyAddDeleteJob_Add.png");

        pgActivity.deleteJob();

        Assert.assertTrue(pgActivity.isJobDeleted(), "Job not deleted");
        TestReport.logPass("Passed - Job successfully deleted");
        takeScreenshot("Evidences/ActivityBoard/ActivityBoardTest_verifyAddDeleteJob_Delete.png");
    }

    @Test(description = "Verify adding and deleting Candidate", priority = 2)
    public void verifyAddDeleteCandidate(){

        goToBoardAndAddJob(1);
        pgActivity.addCandidate(testData.get("Candidate_FirstName"), testData.get("Candidate_LastName"));

        Assert.assertTrue(pgActivity.isCandidateAdded(testData.get("Candidate_FirstName"), testData.get("Candidate_LastName")), "Candidate not added");
        TestReport.logPass("Passed - Candidate successfully added");
        takeScreenshot("Evidences/ActivityBoard/ActivityBoardTest_verifyAddDeleteCandidate_Add.png");

        pgActivity.deleteCandidate(testData.get("Candidate_FirstName"), testData.get("Candidate_LastName"));

        Assert.assertTrue(pgActivity.isCandidateDeleted(testData.get("Candidate_FirstName"), testData.get("Candidate_LastName")), "Candidate not deleted");
        TestReport.logPass("Passed - Candidate successfully deleted");
        takeScreenshot("Evidences/ActivityBoard/ActivityBoardTest_verifyAddDeleteCandidate_Delete.png");

        pgActivity.deleteJob();
    }

    @Test(description = "Verify Candidate history is displayed", priority = 3)
    public void verifyViewCandidateHistory(){

        goToBoardAndAddJob(2);
        pgActivity.addCandidate(testData.get("Candidate_FirstName"), testData.get("Candidate_LastName"));
        pgActivity.viewCandidateHistory(testData.get("Candidate_FirstName"), testData.get("Candidate_LastName"));

        takeScreenshot("Evidences/ActivityBoard/ActivityBoardTest_verifyViewCandidateHistory.png");
        Assert.assertTrue(pgActivity.isCandidateHistoryDisplayed(testData.get("Candidate_FirstName"), testData.get("Candidate_LastName")), "Candidate History not displayed");
        TestReport.logPass("Candidate History modal successfully displayed");
    }

    @Test(description = "Verify Job Id link takes to Bullhorn", priority = 4)
    public void verifyJobIdLink(){

        goToBoardAndAddJob(3);
        pgActivity.clickOnJobId();

        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        waitAPause(3);
        takeScreenshot("Evidences/ActivityBoard/ActivityBoardTest_verifyJobIdLink.png");
        Assert.assertTrue(pgActivity.isBullhornDisplayed(), "Candidate History not displayed");
        TestReport.logPass("Passed - Bullhorn login screen successfully displayed");
    }

    @Test(description = "Verify Conference Line information displayed", priority = 5)
    public void verifyConferenceInfoDisplayed(){

        goToBoardAndAddJob(4);
        pgActivity.clickOnConferenceLine();
        pgActivity.setConferenceLine(testData.get("Conference_Line"));

        Assert.assertTrue(pgActivity.isConferenceInfoTheExpected(testData.get("Conference_Line")));
        TestReport.logPass("Passed - Conference Line information successfully displayed");
        takeScreenshot("Evidences/ActivityBoard/ActivityBoardTest_verifyConferenceInfoDisplayed.png");
    }

    @Test(description = "Verify Zoom information displayed", priority = 6)
    public void verifyZoomInfoDisplayed(){

        goToBoardAndAddJob(5);
        pgActivity.clickOnConferenceLine();
        pgActivity.setZoomInformation(testData.get("Zoom_Text"), testData.get("Zoom_Link"));

        Assert.assertTrue(pgActivity.isConferenceInfoTheExpected(testData.get("Zoom_Text")));
        TestReport.logPass("Passed - Zoom call information successfully displayed");
        takeScreenshot("Evidences/ActivityBoard/ActivityBoardTest_verifyZoomInfoDisplayed.png");
    }

    @Test(description = "Verify 'Today' tab displays the corresponding Job", priority = 7)
    public void verifyTodayTab(){

        goToBoardAndAddJob(6);
        pgActivity.clickOnToday();

        Assert.assertTrue(pgActivity.isInterviewInformationDisplayed(testData.get("Company"), testData.get("Position")), "Job not added");
        TestReport.logPass("Passed - Job successfully displayed on 'Today' tab");
        takeScreenshot("Evidences/ActivityBoard/ActivityBoardTest_verifyTodayTab_JobDisplayed.png");

        pgActivity.clickOnAll();
        pgActivity.deleteJob();
        pgActivity.clickOnToday();

        takeScreenshot("Evidences/ActivityBoard/ActivityBoardTest_verifyTodayTab_JobDeleted.png");
        Assert.assertTrue(pgActivity.isJobDeleted(), "Job not deleted");
        TestReport.logPass("Passed - Deleted Job no longer displayed on 'Today' tab");
    }

    @Test(description = "Verify 'Next Day' tab displays the corresponding Job", priority = 8)
    public void verifyNextDayTab(){

        goToBoardAndAddJob(7);
        pgActivity.clickOnNextDay();

        Assert.assertTrue(pgActivity.isInterviewInformationDisplayed(testData.get("Company"), testData.get("Position")), "Job not added");
        TestReport.logPass("Passed - Job successfully displayed on 'Next Day' tab");
        takeScreenshot("Evidences/ActivityBoard/ActivityBoardTest_verifyNextDayTab_JobDisplayed.png");

        pgActivity.clickOnAll();
        pgActivity.deleteJob();
        pgActivity.clickOnNextDay();

        takeScreenshot("Evidences/ActivityBoard/ActivityBoardTest_verifyNextDayTab_JobDeleted.png");
        Assert.assertTrue(pgActivity.isJobDeleted(), "Job not deleted");
        TestReport.logPass("Passed - Deleted Job no longer displayed on 'Next Day' tab");
    }

   @AfterMethod
    public void tearDown(Method method) {

       finalization();
    }
}
