package com.vs.foosh.helper;

import com.vs.foosh.api.model.device.AbstractDevice;

public class AbstractDeviceMock extends AbstractDevice{

    public AbstractDeviceMock(String name) {
        super(name);
    }

    @Override
    protected void setObjectFields() {
        throw new UnsupportedOperationException("Unimplemented method 'setObjectFields'");
    }
    
}
