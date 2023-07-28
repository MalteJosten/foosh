package com.vs.foosh.api.model.misc;

// TODO: @Override toString()
public class ReadSaveFileResult<T> {
    private T data;
    private boolean success;

    public ReadSaveFileResult() {}

    public void setData(T readResult) {
        this.data = readResult;
    }

    public T getData() {
        return this.data;
    }

    public void setSuccess(boolean wasSuccessful) {
        this.success = wasSuccessful;
    }

    public boolean getSuccess() {
        return this.success;
    }
}
