/*
	This is the Geb configuration file.

	See: https://groovy.apache.org/geb/manual/current/#configuration
*/

import org.openqa.selenium.firefox.FirefoxDriver

waiting {
    timeout = 2
}

atCheckWaiting = 1

driver = { new FirefoxDriver() }

baseUrl = "https://groovy.apache.org/geb/"
