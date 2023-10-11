package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.domain.users.Users;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;


class OAuthLoginServiceTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Test
    public void testOAuth2Google() {
//        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
//        driver = new ChromeDriver(options);
//        driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=email%20profile&response_type=code&redirect_uri=https://moyeota-webview.netlify.app&client_id=1080355904017-bc0ejbid1j01td1ur921cpvur768s8s5.apps.googleusercontent.com");
//        WebElement emailField = driver.findElement(By.name("identifier"));
//        emailField.sendKeys("taehuen7757@gmail.com");
//        emailField.submit();
//        WebElement passwordField = driver.findElement(By.name("password"));
//        passwordField.sendKeys("rlaxogjs8312@");
//        passwordField.submit();
//        WebElement allowButton = driver.findElement(By.id("submit-allow"));
//        allowButton.click();
//        String currentUrl = driver.getCurrentUrl();
//        String code = currentUrl.substring(currentUrl.indexOf("code=") + 5);
//        System.out.println("OAuth 2.0 Code: " + code);
    }
}