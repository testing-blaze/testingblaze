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
package com.testblaze.actionsfactory.processing;

import com.testblaze.actionsfactory.api.IframeAnalyzer;
import com.testblaze.exception.TestBlazeRunTimeException;
import com.testblaze.objects.InstanceRecording;
import com.testblaze.register.i;
import com.testblaze.report.LogLevel;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.io.IOException;

import static com.testblaze.misclib.ConsoleFormatter.COLOR.CYAN;
import static com.testblaze.misclib.ConsoleFormatter.ICON.HORSE;
import static com.testblaze.misclib.ConsoleFormatter.ICON.THUMBS_UP;


public class ExecuteLocatorProcessing {
    IframeAnalyzer iframe;

    public ExecuteLocatorProcessing() {
        this.iframe = InstanceRecording.getInstance(IframeAnalyzer.class);
    }

    /**
     * Receive a By lcoator. Evaluate it for parameterization and fetches value from prop file if requried and return final locator
     *
     * @param locator
     * @return BY Locator
     * @author nauman.shahid
     */
    public <T> T getRefinedLocator(T locator) {
        T finalRefinedLocator = null;
        if (locator instanceof By) {
            finalRefinedLocator = (T) i.amPerforming().conversionOf().stringToBy(getLocatorParameters(locator.toString()));
            iframe.setUpLocator((By) finalRefinedLocator);
        } else if (locator instanceof String) {
            finalRefinedLocator = (T) getLocatorParameters(locator.toString().split(":")[1]);
        }
        i.amPerforming().updatingReportWith().newLine();
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, CYAN, HORSE, "Locator and Element Processing Starts");
        if (finalRefinedLocator == null)
            throw new TestBlazeRunTimeException("Locator processing failed: " + locator.toString());
        i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, THUMBS_UP, "Locator = " + finalRefinedLocator);
        return finalRefinedLocator;
    }


    /**
     * 1- Name properties file in small letters only
     * 2- Parameter naame should be in small . If its longer user underscore
     * Example: participants_table_id=GrantorSiteVisitContacts
     * 3- Syntax in gherkin:
     * ---siteVisits:-:participants_table_id---
     * 4- if paramter is needed to be added from saved value in properties file then: syntax in gherkin
     * ---SavedValue:-:Key---
     *
     * @param parameter
     * @return string element
     */
    private String handleLocatorParameter(String parameter) {
        if (parameter == null || !parameter.contains(":-:")) {
            return parameter;
        }
        String finalParameter = parameter;
        String[] parameters = parameter.split(":-:");
        if (parameters[0].equalsIgnoreCase("SavedValue")) {
            try {
                finalParameter = i.amPerforming().propertiesFileOperationsTo().getValue(parameters[1]);
            } catch (Exception e) {
                i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Key :" + parameters[0] + " not found");
                Assert.fail("Scenario Failed: Key was not found.");
            }
        } else {
            try {
                finalParameter = i.amPerforming().propertiesFileOperationsTo().ReadPropertyFile(parameters[0] + ".properties", parameters[1]);
            } catch (IOException e) {
                i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_ERROR, "Properties file name :" + parameters[0] + " not found");
                Assert.fail("Scenario Failed: properties file was not found.");
            }
        }
        return finalParameter;
    }

    /**
     * get strinng lcoator and split is based on properties parameter if any
     *
     * @param locator
     * @return
     */
    private String getLocatorParameters(String locator) {
        if (!locator.contains("---")) {
            return locator;
        }

        String parameterizedLocator = locator;
        boolean locatorTypeIsXpath = locator.contains("xpath:");
        for (String i : locator.split("---")) {
            if (i.contains(":-:")) {
                parameterizedLocator = parameterizedLocator.replace("---" + i + "---", handleLocatorParameter(i));
            }
        }
        return parameterizedLocator;
    }

}