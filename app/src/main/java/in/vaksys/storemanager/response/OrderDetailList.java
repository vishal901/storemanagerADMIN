package in.vaksys.storemanager.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovoi3 on 8/17/2016.
 */
public class OrderDetailList {

    /**
     * error : false
     * message : Orders found
     * data : [{"item_ids":"4","item_price":"100","customer_name":"milansoni","product_name":"esfh","item_type":"COUPON","order_id":"1","customer_id":"2","branch_id":"1","order_date":"2016-08-17 10:45:32","payment_method":"CASH","total":"100"},{"item_ids":"1,2,3","item_price":"100,1000,1000","customer_name":"milansoni","product_name":"esfh,dfgg,dfgg","item_type":"PRODUCT","order_id":"1","customer_id":"2","branch_id":"1","order_date":"2016-08-17 10:45:32","payment_method":"CASH","total":"100"}]
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    /**
     * item_ids : 4
     * item_price : 100
     * customer_name : milansoni
     * product_name : esfh
     * item_type : COUPON
     * order_id : 1
     * customer_id : 2
     * branch_id : 1
     * order_date : 2016-08-17 10:45:32
     * payment_method : CASH
     * total : 100
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
        @SerializedName("item_ids")
        private String itemIds;
        @SerializedName("item_price")
        private String itemPrice;
        @SerializedName("customer_name")
        private String customerName;
        @SerializedName("product_name")
        private String productName;
        @SerializedName("item_type")
        private String itemType;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("customer_id")
        private String customerId;
        @SerializedName("branch_id")
        private String branchId;
        @SerializedName("order_date")
        private String orderDate;
        @SerializedName("payment_method")
        private String paymentMethod;
        @SerializedName("total")
        private String total;

        public String getItemIds() {
            return itemIds;
        }

        public void setItemIds(String itemIds) {
            this.itemIds = itemIds;
        }

        public String getItemPrice() {
            return itemPrice;
        }

        public void setItemPrice(String itemPrice) {
            this.itemPrice = itemPrice;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
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

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}
