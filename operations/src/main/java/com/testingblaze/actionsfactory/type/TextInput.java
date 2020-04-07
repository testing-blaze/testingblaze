/*
 * Copyright 2020
 *
 * This file is part of  Testing Blaze Automation Framework [BDD] .
 *
 * Testing Blaze Automation Framework is licensed under the Apache License, Version
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
package com.testingblaze.actionsfactory.type;


import com.testingblaze.actionsfactory.abstracts.Action;
import com.testingblaze.actionsfactory.abstracts.Element;
import com.testingblaze.actionsfactory.elementfunctions.JavaScript;
import com.testingblaze.actionsfactory.elementfunctions.Mobile;
import com.testingblaze.objects.InstanceRecording;

public class TextInput {
    private JavaScript javaScript;
    private Element elementApi;
    private Action executeAction;
    private Mobile mobile;

    public TextInput() {
        elementApi = InstanceRecording.getInstance(Element.class);
        executeAction = InstanceRecording.getInstance(Action.class);
        javaScript = InstanceRecording.getInstance(JavaScript.class);
        mobile = InstanceRecording.getInstance(Mobile.class);
    }

    /**
     * input on webPage , mobilePage , AngularPage
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @author nauman.shahid
     */
    public <T> void in(T locator,String input) {
        in(locator,input, true);
    }

    /**
     * input on webPage , mobilePage , AngularPage
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @author nauman.shahid
     */
    public <T> void in(T locator,String input, Boolean processing) {
        executeAction.doIt(elementApi.locator(locator,processing),input);
    }

    /**
     * clear on webPage , mobilePage , AngularPage
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @author nauman.shahid
     */
    public <T> void toClear(T locator) {
        toClear(locator, true);
    }

    /**
     * clear on webPage , mobilePage , AngularPage
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @author nauman.shahid
     */
    public <T> void toClear(T locator, Boolean processing) {
        executeAction.doIt(elementApi.locator(locator,processing),"--clear--");
    }

    /**
     * Enter text with Java Script
     *
     * @param locator
     * @param input
     */
    public <T> void withJavaScript(T locator, String input) {
        withJavaScript(locator, input, true);
    }

    /**
     * Enter text with Java Script
     *
     * @param locator
     * @param input
     */
    public <T> void withJavaScript(T locator, String input, Boolean processing) {
        javaScript.InputJSByWebElement(elementApi.locator(locator,processing), input);
    }

    /**
     * Enter text on android specific locator strategy
     * @param locator: Android
     * @
     */
    public void onAndroidViewTag(String locator,String input) { executeAction.doIt(mobile.usingAndroidSpecials().toGetElementByViewTag(locator),input);
    }

    /**
     * Enter text on android specific locator strategy
     * @param imageName: Android
     * @
     */
    public void onAndroidImage(String imageName,String input) {
        executeAction.doIt(mobile.usingAndroidSpecials().toGetElementByImage(imageName),input);
    }

    /**
     * clicks on IOS specific locator strategy
     * @param locator: Mobile.
     * @
     */
    public void onIosApp(String locator) {
        // to be added
    }

}
