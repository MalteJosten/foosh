package com.vs.foosh.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vs.foosh.api.model.web.SmartHomeInstruction;
import com.vs.foosh.api.model.web.SmartHomePostResult;

/**
 * An interface and {@link Service} that should provide the functionality of executing a list of {@link SmartHomeInstruction}s by forwarding them to a smart home API.
 */
@Service
public interface ISmartHomeInstructionExecutor {
    /**
     * A {@link List} of {@link SmartHomeInstruction}s should be reformatted (if necessary) and forwarded to a smart home API.
     * The results of executing the aforementioned instructions shall be returned in form of a {@link List} with elements of type {@link SmartHomePostResult}.
     * 
     * @param instructions the {@link List} with elements of type {@link SmartHomeInstruction}
     * @return a {@link List} with elements of type {@link SmartHomePostResult}
     */
    public List<SmartHomePostResult> sendAndExecuteSmartHomeInstructions(List<SmartHomeInstruction> instructions);
}
