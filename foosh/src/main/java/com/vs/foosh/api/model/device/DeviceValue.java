package com.vs.foosh.api.model.device;

public class DeviceValue<T> {
    private T value;

    public DeviceValue() {
        
    }

    public DeviceValue(T value) {
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }
}
