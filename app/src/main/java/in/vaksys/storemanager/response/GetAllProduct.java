package in.vaksys.storemanager.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovoi3 on 8/12/2016.
 */
public class GetAllProduct {


    /**
     * error : false
     * message : Get Product successfully.
     * data : [{"id":"1","branch_id":"1","product_name":"efgg","price":"100"},{"id":"2","branch_id":"2","product_name":"asd","price":"300"}]
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    /**
     * id : 1
     * branch_id : 1
     * product_name : efgg
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
        @SerializedName("product_name")
        private String productName;
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

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
