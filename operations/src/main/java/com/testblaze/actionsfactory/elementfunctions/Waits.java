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
import com.testblaze.objects.InstanceRecording;
import com.testblaze.register.EnvironmentFetcher;
import com.testblaze.register.i;
import com.testblaze.report.LogLevel;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author nauman.shahid
 * @category Manages different waits
 */

public final class Waits {
    public static final long STANDARD_WAIT_TIME = EnvironmentFetcher.getMaxWaitTime();
    private final long STANDARD_POLLING_TIME = 1;
    private FluentWait<WebDriver> wait;
    private FluentWait<WebDriver> waitToInvisible;
    private LocatorProcessing getLocator;

    public Waits() {
        this.getLocator = InstanceRecording.getInstance(LocatorProcessing.class);
    }

    /**
     * Waits for an element to be present on a page, based on a By locator
     *
     * @param locator        The locator of the element to look for
     * @param customWaitTime Optional. Time to wait for the element in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return The element which was found on the page
     */
    public void WaitForElementToBePresent(By locator, long customWaitTime) {
        By refinedFinalLocator = getLocator.get(locator);
        waitFor(refinedFinalLocator, ExpectedConditions.presenceOfElementLocated(refinedFinalLocator), customWaitTime);
    }

    public void WaitForElementToBePresent(By locator) {
        WaitForElementToBePresent(locator, STANDARD_WAIT_TIME);
    }


    /**
     * Waits for an element to be clickable on a page, based on a By locator
     *
     * @param locator        The locator of the element to look for
     * @param customWaitTime Optional. Time to wait for the element in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return The element which was found on the page
     */
    public void WaitForElementToBeClickable(By locator, long customWaitTime) {
        By refinedFinalLocator = getLocator.get(locator);
        waitFor(refinedFinalLocator, ExpectedConditions.elementToBeClickable(getLocator.get(refinedFinalLocator)), customWaitTime);
    }

    public void WaitForElementToBeClickable(By locator) {
        WaitForElementToBeClickable(locator, STANDARD_WAIT_TIME);
    }

    /**
     * Waits for an element to be displayed on a page, based on a By locator
     *
     * @param locator        The locator of the element to look for
     * @param customWaitTime Optional. Time to wait for the element in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return The element which was found on the page
     */
    public void WaitForElementToBeVisible(By locator, long customWaitTime) {
        By refinedFinalLocator = getLocator.get(locator);
        waitFor(refinedFinalLocator, ExpectedConditions.visibilityOfElementLocated(getLocator.get(refinedFinalLocator)), customWaitTime);
    }

    public void WaitForElementToBeVisible(By locator) {
        WaitForElementToBeVisible(locator, STANDARD_WAIT_TIME);
    }

