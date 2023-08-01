package Pages;

import Base.TestReport;
import Base.Wrappers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class AETouchScreenPage extends Wrappers {


    @FindBy(xpath="//i[@class='c-ts-header__separator']/following-sibling::span")
    WebElement lblBoardName;

    @FindBy(xpath="//ul[@class='c-ts-menu__board-selector']/li/button")
    WebElement btnNavigationButtons;

    @FindBy(xpath="//button[contains(@class, 'menu__button')]")
    WebElement btnMenu;

    @FindBy(xpath="//button[@class='c-btn c-btn--bare']")
    WebElement btnSettings;

    @FindBy(xpath="//div[@class='c-toggle-btn__container']")
    WebElement btnAdvancedPagination;

    @FindBy(xpath="//button[@aria-label='Close']")
    WebElement btnCloseSettings;

    boolean isDisplayed = false;
    boolean areExpanded = false;
    boolean areArrows = false;
    List<WebElement> buttonsExpanded;
    List<WebElement> arrows;

    public AETouchScreenPage(){

        PageFactory.initElements(driver, this);
    }

    public void navigateToTS(String department){

        try{

            String tsURL = driver.getCurrentUrl().concat("ts/").concat(department);
            driver.get(tsURL);
            TestReport.logInfo("Navigated to '" +department+"' Department on Touch Screen mode");
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to navigate to Touch Screen mode");
        }
    }


    public void navigateToBoard(String board){

        try{

            waitForEnabled(btnNavigationButtons);
            scrollToElement(btnNavigationButtons);
            clickElement(driver.findElement(By.xpath("//ul[@class='c-ts-menu__board-selector']/li/button[contains(@aria-label, '"+board+"')]")));
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to navigate to board");
        }
    }

    public boolean isBoardDisplayed(String boardName, Boolean booleanMethod){

       try{

           waitForDisplayed(lblBoardName);

           if(booleanMethod && lblBoardName.getText().equalsIgnoreCase(boardName)){

               isDisplayed = true;

               highlightLabel(lblBoardName);
           }
           else{

               TestReport.logFail("Failed - Board "+boardName.toUpperCase()+"not displayed");
           }
       }
       catch(Exception e){

           TestReport.logFail("Failed - Issue trying to navigate to Board: "+boardName.toUpperCase());
       }

        return isDisplayed;
    }

    public boolean areBoardsButtonsExpanded(){

        try{

            buttonsExpanded = driver.findElements(By.xpath("//button[contains(@class, 'is-open')]"));

            if(buttonsExpanded.size() == 0){

                clickElement(btnMenu);
                TestReport.logInfo("Clicked on 'Menu' button");
                waitAPause(1);
                buttonsExpanded = driver.findElements(By.xpath("//button[contains(@class, 'is-open')]"));

                if(buttonsExpanded.size() == 6){

                    areExpanded = true;

                    for(WebElement buttons: buttonsExpanded){

                        highlightLabel(buttons);
                    }
                }
                else{

                    TestReport.logFail("Failed - Boards buttons not expanded");
                }
            }
            else{

                TestReport.logFail("Failed - Boards buttons already expanded");
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to validate if boards buttons are expanded");
        }

        return areExpanded;
    }

    public boolean areArrowsDisplayed(){

        try{

            arrows = driver.findElements(By.xpath("//button[contains(@class, 'pagination-arrow')]"));

            if(arrows.size() == 0){

                clickElement(btnSettings);
                TestReport.logInfo("Clicked on Settings");

                clickElement(btnAdvancedPagination);
                TestReport.logInfo("Clicked on Advanced Pagination");

                clickElement(btnCloseSettings);
                waitAPause(2);

                arrows = driver.findElements(By.xpath("//button[contains(@class, 'pagination-arrow')]"));

                if(arrows.size() == 2){

                    areArrows = true;

                    for(WebElement arrow: arrows){

                        highlightLabel(arrow);
                    }
                }
                else{

                    TestReport.logFail("Failed - Navigation arrows not displayed");
                }
            }
            else{

                TestReport.logFail("Failed - Navigation arrows already displayed");
            }
        }
        catch(Exception e){

            TestReport.logFail("Failed - Issue trying to validate if navigation arrows are displayed");
        }

        return areArrows;
    }

}
