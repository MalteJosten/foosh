package com.vs.foosh.api.services;

import java.util.ArrayList;
import java.util.List;

import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.model.web.SmartHomePostResult;

public interface ISmartHomeInstructionExecutor {
    public static List<SmartHomePostResult> sendAndExecuteSmartHomeInstructions(List<SmartHomeInstruction> instructions){
        return new ArrayList<>();
    }
}
