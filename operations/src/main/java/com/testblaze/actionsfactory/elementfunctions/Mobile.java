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
import com.testblaze.mobile.Android;
import com.testblaze.mobile.IOS;
import com.testblaze.objects.InstanceRecording;
import com.testblaze.register.EnvironmentFetcher;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidBatteryInfo;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.ios.IOSTouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.Location;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

//Update report logs. Scroll strategy etc.

/**
 * @author nauman.shahid
 * @REI-Systems
 * @category Handles mobile based libraries including Appium , Android and IOS
 */
public final class Mobile {
    private Android android;
    private IOS ios;
    int pressX, bottomY, topY;
    private LocatorProcessing getLocator;
    private MobileScrolls mobileScrolls;
    private Tap tap;
    private MobileAccessories mobileAccessories;

    public Mobile() {
        this.getLocator = InstanceRecording.getInstance(LocatorProcessing.class);
    }

    /**
     * reach out android specific library and instance
     *
     * @return
     */
    public Android androidSpecials() {
        if ("android".equalsIgnoreCase(EnvironmentFetcher.getDevice()))
            this.android = new Android();
        return android;
    }

    /**
     * reach out IOS specific library and instance
     *
     * @return
     */
    public IOS iosSpecials() {
        if ("ios".equalsIgnoreCase(EnvironmentFetcher.getDevice()))
            this.ios = new IOS();
        return ios;
    }

    public MobileAccessories MobileAccessories() {
        if (mobileAccessories == null) mobileAccessories = new MobileAccessories();
        return mobileAccessories;
    }

    public Tap tap() {
        if (tap == null) tap = new Tap();
        return tap;
    }

    public MobileScrolls mobileScrolls() {
        if (mobileScrolls == null) mobileScrolls = new MobileScrolls();
        return mobileScrolls;
    }

    /**
     * Access mobile elements
     *
     * @param locatorWithType : xapth,name,id,accessiblityid,linktext,classname,css,tagname : String of locator
     * @return
     */
    private WebElement getMobileElement(String locatorWithType, Boolean processing) {
        WebElement fetchElement = null;
        String locatorType = locatorWithType.split(":")[0];
        String refinedLocator = getLocator.get(locatorWithType);
        // Add processing
        switch (locatorType.toUpperCase()) {
            case "XPATH":
                fetchElement = driver().findElementByXPath(refinedLocator);
                break;
            case "ID":
                fetchElement = driver().findElementById(refinedLocator);
                break;
            case "NAME":
                fetchElement = driver().findElementByName(refinedLocator);
                break;
            case "CLASSNAME":
                fetchElement = driver().findElementByClassName(refinedLocator);
                break;
            case "CSS":
                fetchElement = driver().findElementByCssSelector(refinedLocator);
                break;
            case "LINKTEXT":
                fetchElement = driver().findElementByLinkText(refinedLocator);
                break;
            case "ACCESSIBILITYID":
                fetchElement = driver().findElementByAccessibilityId(refinedLocator);
                break;
            case "TAGNAME":
                fetchElement = driver().findElementByTagName(refinedLocator);
                break;
        }
        if (fetchElement == null)
            throw new TestBlazeRunTimeException("Mobile element is not present or incorrect: " + refinedLocator);
        return fetchElement;
    }


    /**
     * Access mobile elements
     *
     * @param locatorWithType : xapth,name,id,accessiblityid,linktext,classname,css,tagname : String of locator
     * @return
     */
    private List<WebElement> getMobileElements(String locatorWithType, Boolean processing) {
        List<WebElement> fetchElement = null;
        String locatorType = locatorWithType.split(":")[0];
        String locator = locatorWithType.split(":")[1];
        String refinedLocator = getLocator.get(locator);
        // Add processing
        switch (locatorType.toUpperCase()) {
            case "XPATH":
                fetchElement = driver().findElementsByXPath(refinedLocator);
                break;
            case "ID":
                fetchElement = driver().findElementsById(refinedLocator);
                break;
            case "NAME":
                fetchElement = driver().findElementsByName(refinedLocator);
                break;
            case "CLASSNAME":
                fetchElement = driver().findElementsByClassName(refinedLocator);
                break;
            case "CSS":
                fetchElement = driver().findElementsByCssSelector(refinedLocator);
                break;
            case "LINKTEXT":
                fetchElement = driver().findElementsByLinkText(refinedLocator);
                break;
            case "ACCESSIBILITYID":
                fetchElement = driver().findElementsByAccessibilityId(refinedLocator);
                break;
            case "TAGNAME":
                fetchElement = driver().findElementsByTagName(refinedLocator);
                break;
        }
        return fetchElement;
    }

