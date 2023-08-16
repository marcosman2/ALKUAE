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

public class StartsBoard_TestCases extends Wrappers {

    AEHomePage pgHome;
    AEManagersPage pgManagers;
    AEBoardsPage pgBoards;
    AEStartsPage pgStarts;
    HashMap<String, String> testData;

    private Object[][] testCasesData;

    @BeforeClass
    public void deletePriorEvidences(){

        deleteScreenshots("Evidences/StartsBoard");
        TestReport.initialize("StartsBoard");
    }

    @BeforeMethod
    public void navigateToHome(ITestResult result){

        TestReport.createTest(result.getMethod().getDescription());

        initialization();
        //browserStackInitialization();
        testCasesData = loadExcelTable("Credentials");
        logIntoApplication(testCasesData, 0);
        testCasesData = loadExcelTable("Starts");

        pgHome = new AEHomePage();
        pgManagers = new AEManagersPage();
        pgBoards = new AEBoardsPage();
        pgStarts = new AEStartsPage();
    }

    public boolean goToBoard(int row){

        testData = (HashMap<String, String>) testCasesData[row][0];

        pgHome.selectDepartment(testData.get("Department"));
        pgManagers.selectManager(testData.get("User"));
        pgBoards.selectBoard(testData.get("Board"));

        return pgStarts.isStartsBoardDisplayed();
    }

    @Test(description = "Verify filter by Status", priority = 1)
    public void verifyFilterByStatus(){

        if(goToBoard(0)){

            pgStarts.selectStatus(testData.get("Status"));

            Assert.assertTrue(pgStarts.areRecordsFilteredByStatus(testData.get("Status")), "Not all displayed records belong to the selected Status");
            TestReport.logPass("Passed - All displayed records belong to the selected Status");
        }
    }

    @Test(description = "Verify filter by Company", priority = 2)
    public void verifyFilterByCompany(){

        if(goToBoard(0)){

            pgStarts.selectCompany(testData.get("Company"));

            Assert.assertTrue(pgStarts.areRecordsFilteredByCompany(testData.get("Company")), "Not all records displayed belong to the selected Company");
            TestReport.logPass("Passed - All displayed records belong to the selected Company");
        }
    }

    @Test(description = "Verify that Consultant information is hidden", priority = 3)
    public void verifyConsultantInfoIsHidden(){

        if(goToBoard(0)){

            Assert.assertTrue(pgStarts.isConsultantInfoHidden(), "Consultant information not hidden");
            TestReport.logPass("Passed - Consultant information successfully hidden");
        }
    }

    @Test(description = "Verify that Client information is hidden", priority = 4)
    public void verifyClientInfoIsHidden(){

        if(goToBoard(0)){

            Assert.assertTrue(pgStarts.isClientInfoHidden(), "Client information not hidden");
            TestReport.logPass("Passed - Client information successfully hidden");
        }
    }

    @Test(description = "Verify that 'Onboarding Paperwork' modal displays", priority = 5)
    public void verifyOnboardingPWModalDisplays(){

        if(goToBoard(0)){

            pgStarts.openOPWModal();

            Assert.assertTrue(pgStarts.isOPWModalDisplayed(), "'Onboarding Paperwork' modal not displayed");
            TestReport.logPass("Passed - 'Onboarding Paperwork' modal successfully displayed");
        }
    }

   @AfterMethod
    public void tearDown(Method method) {

       takeScreenshot("Evidences/StartsBoard/StartsBoardTest_".concat(method.getName()).concat(".png"));
       finalization();
    }
}
