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

public class AABoard_TestCases extends Wrappers {

    AEHomePage pgHome;
    AEManagersPage pgManagers;
    AEBoardsPage pgBoards;
    AEAssignedAccountsPage pgAA;
    HashMap<String, String> testData;

    private Object[][] testCasesData;

    @BeforeClass
    public void deletePriorEvidences(){

        deleteScreenshots("Evidences/AssignedAccounts");
        TestReport.initialize("AssignedAccountsBoard");
    }

    @BeforeMethod
    public void navigateToHome(ITestResult result){

        TestReport.createTest(result.getMethod().getDescription());

        initialization();
        //browserStackInitialization();
        testCasesData = loadExcelTable("Credentials");
        logIntoApplication(testCasesData, 0);
        testCasesData = loadExcelTable("AA");
        testData = (HashMap<String, String>) testCasesData[0][0];

        pgHome = new AEHomePage();
        pgManagers = new AEManagersPage();
        pgBoards = new AEBoardsPage();
        pgAA = new AEAssignedAccountsPage();
    }

    public boolean goToBoard(int row){

        testData = (HashMap<String, String>) testCasesData[row][0];

        pgHome.selectDepartment(testData.get("Department"));
        pgManagers.selectManager(testData.get("User"));
        pgBoards.selectBoard(testData.get("Board"));

        return pgAA.isAABoardDisplayed();
    }

    @Test(description = "Verify that records al filtered by Company", priority = 1)
    public void verifyFilteringByCompany(){

        if(goToBoard(0)){

            pgAA.selectCompany(testData.get("Company"));

            Assert.assertTrue(pgAA.allRecordsBelongToSelectedCompany(testData.get("Company")), "Not all records displayed belong to selected company");
            TestReport.logPass("Passed - All displayed records belong to selected Company");
        }
    }

    @Test(description = "Verify that records al filtered by Account Manager", priority = 2)
    public void verifyFilteringByAM(){

        if(goToBoard(0)){

            pgAA.selectAM(testData.get("AM_FirstName").concat(" ").concat(testData.get("AM_LastName")));

            Assert.assertTrue(pgAA.allRecordsBelongToSelectedAM(testData.get("AM_FirstName"), testData.get("AM_LastName")), "Not all records displayed belong to the selected AM");
            TestReport.logPass("Passed - All displayed records belong to selected Account Manager");
        }
    }

    @Test(description = "Verify that records al filtered by Division", priority = 3)
    public void verifyFilteringByDivision(){

        if(goToBoard(0)){

            pgAA.selectDivision(testData.get("Division"));

            Assert.assertTrue(pgAA.allRecordsBelongToSelectedDivision(testData.get("Division")), "Not all records displayed belong to selected Division");
            TestReport.logPass("Passed - All displayed records belong to selected Division");
        }
    }

    @Test(description = "Verify that records al filtered by Program", priority = 4)
    public void verifyFilteringByProgram(){

        if(goToBoard(0)){

            pgAA.setDdSelectProgram(testData.get("Program"));

            Assert.assertTrue(pgAA.allRecordsBelongToSelectedProgram(testData.get("Program")), "Not all records displayed belong to selected Program");
            TestReport.logPass("Passed - All displayed records belong to selected Program");
        }
    }

   @AfterMethod
    public void tearDown(Method method) {

       takeScreenshot("Evidences/AssignedAccounts/AA_".concat(method.getName()).concat(".png"));
       finalization();
    }
}
