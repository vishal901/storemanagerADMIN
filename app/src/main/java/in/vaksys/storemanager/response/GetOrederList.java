package in.vaksys.storemanager.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovoi3 on 8/16/2016.
 */
public class GetOrederList {


    /**
     * error : false
     * message : Orders found
     * data : [{"order_id":"1","branch_id":"2","order_date":"0000-00-00 00:00:00","order_total":"2301","customer_name":"xay","product_count":"0","coupon_count":"0"},{"order_id":"2","branch_id":"2","order_date":"0000-00-00 00:00:00","order_total":"2301","customer_name":"xay","product_count":"2","coupon_count":"1"},{"order_id":"3","branch_id":"2","order_date":"0000-00-00 00:00:00","order_total":"2301","customer_name":"xay","product_count":"2","coupon_count":"1"},{"order_id":"4","branch_id":"2","order_date":"0000-00-00 00:00:00","order_total":"500","customer_name":"xay","product_count":"2","coupon_count":"1"},{"order_id":"5","branch_id":"2","order_date":"0000-00-00 00:00:00","order_total":"2301","customer_name":"xay","product_count":"2","coupon_count":"1"}]
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    /**
     * order_id : 1
     * branch_id : 2
     * order_date : 0000-00-00 00:00:00
     * order_total : 2301
     * customer_name : xay
     * product_count : 0
     * coupon_count : 0
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
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("branch_id")
        private String branchId;
        @SerializedName("order_date")
        private String orderDate;
        @SerializedName("order_total")
        private String orderTotal;
        @SerializedName("customer_name")
        private String customerName;
        @SerializedName("product_count")
        private String productCount;
        @SerializedName("coupon_count")
        private String couponCount;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getOrderTotal() {
            return orderTotal;
        }

        public void setOrderTotal(String orderTotal) {
            this.orderTotal = orderTotal;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getProductCount() {
            return productCount;
        }

        public void setProductCount(String productCount) {
            this.productCount = productCount;
        }

        public String getCouponCount() {
            return couponCount;
        }

        public void setCouponCount(String couponCount) {
            this.couponCount = couponCount;
        }
    }
}
