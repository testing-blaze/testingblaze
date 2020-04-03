/*
 * Copyright 2020
 *
 * This file is part of  Test Blaze Bdd Framework [Test Blaze Automation Solution].
 *
 * Test Blaze Bdd Framework is licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.testblaze.actionsfactory.elementfunctions;

import com.testblaze.actionsfactory.abstracts.LocatorProcessing;
import com.testblaze.controller.DeviceBucket;
import com.testblaze.exception.TestBlazeRunTimeException;
import com.testblaze.objects.InstanceRecording;
import com.testblaze.register.EnvironmentFetcher;
import com.testblaze.register.i;
import com.testblaze.report.LogLevel;
import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.ByAngular.Factory;
import com.paulhammant.ngwebdriver.ByAngularBinding;
import com.paulhammant.ngwebdriver.ByAngularButtonText;
import com.paulhammant.ngwebdriver.ByAngularCssContainingText;
import com.paulhammant.ngwebdriver.ByAngularExactBinding;
import com.paulhammant.ngwebdriver.ByAngularModel;
import com.paulhammant.ngwebdriver.ByAngularOptions;
import com.paulhammant.ngwebdriver.ByAngularPartialButtonText;
import com.paulhammant.ngwebdriver.ByAngularRepeater;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Handles angular specific locators
 *
 * @author nauman.shahid
 */
public final class Ng {
    private NgWebDriver ngDriver;
    private WebDriver driver;
    private LocatorProcessing getLocator;

    public Ng() {
        if (!EnvironmentFetcher.getDevice().equalsIgnoreCase("android") || !EnvironmentFetcher.getDevice().equalsIgnoreCase("ios")) {
            this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
            this.ngDriver = new NgWebDriver((JavascriptExecutor) this.driver);
            ngDriver.waitForAngularRequestsToFinish();
            this.getLocator = InstanceRecording.getInstance(LocatorProcessing.class);
        }
    }

    /**
     * Access angular based attributes and element
     *
     * @param locatorWithType MODEL,BINDING,BUTTON TEXT,
     * @return
     */
    public WebElement getNgElement(String locatorWithType,Boolean processing) {
        WebElement fetchElement = null;
        String locatorType=locatorWithType.split(":")[0];
        String refinedLocator = getLocator.get(locatorWithType);
        switch (locatorType.toUpperCase()) {
            case "MODEL":
                fetchElement = getElementWithModel(refinedLocator);
                break;
            case "BINDING":
                fetchElement = getElementWithBinding(refinedLocator);
                break;
            case "BUTTON_TEXT":
                fetchElement = getElementWithButtonText(refinedLocator);
                break;
            case "EXACT_BINDING":
                fetchElement = getElementWithExactBidning(refinedLocator);
                break;
            case "CSS CONTAINING TEXT":
                //fetchElement = getElementWithCssContainingText(String cssSelector, String searchText); //not functional at the moment. need to add something to regex
                break;
            case "OPTIONS":
                fetchElement = getElementWithOptions(refinedLocator);
                break;
            case "PARTIAL_BUTTON_TEXT":
                fetchElement = getElementWithpartialButtonText(refinedLocator);
                break;
            case "REPEATER":
                fetchElement = getElementWithRepeater(refinedLocator);
                break;
            case "EXACT_REPEATER":
                fetchElement = getElementWithexactRepeater(refinedLocator);
                break;
        }
        return fetchElement;
    }

    /**
     * Access angular based attributes and element
     *
     * @param locatorWithType MODEL,BINDING,BUTTON TEXT,
     * @return
     */
    public List<WebElement> getNgElements(String locatorWithType,Boolean processing) {
        List<WebElement> fetchElement = null;
        String locatorType=locatorWithType.split(":")[0];
        String refinedLocator = getLocator.get(locatorWithType);
        switch (locatorType.toUpperCase()) {
            case "MODEL":
                fetchElement = getElementsWithModel(refinedLocator);
                break;
            case "BINDING":
                fetchElement = getElementsWithBinding(refinedLocator);
                break;
            case "BUTTON_TEXT":
                fetchElement = getElementsWithButtonText(refinedLocator);
                break;
            case "EXACT_BINDING":
                fetchElement = getElementsWithExactBidning(refinedLocator);
                break;
            case "CSS CONTAINING TEXT":
                //fetchElement = getElementWithCssContainingText(String cssSelector, String searchText); //not functional at the moment. need to add something to regex
                break;
            case "OPTIONS":
                fetchElement = getElementsWithOptions(refinedLocator);
                break;
            case "PARTIAL_BUTTON_TEXT":
                i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Inside partial buttone text :"+refinedLocator);
                fetchElement = getElementsWithpartialButtonText(refinedLocator);
                break;
            case "REPEATER":
                fetchElement = getElementsWithRepeater(refinedLocator);
                break;
            case "EXACT_REPEATER":
                fetchElement = getElementsWithexactRepeater(refinedLocator);
                break;
        }
        if(fetchElement == null)throw new TestBlazeRunTimeException("Angular element is not present or incorrect: "+refinedLocator);
        return fetchElement;
    }

