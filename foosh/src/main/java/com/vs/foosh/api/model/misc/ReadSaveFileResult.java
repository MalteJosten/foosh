package com.vs.foosh.api.model.misc;

/**
 * A container containing data which was retrieved by reading a save file.
 *  
 */

@SuppressWarnings("rawtypes")
public class ReadSaveFileResult<T extends IThingList> {
    /**
     * An {@link IThingList} which was retrieved from reading in a save file.
     */
    private T data;

    /**
     * An indicator if the process of reading in a save file was successful.
     */
    private boolean success;

    /**
     * Create an empty {@code ReadSaveFileResult}.
     */
    public ReadSaveFileResult() {}


    /**
     * Set the field {@code data}.
     * 
     * @param readResult the read result of type {@link IThingList} to set the field {@code data} to.
     */
    public void setData(T readResult) {
        this.data = readResult;
    }

    /**
     * Return the {@code data}.
     * 
     * @return {@code data}
     */
    public T getData() {
        return this.data;
    }

    /**
     * Set the field {@code success}.
     * 
     * @param wasSuccessful the boolean value indicating the success of the reading process
     */
    public void setSuccess(boolean wasSuccessful) {
        this.success = wasSuccessful;
    }

    /**
     * Return the field {@code success}.
     * 
     * @return {@code success}
     */
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
