package in.vaksys.storemanager.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovoi3 on 8/12/2016.
 */
public class GetCoupanList {


    /**
     * error : false
     * message : Get Coupon successfully.
     * data : [{"id":"1","branch_id":"1","coupon_name":"lucky","price":"100"},{"id":"2","branch_id":"1","coupon_name":"ads","price":"2000"}]
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    /**
     * id : 1
     * branch_id : 1
     * coupon_name : lucky
     * price : 100
     */

    @SerializedName("data")
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        @SerializedName("id")
        private String id;
        @SerializedName("branch_id")
        private String branchId;
        @SerializedName("coupon_name")
        private String couponName;
        @SerializedName("price")
        private String price;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getCouponName() {
            return couponName;
        }

        public void setCouponName(String couponName) {
            this.couponName = couponName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
