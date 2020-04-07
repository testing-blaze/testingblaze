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
package com.testingblaze.controller;

import com.testingblaze.objects.InstanceRecording;
import com.testingblaze.register.I;
import com.testingblaze.report.LogLevel;
import io.cucumber.java.Scenario;

import java.io.File;
import java.util.Objects;

import static com.testingblaze.misclib.ConsoleFormatter.COLOR.CYAN;
import static com.testingblaze.misclib.ConsoleFormatter.COLOR.GREEN;
import static com.testingblaze.misclib.ConsoleFormatter.ICON.CLOCK;
import static com.testingblaze.misclib.ConsoleFormatter.ICON.TICK;

public class ScenarioController {
    private static Scenario scenario;
    private static int totalNumberOfScenarios = 0;
    private static int executedScenarioCount = 0;
    private static int failedScenarioCount = 0;

    static void setScenario(Scenario scenario) {
        ScenarioController.scenario = scenario;
        if (totalNumberOfScenarios == 0) {
            setTotalNumberOfScenarios();
        }

        executedScenarioCount++;
    }

    public static Scenario getScenario() {
        return scenario;
    }

    public static int getExecutedScenarioCount() {
        return executedScenarioCount;
    }

    static void printInitialLogs() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("************************** Framework Logs **************************************");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("[Info:ETCAF] Scenario Number in Current Execution: " + getExecutedScenarioCount() + " / " + totalNumberOfScenarios);
        System.out.println("[Info:ETCAF] Scenario Name: " + getScenario().getName());
        System.out.println("[Info:ETCAF] Scenario Tags: " + getScenario().getSourceTagNames());
        System.out.println("[Info:ETCAF] Controlling DI instance # " + InstanceRecording.getInstance(DeviceBucket.class).hashCode());
        System.out.println("[Info:ETCAF] Controlling WebDriver instance # " + InstanceRecording.getInstance(DeviceBucket.class).getDriver().hashCode());
    }

    static void printFinalLogs() {
        if (scenario.isFailed()) {
            failedScenarioCount++;
        }

        I.amPerforming().updatingOfReportWith().newLine();
        I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_INFO, GREEN, CLOCK, "Scenario Result Analysis:");
        I.amPerforming().updatingOfReportWith().write(LogLevel.EMPTY_LABEL, CYAN, TICK, "                          Successful / Failed / Total scenarios: "
                + (executedScenarioCount - failedScenarioCount) + " / "  + failedScenarioCount + " / " + totalNumberOfScenarios);
        I.amPerforming().updatingOfReportWith().newLine();

        if ("false".equalsIgnoreCase(System.getProperty("printConsoleLogs"))) {
            System.out.println();
            System.out.println("Suite Execution Summary:");
            System.out.println("Successful / Failed / Total scenarios: "
                    + (executedScenarioCount - failedScenarioCount) + " / "  + failedScenarioCount + " / " + totalNumberOfScenarios);
            System.out.println();
        }
    }

    /**
     * Gets the total number of scenarios that will be executed in this suite.
     * It does so by counting the number of files in the /target/parallet/features folder, which are generated by Cucable.
     * If, in the future, we no longer use Cucable this solution will no longer work.
     * In that case, the total number of scenarios <i>should</i> just display as "0" until it is fixed.
     */
    static void setTotalNumberOfScenarios() {
        try {
            totalNumberOfScenarios = Objects.requireNonNull((new File(System.getProperty("user.dir") + "/target/parallel/features")).list()).length;
        } catch (NullPointerException e) {
            I.amPerforming().updatingOfReportWith().write(LogLevel.TEST_BLAZE_ERROR,
                    "The directory '" + System.getProperty("user.dir") + "/target/parallel/features' was not found, or is not a directory."
            );
        }
    }
}