    /**
     * Waits for an element to no longer be displayed on a page, based on a By locator
     *
     * @param locator        The locator of the element to look for
     * @param customWaitTime Optional. Time to wait for the element in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return A boolean indicating whether or not the element disappeared
     */
    public Boolean waitToDisappearForProcessing(By locator, long customWaitTime) {
        if (waitToInvisible == null) {
            waitToInvisible = new WebDriverWait(InstanceRecording.getInstance(DeviceBucket.class).getDriver(), customWaitTime).pollingEvery(STANDARD_POLLING_TIME, TimeUnit.SECONDS);
        }
        return waitToInvisible.withTimeout(Duration.ofSeconds(STANDARD_WAIT_TIME)).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for an element to no longer be displayed on a page, based on a By locator
     *
     * @param locator        The locator of the element to look for
     * @param customWaitTime Optional. Time to wait for the element in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return A boolean indicating whether or not the element disappeared
     */
    public Boolean WaitForElementToDisappear(By locator, long customWaitTime) {
        By refinedFinalLocator = getLocator.get(locator);
        return waitFor(refinedFinalLocator, ExpectedConditions.invisibilityOfElementLocated(getLocator.get(refinedFinalLocator)), customWaitTime);
    }

    public Boolean WaitForElementToDisappear(By locator) {
        return WaitForElementToDisappear(locator, STANDARD_WAIT_TIME);
    }

    /**
     * Waits for an element to be selected on a page, based on a By locator
     *
     * @param locator        The locator of the element to look for
     * @param customWaitTime Optional. Time to wait for the element in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return A boolean indicating whether or not the element was selected
     */
    public Boolean WaitForElementToBeSelected(By locator, long customWaitTime) {
        By refinedFinalLocator = getLocator.get(locator);
        return waitFor(refinedFinalLocator, ExpectedConditions.elementToBeSelected(getLocator.get(refinedFinalLocator)), customWaitTime);
    }

    public Boolean WaitForElementToBeSelected(By locator) {
        return WaitForElementToBeSelected(locator, STANDARD_WAIT_TIME);
    }

    /* Wait For single elements by WebElement */

    /**
     * Waits for a WebElement to become clickable on a page
     *
     * @param element        The element which should become clickable
     * @param customWaitTime Optional. Time to wait for the element in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return The element which became clickable
     */
    public void WaitForWebElementToBeClickable(WebElement element, long customWaitTime) {
        waitFor(element, ExpectedConditions.elementToBeClickable(element), customWaitTime);
    }

    public void WaitForWebElementToBeClickable(WebElement element) {
        WaitForWebElementToBeClickable(element, STANDARD_WAIT_TIME);
    }


    /**
     * Waits for a WebElement to become visible on a page
     *
     * @param element        The element which should become visible
     * @param customWaitTime Optional. Time to wait for the element in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return The element which became visible
     */
    public void WaitForWebElementToBeVisible(WebElement element, long customWaitTime) {
        waitFor(element, ExpectedConditions.visibilityOf(element), customWaitTime);
    }

    public void WaitForWebElementToBeVisible(WebElement element) {
        WaitForWebElementToBeVisible(element, STANDARD_WAIT_TIME);
    }

    /**
     * Waits for a WebElement to no longer be visible on a page
     *
     * @param element        The element which should become not visible
     * @param customWaitTime Optional. Time to wait for the element in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return A boolean indicating whether or not the element became not visible.
     */
    public Boolean WaitForWebElementToDisappear(WebElement element, long customWaitTime) {
        return waitFor(ExpectedConditions.invisibilityOf(element), customWaitTime);
    }

    public Boolean WaitForWebElementToDisappear(WebElement element) {
        return WaitForWebElementToDisappear(element, STANDARD_WAIT_TIME);
    }

    /**
     * Waits for a WebElement to be selected on a page
     *
     * @param element        The element which should become selected
     * @param customWaitTime Optional. Time to wait for the element in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return A boolean indicating whether or not the element became selected
     */
    public Boolean WaitForWebElementToBeSelected(WebElement element, long customWaitTime) {
        return waitFor(element, ExpectedConditions.elementToBeSelected(element), customWaitTime);
    }

    public Boolean WaitForWebElementToBeSelected(WebElement element) {
        return WaitForWebElementToBeSelected(element, STANDARD_WAIT_TIME);
    }

    /**
     * Waits for a list of WebElements to not be visible on a page
     *
     * @param elements       The list of WebElements which should be waited for
     * @param customWaitTime Optional. Time to wait for the elements in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return A boolean indicating whether or not the elements became not visible
     */
    public Boolean WaitForWebElementListToDisappear(List<WebElement> elements, long customWaitTime) {
        // Note, if the element list is empty, we cannot scroll to the element.
        // If element list is not empty, scroll to first element in the list
        if (elements.size() > 0) {
            return waitFor(elements.get(0), ExpectedConditions.invisibilityOfAllElements(elements), customWaitTime);
        } else {
            return waitFor(ExpectedConditions.invisibilityOfAllElements(elements), customWaitTime);
        }
    }

    public Boolean WaitForWebElementListToDisappear(List<WebElement> elements) {
        return WaitForWebElementListToDisappear(elements, STANDARD_WAIT_TIME);
    }

    /**
     * Waits for an arbitrary attribute to equal a given value in an element located by a By locator
     *
     * @param locator        The locator of the element which is to be checked
     * @param attribute      The attribute of the element which should be examined (e.g. "@title" or "text()")
     * @param value          The value which should be found in the
     * @param customWaitTime Optional. Time to wait for the elements in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return A boolean indicating whether or not the given conditions were met.
     */
    public Boolean WaitForAttributeToEqual(By locator, String attribute, String value, long customWaitTime) {
        By refinedFinalLocator = getLocator.get(locator);
        return waitFor(refinedFinalLocator, ExpectedConditions.attributeToBe(refinedFinalLocator, attribute, value), customWaitTime);
    }

    public Boolean WaitForAttributeToEqual(By locator, String attribute, String value) {
        return WaitForAttributeToEqual(locator, attribute, value, STANDARD_WAIT_TIME);
    }

    /**
     * Waits for an arbitrary attribute to contain a given value in an element located by a By locator
     *
     * @param locator        The locator of the element which is to be checked
     * @param attribute      The attribute of the element which should be examined (e.g. "@title" or "text()")
     * @param value          The value which should be found in the
     * @param customWaitTime Optional. Time to wait for the elements in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return A boolean indicating whether or not the given conditions were met.
     */
    public Boolean WaitForAttributeToContain(By locator, String attribute, String value, long customWaitTime) {
        By refinedFinalLocator = getLocator.get(locator);
        return waitFor(refinedFinalLocator, ExpectedConditions.attributeContains(refinedFinalLocator, attribute, value), customWaitTime);
    }

    public Boolean WaitForAttributeToContain(By locator, String attribute, String value) {
        return WaitForAttributeToContain(locator, attribute, value, STANDARD_WAIT_TIME);
    }

    /**
     * Waits for the text of an element to contain a given String
     *
     * @param locator        The locator of the element which will be checked
     * @param text           The text which is expected to be found in the element
     * @param customWaitTime Optional. Time to wait for the elements in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return
     */
    public Boolean WaitForTextToContain(By locator, String text, long customWaitTime) {
        // Implemention Detail: ExpectedConditions.textToBePresentInElementLocated checks the .getText()
        //                      method of the found WebElement
        //                      ExpectedConditions.attributeToBe can only check the attribute text().
        //                      These are not guaranteed to be the same, so I cannot simply call
        //                      WaitForAttributeToEqual(locator, "text()", text).
        By refinedFinalLocator = getLocator.get(locator);
        return waitFor(refinedFinalLocator, ExpectedConditions.textToBePresentInElementLocated(refinedFinalLocator, text), customWaitTime);
    }

    public Boolean WaitForTextToContain(By locator, String text) {
        return WaitForTextToContain(locator, text, STANDARD_WAIT_TIME);
    }

    /**
     * Waits for an alert to appear on a page
     *
     * @param customWaitTime Optional. Time to wait for the alert in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     * @return The Alert which was found
     */
    public Alert WaitForAlert(long customWaitTime) {
        return waitFor(ExpectedConditions.alertIsPresent(), customWaitTime);
    }

    public Alert WaitForAlert() {
        return WaitForAlert(STANDARD_WAIT_TIME);
    }

    /**
     * Makes the current automation execution pause for a specified number of milliseconds
     * Note, this is generally a bad idea outside of debugging, and should be avoided in final
     * code if at all possible.
     *
     * @param threadSleep The number of milliseconds to pause the current execution
     */
    public void makeThreadSleep(int threadSleep) {
        try {
            //Console log  Making thread sleep for " + threadSleep + " ms");
            Thread.sleep(threadSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for a list of elements to be present on a page
     *
     * @param locator        The locator of the elements which should be waited for
     * @param customWaitTime Optional. Time to wait for the elements in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     */
    public void WaitForElementListToBePresent(By locator, long customWaitTime) {
        By refinedFinalLocator = getLocator.get(locator);
        waitFor(refinedFinalLocator, ExpectedConditions.presenceOfAllElementsLocatedBy(refinedFinalLocator), customWaitTime);
    }

    public void WaitForElementListToBePresent(By locator) {
        WaitForElementListToBePresent(locator, STANDARD_WAIT_TIME);
    }

    /**
     * Waits for a list of elements to be visible on a page
     *
     * @param locator        The locator of the elements which should be waited for
     * @param customWaitTime Optional. Time to wait for the elements in seconds.
     *                       Defaults to the standard wait time (5 or -DwaitTime parameter if present)
     */
    public void WaitForElementListToBeVisible(By locator, long customWaitTime) {
        By refinedFinalLocator = getLocator.get(locator);
        waitFor(refinedFinalLocator, ExpectedConditions.visibilityOfAllElementsLocatedBy(refinedFinalLocator), customWaitTime);
    }

    public long getWaitTime() {
        return System.currentTimeMillis() / 1000 + STANDARD_WAIT_TIME;
    }

    public long getCurrentTimeInSecs() {
        return System.currentTimeMillis() / 1000;
    }

    /***** Private methods with limited functionality *****/

    /**
     * @param expectedCondition The condition which should be waited for.
     * @param waitTime          How long to wait for the expected condition to become true
     * @param <V>               Defined by the ExpectedCondition which is passed in.
     *                          (Generally either WebElement or Boolean)
     * @return Whatever the return value of waiting for the ExpectedCondition is
     */
    private <V> V waitFor(ExpectedCondition<V> expectedCondition, long waitTime) {
        i.amPerforming().switchTo().acceptAlert();
        V value;
        if (wait == null) {
            wait = new WebDriverWait(InstanceRecording.getInstance(DeviceBucket.class).getDriver(), STANDARD_WAIT_TIME).pollingEvery(Duration.ofSeconds(STANDARD_POLLING_TIME)).ignoring(NoSuchElementException.class, NullPointerException.class);
        }
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Waiting for " + expectedCondition.toString());
        long preTime = System.currentTimeMillis();
        String result = "Failed";
        try {
            value = wait.withTimeout(Duration.ofSeconds(waitTime)).until(expectedCondition);
            result = "Succeeded";
        } catch (Exception e) {
            i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Wait for " + expectedCondition.toString() + " failed.");
            throw e;
        } finally {
            long postTime = System.currentTimeMillis();
            i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, String.format("%s after %.1f  seconds.", result, ((postTime - preTime) / 1_000.0)));
        }
        return value;
    }

    /**
     * Waits for a condition, and afterwards scrolls to the given locator
     */
    private <V> V waitFor(By locator, ExpectedCondition<V> expectedCondition, long waitTime) {
        i.amPerforming().switchTo().acceptAlert();
        V value = waitFor(expectedCondition, waitTime);
        i.amPerforming().switchTo().acceptAlert();
        InstanceRecording.getInstance(JavaScript.class).scrollElementToPageDetailCenter(locator);
        return value;
    }

    /**
     * Scrolls to the given element, and afterwards waits for a condition
     */
    private <V> V waitFor(WebElement element, ExpectedCondition<V> expectedCondition, long waitTime) {
        i.amPerforming().switchTo().acceptAlert();
        InstanceRecording.getInstance(JavaScript.class).scrollElementToPageDetailCenter(element);
        return waitFor(expectedCondition, waitTime);
    }
}