    @SuppressWarnings("unchecked") // If statement ensures unchecked cast is safe
    private AppiumDriver<WebElement> driver() {
        if (!"android".equalsIgnoreCase(EnvironmentFetcher.getDevice())
                && !"ios".equalsIgnoreCase(EnvironmentFetcher.getDevice())) {
            throw new TestBlazeRunTimeException("In order to use 'Mobile' library, System parameter 'device' must be set to 'android' or 'ios'.\n" +
                    "Parameter 'device' was found to be '" + EnvironmentFetcher.getDevice() + "'");
        }
        return (AppiumDriver<WebElement>) InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }

    private TouchAction<?> touch() {
        if ("android".equalsIgnoreCase(EnvironmentFetcher.getDevice())) {
            return new TouchAction<AndroidTouchAction>(driver());
        } else if ("ios".equalsIgnoreCase(EnvironmentFetcher.getDevice())) {
            return new TouchAction<IOSTouchAction>(driver());
        } else {
            return new TouchAction<>(driver());
        }
    }

    private MultiTouchAction multiTouch() {
        return new MultiTouchAction(driver());
    }

    public class MobileAccessories {
        /**
         * @return current context
         */
        public String getCurrentContext() {
            //Console log Getting Current context of app ");
            return driver().getContext();
        }

        /**
         * @return array of contexts
         */
        public ArrayList<String> getContextHandlers() {
            //Console log Getting list of contexts of app ");
            return new ArrayList<String>(driver().getContextHandles());
        }

        /**
         * switch to a context
         *
         * @param context
         */
        public void switchContext(String context) {
            driver().context(context);
        }

        /**
         * close the current app
         */
        public void closeApp() {
            //Console log closing app");
            driver().closeApp();
        }

        /**
         * Device time
         *
         * @return
         */
        public String getDeviceTime() {
            //Console log Getting Current Device time");
            return driver().getDeviceTime();
        }

        /**
         * Get keyboard on mobile screen
         */
        public void showKeyboard() {
            //Console log Hidding keyboard from screen ");
            driver().getKeyboard();
        }

        /**
         * @return screen orientation library
         */
        public ScreenOrientation getScreenOrientation() {
            //Console log Fetching screen orinetation ");
            return driver().getOrientation();
        }

        /**
         * gives screen page source
         */
        public void getScreenSource() {
            //Console log Reading page source code ");
            driver().getPageSource();
        }

        /**
         * Hide keyboard from screen
         */
        public void hideKeyboard() {
            //Console log Hidding keyboard ");
            driver().hideKeyboard();
        }

        /**
         * Install any app on mobile
         *
         * @param path
         */
        public void installApp(String path) {
            //Console log Installing app from " + path);
            driver().installApp(path);
        }

        /**
         * @return locations library
         */
        public Location getLocation() {
            //Console log Getting Current location ");
            return driver().location();
        }

        /**
         * boolean app is installed
         *
         * @param bundleId
         * @return true/false
         */
        public boolean isAppInstall(String bundleId) {
            return driver().isAppInstalled(bundleId);
        }

        /**
         * open the App
         */
        public void openApp() {
            //Console log Getting App launched ");
            driver().launchApp();
        }

        /**
         * run app in background
         *
         * @param seconds
         */
        public void runAppInBackGround(int seconds) {
            //Console log Enable app execution in background");
            driver().runAppInBackground(Duration.ofSeconds(seconds));
        }

        /**
         * change screen orientation to landscape
         */
        public void changeScreenOrientationToLandScape() {
            //Console log Chnaging screen resolution to Landscape");
            driver().rotate(ScreenOrientation.LANDSCAPE);
        }

        /**
         * change screen resolution to portrait
         */
        public void changeScreenOrientationToPortrait() {
            //Console log Chnaging screen resolution to Portrait");
            driver().rotate(ScreenOrientation.PORTRAIT);
        }

        public AndroidBatteryInfo androidBatteryInfo() {
            return androidSpecials().getBatteryInfo();
        }

        public void openAndroidNotifications() {
            androidSpecials().openNotifications();
        }
    }