    /**
     * get element using Model attribute
     *
     * @param model
     * @return
     */
    private WebElement getElementWithModel(String model) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + model);
        return driver.findElement(model(model));
    }

    /**
     * get element by using binding attribute value
     *
     * @param binding
     * @return
     */
    private WebElement getElementWithBinding(String binding) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + binding);
        return driver.findElement(binding(binding));
    }

    /**
     * use button text attribute
     *
     * @param buttonText
     * @return WebElement
     */
    private WebElement getElementWithButtonText(String buttonText) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + buttonText);
        return driver.findElement(buttonText(buttonText));
    }

    /**
     * @param exactBinding
     * @return WebElement
     */
    private WebElement getElementWithExactBidning(String exactBinding) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + exactBinding);
        return driver.findElement(exactBinding(exactBinding));
    }

    /**
     * @param cssSelector
     * @param searchText
     * @return WebElement
     */
    private WebElement getElementWithCssContainingText(String cssSelector, String searchText) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + cssSelector);
        return driver.findElement(cssContainingText(cssSelector, searchText));
    }

    /**
     * @param options
     * @return WebElement
     */
    private WebElement getElementWithOptions(String options) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + options);
        return driver.findElement(options(options));
    }

    /**
     * @param partialButtonText
     * @return WebElement
     */
    private WebElement getElementWithpartialButtonText(String partialButtonText) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + partialButtonText);
        return driver.findElement(partialButtonText(partialButtonText));
    }

    /**
     * @param repeater
     * @return WebElement
     */
    private WebElement getElementWithRepeater(String repeater) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + repeater);
        return driver.findElement(repeater(repeater));
    }

    /**
     * @param exactRepeater
     * @return WebElement
     */
    private WebElement getElementWithexactRepeater(String exactRepeater) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + exactRepeater);
        return driver.findElement(exactRepeater(exactRepeater));
    }

    /**
     * get element using Model attribute
     *
     * @param model
     * @return
     */
    private List<WebElement> getElementsWithModel(String model) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + model);
        return driver.findElements(model(model));
    }

    /**
     * get element by using binding attribute value
     *
     * @param binding
     * @return
     */
    private List<WebElement> getElementsWithBinding(String binding) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + binding);
        return driver.findElements(binding(binding));
    }

    /**
     * use button text attribute
     *
     * @param buttonText
     * @return WebElement
     */
    private List<WebElement> getElementsWithButtonText(String buttonText) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + buttonText);
        return driver.findElements(buttonText(buttonText));
    }

    /**
     * @param exactBinding
     * @return WebElement
     */
    private List<WebElement> getElementsWithExactBidning(String exactBinding) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + exactBinding);
        return driver.findElements(exactBinding(exactBinding));
    }

    /**
     * @param cssSelector
     * @param searchText
     * @return WebElement
     */
    private List<WebElement> getElementsWithCssContainingText(String cssSelector, String searchText) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + cssSelector);
        return driver.findElements(cssContainingText(cssSelector, searchText));
    }

    /**
     * @param options
     * @return WebElement
     */
    private List<WebElement> getElementsWithOptions(String options) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + options);
        return driver.findElements(options(options));
    }

    /**
     * @param partialButtonText
     * @return WebElement
     */
    private List<WebElement> getElementsWithpartialButtonText(String partialButtonText) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + partialButtonText);
        return driver.findElements(partialButtonText(partialButtonText));
    }

    /**
     * @param repeater
     * @return WebElement
     */
    private List<WebElement> getElementsWithRepeater(String repeater) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + repeater);
        return driver.findElements(repeater(repeater));
    }

    /**
     * @param exactRepeater
     * @return WebElement
     */
    private List<WebElement> getElementsWithexactRepeater(String exactRepeater) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + exactRepeater);
        return driver.findElements(exactRepeater(exactRepeater));
    }
    /**
     * @param rootSelector
     * @return Angular control to receive a locator
     */
    public Factory rootSelector(String rootSelector) {
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Accessing ng element " + rootSelector);
        return ByAngular.withRootSelector(rootSelector);
    }

    private ByAngularModel model(String model) {
        return ByAngular.model(model);
    }

    private ByAngularBinding binding(String binding) {
        return ByAngular.binding(binding);
    }

    private ByAngularButtonText buttonText(String buttonText) {
        return ByAngular.buttonText(buttonText);
    }

    private ByAngularCssContainingText cssContainingText(String cssSelector, String searchText) {
        return ByAngular.cssContainingText(cssSelector, searchText);
    }

    private ByAngularExactBinding exactBinding(String exactBinding) {
        return ByAngular.exactBinding(exactBinding);
    }

    private ByAngularOptions options(String options) {
        return ByAngular.options(options);
    }

    private ByAngularPartialButtonText partialButtonText(String partialButtonText) {
        return ByAngular.partialButtonText(partialButtonText);
    }

    private ByAngularRepeater repeater(String repeater) {
        return ByAngular.repeater(repeater);
    }

    private ByAngularRepeater exactRepeater(String exactRepeater) {
        return ByAngular.exactRepeater(exactRepeater);
    }

}