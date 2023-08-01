package Pages;

import Base.TestReport;
import Base.Wrappers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AEBoardsPage extends Wrappers {

    @FindBy(xpath = "//button[@class='ant-dropdown-trigger c-sidebar__user-kebab-btn']")
    private WebElement iconUserOptions;

    @FindBy(xpath = "//div[@class='c-sidebar__user-kebab-menu-item']")
    private WebElement linkUserOptions;

    @FindBy(xpath = "//div[@class='headerless-header']/div")
    private WebElement lblHeaderlessTitle;

    @FindBy(xpath = "//div[@class='headerless-header']/img[@alt='alku logo']")
    private WebElement imgHeaderlessLogo;

    @FindBy(xpath = "//span[@class='ant-select-selection-item']")
    private WebElement ddPeriod;

    boolean isHeaderlessDisplayed = false;

    public AEBoardsPage(){

        PageFactory.initElements(driver, this);
    }

    public void selectUserOption(String option){

        try{

            clickElement(iconUserOptions);
            waitForDisplayed(linkUserOptions);
            clickElement(driver.findElement(By.xpath("//button[@class='ant-dropdown-trigger c-sidebar__user-kebab-btn' and contains(text(), '"+option+"')]")));
            TestReport.logInfo("User option selected: "+option);
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying select user option: "+option);
        }
    }

    public void selectBoard(String board){

        waitForDisplayed(iconUserOptions);
        clickElement(driver.findElement(By.xpath("//span[@class='c-sidebar__link-menu-item-label c-sidebar__hide-collapsed' and contains (text(), '"+board+"')]/parent::*")));
    }

    public void selectHeaderlessBoard(String board){

        String headerlessURL = driver.getCurrentUrl().replace("header", "headerless");
        driver.get(headerlessURL);
    }

    public Boolean getHeaderlessBoardTitle(String expectedTitle){

        try{

            waitForDisplayed(lblHeaderlessTitle);
            String boardTitle = lblHeaderlessTitle.getAttribute("innerText");

            if(boardTitle.trim().equalsIgnoreCase(expectedTitle)){

                isHeaderlessDisplayed = true;
            }
        }
        catch(Exception e){

            TestReport.logFail(expectedTitle+" board on Headerless mode not displayed");
        }

        return isHeaderlessDisplayed;
    }

    public boolean isHeaderlessLogoDisplayed(){

        try{

            waitForDisplayed(imgHeaderlessLogo);
            isHeaderlessDisplayed = true;
        }
        catch(Exception e){

            TestReport.logFail("Failed - Assigned Accounts board on Headerless mode not displayed");
        }

        return isHeaderlessDisplayed;
    }

    public void expandPeriodDropdown(){

        clickElement(ddPeriod);
    }

    public void selectPeriod(String period){

        try{

            clickElement(driver.findElement(By.xpath("//div[@class='ant-select-item-option-content']/div[contains(text(), '"+period+"')]")));
            TestReport.logInfo("Selected Period: "+period);
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to select Period");
        }
    }

    public String getPeriodDateRange(String month){

        String period = driver.findElement(By.xpath("//div[@class='ant-select-item-option-content']/div[contains(text(), '"+month+"')]")).getAttribute("innerText");

        String regex = "\\((.*?)\\)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(period);

        if (matcher.find()) {

            String extractedText = matcher.group(1);
            return extractedText;
        } else {
            return null;
        }
    }
}