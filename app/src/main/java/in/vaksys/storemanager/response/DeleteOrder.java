package in.vaksys.storemanager.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovoi3 on 9/1/2016.
 */
public class DeleteOrder {

    /**
     * error : false
     * message : Delete Order Successfully
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
