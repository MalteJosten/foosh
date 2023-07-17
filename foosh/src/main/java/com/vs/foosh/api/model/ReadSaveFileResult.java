package com.vs.foosh.api.model;

import java.util.List;

public class ReadSaveFileResult {
    private List<AbstractDevice> data;
    private boolean success;

    public ReadSaveFileResult() {}

    public void setData(List<AbstractDevice> readResult) {
        this.data = readResult;
    }

    public List<AbstractDevice> getData() {
        return this.data;
    }

    public void setSuccess(boolean wasSuccessful) {
        this.success = wasSuccessful;
    }

    public boolean getSuccess() {
        return this.success;
    }
}