    public class Tap {

        public void longPressElement(WebElement element, long seconds) {
            getScreenDimensions();
            touch().longPress(LongPressOptions.longPressOptions()
                    .withElement(ElementOption.element(element)))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(seconds)))
                    .release().perform();
        }


        /**
         * tap on specific element
         *
         * @param element
         */
        public void multipleTimesOnElement(WebElement element, int numberOfTaps) {
            try {
                //Console log Performing TAP on : " + element);
            } catch (Exception e) {
                //Console log Performing TAP on specific element");
            }
            touch().tap(TapOptions.tapOptions()
                    .withElement(ElementOption.element(element))
                    .withTapsCount(numberOfTaps))
                    .release().perform();
        }

        /**
         * tap at specific axis
         *
         * @param xAxis
         * @param yAxis
         */
        public void onAxis(int xAxis, int yAxis) {
            //Console log Performing TAP on axis");
            touch().tap(TapOptions.tapOptions()
                    .withPosition(PointOption.point(xAxis, yAxis)))
                    .release().perform();
        }

        public void nativeAndroidKey(AndroidKey androidKey) {
            androidSpecials().pressNativeKey(androidKey);
        }

        private void getScreenDimensions() {
            //Console log Fetching screen dimensions");
            pressX = driver().manage().window().getSize().width / 2;
            bottomY = driver().manage().window().getSize().height * 4 / 5;
            topY = driver().manage().window().getSize().height / 8;
        }

    }

    public class MobileScrolls {
        /**
         * Swipe from one element to other
         *
         * @param startElement
         * @param endElement
         */
        public void swipeByElements(WebElement startElement, WebElement endElement) {
            int startX = startElement.getLocation().getX() + (startElement.getSize().getWidth() / 2);
            int startY = startElement.getLocation().getY() + (startElement.getSize().getHeight() / 2);

            int endX = endElement.getLocation().getX() + (endElement.getSize().getWidth() / 2);
            int endY = endElement.getLocation().getY() + (endElement.getSize().getHeight() / 2);

            touch().press(PointOption.point(startX, startY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                    .moveTo(PointOption.point(endX, endY))
                    .release().perform();
        }

        /**
         * scroll on the screen
         *
         * @param startX
         * @param startY
         * @param endX
         * @param endY
         * @param seconds
         */
        public void screen(int startX, int startY, int endX, int endY, long seconds) {
            //Console log Performing scroll action");
            touch().press(PointOption.point(startX, startY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(seconds)))
                    .moveTo(PointOption.point(endX, endY))
                    .release().perform();
        }

        /**
         * scroll on the screen -IOS
         *
         * @param startX
         * @param startY
         * @param endX
         * @param endY
         * @param seconds
         */
        public void onIOS(int startX, int startY, int endX, int endY, long seconds) {
            //Console log Performing scroll action for IOS");
            touch().press(PointOption.point(startX, startY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(seconds)))
                    .moveTo(PointOption.point(endX - startX, endY - startY))
                    .release().perform();
        }

        /**
         * swipe down till end
         */
        public void swipeDown() {
            //Console log Performing swipte down");
            getScreenDimensions();
            touch().press(PointOption.point(pressX, bottomY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                    .moveTo(PointOption.point(pressX, topY))
                    .release().perform();
        }

        /**
         * scroll down to a specific element
         *
         * @param element
         */
        public void downToElement(WebElement element) {
            try {
                //Console log Performing scroll to specific element : " + element);
            } catch (Exception e) {
                //Console log Performing scroll to a specific element");
            }
            getScreenDimensions();
            touch().press(PointOption.point(pressX, bottomY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                    .moveTo(ElementOption.element(element))
                    .release().perform();
        }

        public void usingAndroidResourceID(String resourceId) {
            androidSpecials().scrollUsingResourceID(resourceId);
        }

        public void usingAndroidText(String text) {
            androidSpecials().scrollUsingText(text);
        }

        public void usingAndroidContentContains(String contentContains) {
            androidSpecials().specialScrollToContentContains(contentContains);
        }


        private void getScreenDimensions() {
            //Console log Fetching screen dimensions");
            pressX = driver().manage().window().getSize().width / 2;
            bottomY = driver().manage().window().getSize().height * 4 / 5;
            topY = driver().manage().window().getSize().height / 8;
        }

    }
}