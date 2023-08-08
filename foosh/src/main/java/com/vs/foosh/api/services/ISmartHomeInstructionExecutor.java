package com.vs.foosh.api.services;

import java.util.List;

import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.model.web.SmartHomePostResult;

public interface ISmartHomeInstructionExecutor {
    public  List<SmartHomePostResult> sendAndExecuteSmartHomeInstructions(List<SmartHomeInstruction> instructions);
}
