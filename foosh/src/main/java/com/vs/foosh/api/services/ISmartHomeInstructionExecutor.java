package com.vs.foosh.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.model.web.SmartHomePostResult;

@Service
public interface ISmartHomeInstructionExecutor {
    public  List<SmartHomePostResult> sendAndExecuteSmartHomeInstructions(List<SmartHomeInstruction> instructions);
}
