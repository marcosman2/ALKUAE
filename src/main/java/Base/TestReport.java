package Base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentReports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class TestReport {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static void initialize(String reportName) {
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("test-output/".concat(reportName).concat(".html"));
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    public static void createTest(String testName) {
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
    }

    public static void logInfo(String message) {
        test.get().log(Status.INFO, message);
    }

    public static void logPass(String message) {
        test.get().log(Status.PASS, message);
    }

    public static void logFail(String message) {
        test.get().log(Status.FAIL, message);
    }

    public static void flushReport() {
        extent.flush();
    }
}