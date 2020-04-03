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
package com.testblaze.actionsfactory.type;

import com.testblaze.actionsfactory.abstracts.Action;
import com.testblaze.actionsfactory.abstracts.Element;
import com.testblaze.actionsfactory.elementfunctions.JavaScript;
import com.testblaze.actionsfactory.elementfunctions.Mobile;
import com.testblaze.actionsfactory.elementfunctions.MouseActions;
import com.testblaze.objects.InstanceRecording;

public class Click {
    private MouseClicks mouseClicks;
    private JavaScript javaScript;
    private Element elementApi;
    private Action executeAction;
    private Mobile mobile;

    public Click() {
        elementApi = InstanceRecording.getInstance(Element.class);
        executeAction = InstanceRecording.getInstance(Action.class);
        javaScript = InstanceRecording.getInstance(JavaScript.class);
        mobile = InstanceRecording.getInstance(Mobile.class);
    }

    /**
     * click on webPage , mobilePage , AngularPage
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @author nauman.shahid
     */
    public <T> void on(T locator) {
        on(locator, true);
    }

    /**
     * click on webPage , mobilePage , AngularPage
     * @param locator -> Mobile , Ng , By :
     *                Mobile.
     *                Angular.
     *                By.
     * @author nauman.shahid
     */
    public <T> void on(T locator, Boolean processing) {
        executeAction.doIt(elementApi.locator(locator,processing));
    }


    /**
     * clicks on element using java script
     * @param locator
     * @author nauman.shahid
     */
    public <T> void withJavaScript(T locator) {
        withJavaScript(locator, true);
    }

    /**
     * clicks on element using java script
     * @param locator
     * @author nauman.shahid
     */
    public <T> void withJavaScript(T locator, Boolean processing) {
        javaScript.clickByJSWebElement(elementApi.locator(locator, processing));
    }

    /**
     * perform click using mouse actions
     *
     * @return mouse clicks
     * @author nauman.shahid
     */
    public MouseClicks withMouse() {
        if(mouseClicks == null) {
            mouseClicks = new MouseClicks();
        }
        return mouseClicks;
    }

    /**
     * clicks on android specific locator strategy
     * @param locator: Android
     * @
     */
    public void onAndroidViewTag(String locator) { executeAction.doIt(mobile.androidSpecials().getElementByViewTag(locator));
    }

    /**
     * clicks on android specific locator strategy
     * @param imageName: Android
     * @
     */
    public void onAndroidImage(String imageName) {
        executeAction.doIt(mobile.androidSpecials().getElementByImage(imageName));
    }

    /**
     * clicks on IOS specific locator strategy
     * @param locator: Mobile.
     * @
     */
    public void onIosApp(String locator) {
        // to be added
    }

    public Mobile.Tap withTapOnScreen(){
        return mobile.tap();
    }

    /**
     * Inner class for web based mouse actions handling
     *
     * @author nauman.shahid
     */
    public class MouseClicks {
        private MouseActions mouseActions;

        protected MouseClicks() {
            this.mouseActions = InstanceRecording.getInstance(MouseActions.class);
        }

        /**
         * clicks on desired location based on dimension
         *
         * @author nauman.shahid
         */
        public void at(int x, int y) {
            mouseActions.mouseClick(x, y);
        }

        /**
         * clicks on desired location on web page using By
         *
         * @author nauman.shahid
         */
        public <T> void on(T locator) {
            on(locator, true);
        }

        /**
         * clicks on desired location on web page using By
         *
         * @author nauman.shahid
         */
        public <T>  void on(T locator, Boolean processing) {
            mouseActions.mouseClick(elementApi.locator(locator, processing));
        }

        /**
         * click and hold on desired location on web paget
         *
         * @param holdTimeSeconds
         * @author nauman.shahid
         */
        public <T>  void onAndHold(T locator, int holdTimeSeconds) {
            onAndHold(locator, holdTimeSeconds, true);
        }

        /**
         * click and hold on desired location on web paget
         *
         * @param holdTimeSeconds
         * @author nauman.shahid
         */
        public <T>  void onAndHold(T locator, int holdTimeSeconds, Boolean processing) {
            mouseActions.mouseClickAndHold(elementApi.locator(locator, processing), holdTimeSeconds);
        }

        /**
         * Double clicks on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void doubleClickOn(T locator) {
            doubleClickOn(locator, true);
        }

        /**
         * Double clicks on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void doubleClickOn(T locator, Boolean processing) {
            mouseActions.mouseDoubleClick(elementApi.locator(locator, processing));
        }

        /**
         * Drag and drop on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void dragAndDrop(T source, T target) {
            dragAndDrop(source,target, true);
        }


        /**
         * Drag and drop on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void dragAndDrop(T source, T target, Boolean processing) {
            mouseActions.mouseDragAndDrop(elementApi.locator(source, processing), elementApi.locator(target, processing));
        }

        /**
         * Mouse right click on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void rightClickOn(T context) {
            rightClickOn(context, true);
        }


        /**
         * Mouse right click on desired location on web page
         *
         * @author nauman.shahid
         */
        public <T> void rightClickOn(T context, Boolean processing) {
            mouseActions.mouseRightClick(elementApi.locator(context, processing));
        }
    }

}