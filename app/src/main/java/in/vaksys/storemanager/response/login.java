package in.vaksys.storemanager.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovoi3 on 8/10/2016.
 */
public class login {


    /**
     * error : false
     * message : Logged in successfully.
     * data : {"id":"1","username":"admin","branch":"0","type":"0","csrf_key":"4b5555346e96549f01e8b4a65c1caa32"}
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    /**
     * id : 1
     * username : admin
     * branch : 0
     * type : 0
     * csrf_key : 4b5555346e96549f01e8b4a65c1caa32
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
        @SerializedName("id")
        private String id;
        @SerializedName("username")
        private String username;
        @SerializedName("branch")
        private String branch;
        @SerializedName("type")
        private String type;
        @SerializedName("csrf_key")
        private String csrfKey;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getBranch() {
            return branch;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCsrfKey() {
            return csrfKey;
        }

        public void setCsrfKey(String csrfKey) {
            this.csrfKey = csrfKey;
        }
    }
}
