/*
	This is the Geb configuration file.

	See: https://groovy.apache.org/geb/manual/current/current/#configuration
*/

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver

waiting {
    timeout = 2
}

environments {

    // run via “./gradlew chromeTest”
    // See: https://www.selenium.dev/documentation/webdriver/browsers/chrome/
    chrome {
        driver = { new ChromeDriver() }
    }

    // run via “./gradlew chromeHeadlessTest”
    // See: https://www.selenium.dev/documentation/webdriver/browsers/chrome/
    chromeHeadless {
        driver = {
            ChromeOptions o = new ChromeOptions()
            o.addArguments('headless')
            new ChromeDriver(o)
        }
    }

    // run via “./gradlew firefoxTest”
    // See: https://www.selenium.dev/documentation/webdriver/browsers/firefox/
    firefox {
        atCheckWaiting = 1

        driver = { new FirefoxDriver() }
    }

}

// To run the tests with all browsers just run “./gradlew test”

baseUrl = "https://groovy.apache.org/geb/"