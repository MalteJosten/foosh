package com.vs.foosh.api.services;

import com.vs.foosh.api.exceptions.smarthome.SmartHomeDeviceFetcherIsNullException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeInstructionExecutorIsNullException;

public class SmartHomeService {
    private static ISmartHomeDeviceFetcher smartHomeDeviceFetcher;
    private static ISmartHomeInstructionExecutor smartHomeInstructionExecutor;

    public static void registerSmartHomeDeviceFetcher(ISmartHomeDeviceFetcher deviceFetcher) {
        smartHomeDeviceFetcher = deviceFetcher;
    }

    public static void registerSmartHomeInstructionExecutor(ISmartHomeInstructionExecutor instructionExecutor) {
        smartHomeInstructionExecutor = instructionExecutor;
    }

    public static ISmartHomeDeviceFetcher getSmartHomeDeviceFetcher() {
        if (smartHomeDeviceFetcher == null) {
            throw new SmartHomeDeviceFetcherIsNullException();
        }

        return smartHomeDeviceFetcher;
    }

    public static ISmartHomeInstructionExecutor getSmartHomeInstructionExecutor() {
        if (smartHomeInstructionExecutor == null) {
            throw new SmartHomeInstructionExecutorIsNullException();
        }

        return smartHomeInstructionExecutor;
    }

}
