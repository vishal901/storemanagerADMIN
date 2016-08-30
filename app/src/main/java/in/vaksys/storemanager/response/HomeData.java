package in.vaksys.storemanager.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovoi3 on 8/24/2016.
 */
public class HomeData {


    /**
     * error : false
     * message : Data Found.
     * data : {"no_sells":"4","no_product":"2","no_coupon":"0"}
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    /**
     * no_sells : 4
     * no_product : 2
     * no_coupon : 0
     */

    @SerializedName("data")
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @SerializedName("no_sells")
        private String noSells;
        @SerializedName("no_product")
        private String noProduct;
        @SerializedName("no_coupon")
        private String noCoupon;

        public String getNoSells() {
            return noSells;
        }

        public void setNoSells(String noSells) {
            this.noSells = noSells;
        }

        public String getNoProduct() {
            return noProduct;
        }

        public void setNoProduct(String noProduct) {
            this.noProduct = noProduct;
        }

        public String getNoCoupon() {
            return noCoupon;
        }

        public void setNoCoupon(String noCoupon) {
            this.noCoupon = noCoupon;
        }
    }
}
