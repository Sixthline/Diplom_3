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
import pages.LoginPage;
import pages.MainPage;
import pages.RegisterPage;
import util.Waiting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RegistrationTest {
    WebDriver driver;
    User user;
    UserApi userApi = new UserApi();
    MainPage mainPage = new MainPage();
    RegisterPage registerPage = new RegisterPage();
    LoginPage loginPage = new LoginPage();
    Waiting waiting = new Waiting();

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.get("https://stellarburgers.nomoreparties.site/");
        driver.manage().window().maximize();
        user = new User("tst_user2023@ya.ru", "password1", "tst_user2023");
    }

    @Test
    @DisplayName("Проверка успешной регистрации")
    public void successRegisterTest() {
        mainPage.clickLoginButton(driver);
        waiting.waitVisibilityOfRegisterButton(driver, 1);
        loginPage.clickRegisterButton(driver);
        waiting.waitVisibilityOfEmailField(driver, 1);
        registerPage.makeRegistration(driver, user.getName(), user.getEmail(), user.getPassword());
        waiting.waitVisibilityOfRegisterButton(driver, 1);

        assertEquals(driver.getCurrentUrl(), "https://stellarburgers.nomoreparties.site/login");
        loginPage.makeSignIn(driver, user.getEmail(), user.getPassword());
        waiting.waitVisibilityOfMakeOrderButton(driver, 1);

        assertTrue(mainPage.isMakeOrderButtonVisible(driver));
    }

    @Test
    @DisplayName("Проверка появления сообщения с ошибкой при введении некорректного пароля")
    public void shortPasswordMakesErrorMessageTest() {
        mainPage.clickLoginButton(driver);
        waiting.waitVisibilityOfRegisterButton(driver, 1);
        loginPage.clickRegisterButton(driver);
        waiting.waitVisibilityOfEmailField(driver, 1);
        // Меняем пароль на некорректный (менее 6-ти символов)
        user.setPassword("four");
        registerPage.makeRegistration(driver, user.getName(), user.getEmail(), user.getPassword());

        assertTrue(registerPage.isIncorrectPasswordVisible(driver));
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