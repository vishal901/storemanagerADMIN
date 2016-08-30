package in.vaksys.storemanager.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovoi3 on 8/11/2016.
 */
public class GetBranchAdmin {


    /**
     * error : false
     * message : Get Branch successfully.
     * branch : [{"id":"1","name":"S g highway","address":"Pakwan Hotel"},{"id":"2","name":"mehsana","address":"mehsana address bdbbdbb"}]
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    /**
     * id : 1
     * name : S g highway
     * address : Pakwan Hotel
     */

    @SerializedName("branch")
    private List<BranchBean> branch;

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

    public List<BranchBean> getBranch() {
        return branch;
    }

    public void setBranch(List<BranchBean> branch) {
        this.branch = branch;
    }

    public static class BranchBean {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("address")
        private String address;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
