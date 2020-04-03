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
package com.testblaze.register;


import com.testblaze.controller.CoreHandlers;
import com.testblaze.controller.DeviceBucket;
import com.testblaze.controller.ReportingLogsPlugin;
import com.testblaze.controller.ScenarioController;
import com.testblaze.controller.TestSetupController;
import com.testblaze.exception.TestBlazeExceptionWithoutStackTrace;
import com.testblaze.objects.InstanceRecording;
import com.testblaze.report.LogLevel;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;

public final class TestBlazeRegistration {
    private TestSetupController registerSetup;

    public TestBlazeRegistration(DeviceBucket device, CoreHandlers coreLib, TestSetupController registerSetup) {
        this.registerSetup = registerSetup;
        InstanceRecording.recordInstance(CoreHandlers.class, coreLib);
        InstanceRecording.recordInstance(DeviceBucket.class, device);
    }

    @Before(order = 0)
    public void registerSetup(Scenario scenario) throws IOException, AWTException {
        registerSetup.initializer(scenario);
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
    }

    @After(order = 0)
    public void unRegisterSetup() throws IOException {

        registerSetup.theEnd();
    }

    @After(order = 1)
    public void verifyingScenarioSoftAssertions() throws TestBlazeExceptionWithoutStackTrace {
        if (ScenarioController.getScenario().isFailed()) {
            i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "The scenario is already failed.  Skipping check of soft assertions.");
        } else if (ReportingLogsPlugin.getErrorsFromScenario().size() > 0) {
            throw new TestBlazeExceptionWithoutStackTrace("The following soft assertions failed in the scenario:\n"
                    + String.join("\n", ReportingLogsPlugin.getErrorsFromScenario()));
        } else {
            i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "No soft assertions failed in the scenario.");
        }
    }

    @After(order = 100)
    public void triggerMandatoryClosureJobs() throws IOException {
        Thread performClosureActivities = new Thread(() -> {
            /*
            if ("tfs".equalsIgnoreCase(System.getProperty("postTestResults"))) {
                tfs.addTestResultToTfs();
            } else if ("jira".equalsIgnoreCase(System.getProperty("postTestResults"))) {
                //To be Implemented
            }
        }*/
            System.out.println("jvm shutdown hook");});
        Runtime.getRuntime().addShutdownHook(performClosureActivities);
    }

}