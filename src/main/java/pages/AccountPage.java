package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import util.Waiting;

public class AccountPage {
    private By exitButton = By.xpath(".//button[text()='Выход']");

    @Step("Выход из личного кабинета")
    public void clickExitButton(WebDriver driver) {
        driver.findElement(exitButton).click();
    }

    @Step("Дожидаемся отображения кнопки Выход")
    public void waitVisibilityOfExitButton(WebDriver driver, int time) {
        Waiting.waitVisibilityOf(driver, exitButton, time);
    }

    @Step("Проверка видимости кнопки Выйти")
    public boolean isExitButtonVisible(WebDriver driver) {
        return driver.findElement(exitButton).isDisplayed();
    }
}