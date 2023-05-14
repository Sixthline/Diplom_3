import client.UserApi;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.*;
import util.Waiting;

import static org.junit.Assert.assertTrue;

public class LoginTest {
    WebDriver driver;
    User user;
    UserApi userApi = new UserApi();
    MainPage mainPage = new MainPage();
    HeaderPage headerPage = new HeaderPage();
    LoginPage loginPage = new LoginPage();
    RegisterPage registerPage = new RegisterPage();
    ResetPasswordPage resetPasswordPage = new ResetPasswordPage();

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://stellarburgers.nomoreparties.site/");
        user = new User("tst_user2023@ya.ru", "password1", "tst_user2023");
        userApi.createUser(user);
    }

    @Test
    @DisplayName("Проверка логина: вход по кнопке «Войти в аккаунт» на главной")
    public void loginToAccountWithLoginButton() {
        mainPage.clickLoginButton(driver);
        loginPage.makeSignIn(driver, user.getEmail(), user.getPassword());
        mainPage.waitVisibilityOfMakeOrderButton(driver, 1);

        assertTrue(mainPage.isMakeOrderButtonVisible(driver));
    }

    @Test
    @DisplayName("Проверка логина: вход через кнопку «Личный кабинет»")
    public void loginToAccountWithClickPersonalAccountButton() {
        headerPage.clickPersonalAccount(driver);
        loginPage.makeSignIn(driver, user.getEmail(), user.getPassword());
        mainPage.waitVisibilityOfMakeOrderButton(driver, 1);

        assertTrue(mainPage.isMakeOrderButtonVisible(driver));
    }

    @Test
    @DisplayName("Проверка логина: вход через кнопку в форме регистрации")
    public void loginToAccountWithRegistrationButton() {
        headerPage.clickPersonalAccount(driver);
        loginPage.clickRegisterButton(driver);
        registerPage.clickSignInButton(driver);
        loginPage.makeSignIn(driver, user.getEmail(), user.getPassword());
        mainPage.waitVisibilityOfMakeOrderButton(driver, 1);

        assertTrue(mainPage.isMakeOrderButtonVisible(driver));
    }

    @Test
    @DisplayName("Проверка логина: вход через кнопку в форме восстановления пароля")
    public void loginToAccountWithResetPasswordButton() {
        headerPage.clickPersonalAccount(driver);
        loginPage.clickResetPasswordButton(driver);
        resetPasswordPage.clickSignInButton(driver);
        loginPage.makeSignIn(driver, user.getEmail(), user.getPassword());
        mainPage.waitVisibilityOfMakeOrderButton(driver, 1);

        assertTrue(mainPage.isMakeOrderButtonVisible(driver));
    }

    @After
    public void tearDown() {
        driver.quit();
        Response loginResponse = userApi.loginUser(user);
        String accessToken = loginResponse.then().extract().path("accessToken");
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }
}