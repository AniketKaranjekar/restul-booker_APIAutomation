package api.utilities;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.*;

public class ExtentListener implements ITestListener 
{
    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) 
    {
        System.out.println("=== Test Suite Started ===");
    }

    @Override
    public void onTestStart(ITestResult result) 
    {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) 
    {
        test.get().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) 
    {
        test.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) 
    {
        test.get().skip("Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) 
    {
        extent.flush();
        System.out.println("=== Report Generated ===");
    }
}