package com.vs.foosh.api.services;

import org.springframework.stereotype.Service;

import com.vs.foosh.api.exceptions.smarthome.SmartHomeDeviceFetcherIsNullException;
import com.vs.foosh.api.exceptions.smarthome.SmartHomeInstructionExecutorIsNullException;

/**
 * A {@link Service} that manages an {@link ISmartHomeDeviceFetcher} and an {@link ISmartHomeInstructionExecutor}.
 */
@Service
public class SmartHomeService {
    /**
     * The {@link ISmartHomeDeviceFetcher} used for this application.
     */
    private static ISmartHomeDeviceFetcher smartHomeDeviceFetcher;

    /**
     * The {@link ISmartHomeInstructionExecutor} used for this application.
     */
    private static ISmartHomeInstructionExecutor smartHomeInstructionExecutor;

    /**
     * Register an {@link ISmartHomeDeviceFetcher}.
     * 
     * @param deviceFetcher the {@link ISmartHomeDeviceFetcher} to set the field {@code smartHomeDeviceFetcher} to
     */
    public static void registerSmartHomeDeviceFetcher(ISmartHomeDeviceFetcher deviceFetcher) {
        smartHomeDeviceFetcher = deviceFetcher;
    }

    /**
     * Register an {@link ISmartHomeInstructionExecutor}.
     * 
     * @param deviceFetcher the {@link ISmartHomeInstructionExecutor} to set the field {@code smartHomeInstructionExecutor} to
     */
    public static void registerSmartHomeInstructionExecutor(ISmartHomeInstructionExecutor instructionExecutor) {
        smartHomeInstructionExecutor = instructionExecutor;
    }

    /**
     * Return the instance of {@link ISmartHomeDeviceFetcher} (if registered).
     * If there is no registered device fetcher, a {@link SmartHomeDeviceFetcherIsNullException} is thrown.
     * 
     * @return the field {@code smartHomeDeviceFetcher}
     */
    public static ISmartHomeDeviceFetcher getSmartHomeDeviceFetcher() {
        if (smartHomeDeviceFetcher == null) {
            throw new SmartHomeDeviceFetcherIsNullException();
        }

        return smartHomeDeviceFetcher;
    }

    /**
     * Return the instance of {@link ISmartHomeInstructionExecutor} (if registered).
     * If there is no registered instruction executor, a {@link SmartHomeInstructionExecutorIsNullException} is thrown.
     * 
     * @return the field {@code smartHomeInstructionExecutor}
     */
    public static ISmartHomeInstructionExecutor getSmartHomeInstructionExecutor() {
        if (smartHomeInstructionExecutor == null) {
            throw new SmartHomeInstructionExecutorIsNullException();
        }

        return smartHomeInstructionExecutor;
    }

}
