import client.UserApi;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.AccountPage;
import pages.HeaderPage;
import pages.LoginPage;
import pages.MainPage;
import util.Waiting;

import static org.junit.Assert.assertTrue;

public class PersonalAccountTest {
    WebDriver driver;
    User user;
    UserApi userApi = new UserApi();
    MainPage mainPage = new MainPage();
    AccountPage accountPage = new AccountPage();
    HeaderPage headerPage = new HeaderPage();
    LoginPage loginPage = new LoginPage();
    Waiting waiting = new Waiting();

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
    @DisplayName("Проверка перехода по логотипу Личного кабинета")
    public void goToPersonalAccountWithHeaderButton() {
        headerPage.clickPersonalAccount(driver);
        loginPage.makeSignIn(driver, user.getEmail(), user.getPassword());
        headerPage.clickPersonalAccount(driver);
        accountPage.waitVisibilityOfExitButton(driver, 1);
        assertTrue(accountPage.isExitButtonVisible(driver));
    }

    @Test
    @DisplayName("Проверка перехода по логотипу Stellar Burgers")
    public void goToMainPageFromPersonalAccountWithLogoBurger() {
        headerPage.clickPersonalAccount(driver);
        loginPage.makeSignIn(driver, user.getEmail(), user.getPassword());
        headerPage.clickPersonalAccount(driver);
        accountPage.waitVisibilityOfExitButton(driver, 1);
        headerPage.clickStellarBurgers(driver);

        assertTrue(mainPage.isMakeOrderButtonVisible(driver));
    }

    @Test
    @DisplayName("Проверка перехода по логотипу Конструктора")
    public void goToMainPageFromPersonalAccountWithLogoConstructor() {
        headerPage.clickPersonalAccount(driver);
        loginPage.makeSignIn(driver, user.getEmail(), user.getPassword());
        headerPage.clickPersonalAccount(driver);
        accountPage.waitVisibilityOfExitButton(driver, 1);
        headerPage.clickConstructor(driver);
        assertTrue(mainPage.isMakeOrderButtonVisible(driver));
    }

    @Test
    @DisplayName("Проверка выхода из Личного кабинета")
    public void exitFromPersonalAccount() {
        headerPage.clickPersonalAccount(driver);
        loginPage.makeSignIn(driver, user.getEmail(), user.getPassword());
        headerPage.clickPersonalAccount(driver);
        accountPage.waitVisibilityOfExitButton(driver, 2);
        accountPage.clickExitButton(driver);
        loginPage.waitVisibilityOfEmailField(driver, 1);
        assertTrue(loginPage.isSignInButtonVisible(driver));
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