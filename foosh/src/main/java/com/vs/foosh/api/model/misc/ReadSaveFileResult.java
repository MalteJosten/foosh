package com.vs.foosh.api.model.misc;

import java.util.List;

public class ReadSaveFileResult<T> {
    private List<T> data;
    private boolean success;

    public ReadSaveFileResult() {}

    public void setData(List<T> readResult) {
        this.data = readResult;
    }

    public List<T> getData() {
        return this.data;
    }

    public void setSuccess(boolean wasSuccessful) {
        this.success = wasSuccessful;
    }

    public boolean getSuccess() {
        return this.success;
    }
}
