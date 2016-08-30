package in.vaksys.storemanager.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovoi3 on 8/30/2016.
 */
public class UpdateCoupan {

    /**
     * error : false
     * message : Update coupon Successfully
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
