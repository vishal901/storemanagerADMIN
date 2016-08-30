package in.vaksys.storemanager.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovoi3 on 8/12/2016.
 */
public class GetCustomerList {

    /**
     * error : false
     * message : Get Customer successfully.
     * data : [{"id":"1","branch_id":"1","first_name":"milan","last_name":"soni","date_of_birth":"1990-12-08","gender":"MALE","address":"Ahmedabad","mobile":"7878225577","landline":"","email":"milan@vaksys.com","how_did_you_find_us":"GOOGLE","ailments":"asas","pain_picture":"https://google.com"}]
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    /**
     * id : 1
     * branch_id : 1
     * first_name : milan
     * last_name : soni
     * date_of_birth : 1990-12-08
     * gender : MALE
     * address : Ahmedabad
     * mobile : 7878225577
     * landline :
     * email : milan@vaksys.com
     * how_did_you_find_us : GOOGLE
     * ailments : asas
     * pain_picture : https://google.com
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
        @SerializedName("first_name")
        private String firstName;
        @SerializedName("last_name")
        private String lastName;
        @SerializedName("date_of_birth")
        private String dateOfBirth;
        @SerializedName("gender")
        private String gender;
        @SerializedName("address")
        private String address;
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("landline")
        private String landline;
        @SerializedName("email")
        private String email;
        @SerializedName("how_did_you_find_us")
        private String howDidYouFindUs;
        @SerializedName("ailments")
        private String ailments;
        @SerializedName("pain_picture")
        private String painPicture;

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

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
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

        public String getLandline() {
            return landline;
        }

        public void setLandline(String landline) {
            this.landline = landline;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getHowDidYouFindUs() {
            return howDidYouFindUs;
        }

        public void setHowDidYouFindUs(String howDidYouFindUs) {
            this.howDidYouFindUs = howDidYouFindUs;
        }

        public String getAilments() {
            return ailments;
        }

        public void setAilments(String ailments) {
            this.ailments = ailments;
        }

        public String getPainPicture() {
            return painPicture;
        }

        public void setPainPicture(String painPicture) {
            this.painPicture = painPicture;
        }
    }
}
