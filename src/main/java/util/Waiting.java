package util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Waiting {
    public static void waitVisibilityOf(WebDriver driver, By element, int time) {
        new WebDriverWait(driver, time)
                .until(ExpectedConditions.visibilityOfElementLocated(element));
    }

//    public void waitVisibilityOfExitButton(WebDriver driver, int time) {
//        waitVisibilityOf(driver, By.xpath(".//button[text()='Выход']"), time);
//    }
}
