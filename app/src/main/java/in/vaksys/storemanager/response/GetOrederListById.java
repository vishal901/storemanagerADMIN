package in.vaksys.storemanager.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovoi3 on 8/16/2016.
 */
public class GetOrederListById {


    /**
     * error : false
     * message : Orders found
     * data : {"order":[{"id":"2","branch_id":"1","customer_id":"2","order_date":"2016-08-17 16:38:44","payment_method":"CASH","total":"100","customer_name":"milansoni","address":"Ahmedabad","mobile":"7878225577"}],"products":[{"id":"5","order_id":"2","item_id":"1","type":"PRODUCT","price":"100","product_name":"esfh"},{"id":"6","order_id":"2","item_id":"2","type":"PRODUCT","price":"1000","product_name":"dfgg"},{"id":"7","order_id":"2","item_id":"3","type":"PRODUCT","price":"1000","product_name":"dfgg"}],"coupon":[{"id":"8","order_id":"2","item_id":"1","type":"COUPON","price":"100","coupon_name":"lucky"}]}
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
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
        /**
         * id : 2
         * branch_id : 1
         * customer_id : 2
         * order_date : 2016-08-17 16:38:44
         * payment_method : CASH
         * total : 100
         * customer_name : milansoni
         * address : Ahmedabad
         * mobile : 7878225577
         */

        @SerializedName("order")
        private List<OrderBean> order;
        /**
         * id : 5
         * order_id : 2
         * item_id : 1
         * type : PRODUCT
         * price : 100
         * product_name : esfh
         */

        @SerializedName("products")
        private List<ProductsBean> products;
        /**
         * id : 8
         * order_id : 2
         * item_id : 1
         * type : COUPON
         * price : 100
         * coupon_name : lucky
         */

        @SerializedName("coupon")
        private List<CouponBean> coupon;

        public List<OrderBean> getOrder() {
            return order;
        }

        public void setOrder(List<OrderBean> order) {
            this.order = order;
        }

        public List<ProductsBean> getProducts() {
            return products;
        }

        public void setProducts(List<ProductsBean> products) {
            this.products = products;
        }

        public List<CouponBean> getCoupon() {
            return coupon;
        }

        public void setCoupon(List<CouponBean> coupon) {
            this.coupon = coupon;
        }

        public static class OrderBean {
            @SerializedName("id")
            private String id;
            @SerializedName("branch_id")
            private String branchId;
            @SerializedName("customer_id")
            private String customerId;
            @SerializedName("order_date")
            private String orderDate;
            @SerializedName("payment_method")
            private String paymentMethod;
            @SerializedName("total")
            private String total;
            @SerializedName("customer_name")
            private String customerName;
            @SerializedName("address")
            private String address;
            @SerializedName("mobile")
            private String mobile;

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

            public String getCustomerId() {
                return customerId;
            }

            public void setCustomerId(String customerId) {
                this.customerId = customerId;
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

            public String getCustomerName() {
                return customerName;
            }

            public void setCustomerName(String customerName) {
                this.customerName = customerName;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }
        }

        public static class ProductsBean {
            @SerializedName("id")
            private String id;
            @SerializedName("order_id")
            private String orderId;
            @SerializedName("item_id")
            private String itemId;
            @SerializedName("type")
            private String type;
            @SerializedName("price")
            private String price;
            @SerializedName("product_name")
            private String productName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getItemId() {
                return itemId;
            }

            public void setItemId(String itemId) {
                this.itemId = itemId;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }
        }

        public static class CouponBean {
            @SerializedName("id")
            private String id;
            @SerializedName("order_id")
            private String orderId;
            @SerializedName("item_id")
            private String itemId;
            @SerializedName("type")
            private String type;
            @SerializedName("price")
            private String price;
            @SerializedName("coupon_name")
            private String couponName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getItemId() {
                return itemId;
            }

            public void setItemId(String itemId) {
                this.itemId = itemId;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getCouponName() {
                return couponName;
            }

            public void setCouponName(String couponName) {
                this.couponName = couponName;
            }
        }
    }
}
