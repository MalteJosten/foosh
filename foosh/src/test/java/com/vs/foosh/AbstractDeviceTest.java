package com.vs.foosh;

import com.vs.foosh.api.model.device.AbstractDevice;

public class AbstractDeviceTest extends AbstractDevice{

    public AbstractDeviceTest(String name) {
        super(name);
    }

    @Override
    protected void setObjectFields() {
        throw new UnsupportedOperationException("Unimplemented method 'setObjectFields'");
    }
    
}
