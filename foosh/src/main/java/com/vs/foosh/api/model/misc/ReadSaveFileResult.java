package com.vs.foosh.api.model.misc;

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<< ReadSaveFileResult >>\n");
        builder.append("Data:    " + data + "\n");
        builder.append("Success: " + success);

        return builder.toString();
    }
}
