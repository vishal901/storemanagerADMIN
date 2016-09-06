package in.vaksys.storemanager.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lenovoi3 on 8/12/2016.
 */
public class GetCustomerList {


    /**
     * error : false
     * message : Get Customers successfully.
     * data : [{"id":"21","branch_id":"6","first_name":"vishal","last_name":"patel","date_of_birth":"0000-00-00","gender":"MALE","address":"address 1","address1":"address2","city":"Mehsana","state":"Gujarat","zip":"666666","mobile":"123456789012","landline":"123654789012","email":"vishal@gmail.com","how_did_you_find_us":"Other","ailments":"","pain_picture":null,"find_us_reason":"other data"},{"id":"22","branch_id":"6","first_name":"ankit","last_name":"patel","date_of_birth":"0000-00-00","gender":"MALE","address":"balol","address1":"balol","city":"Ahmedabad","state":"Gujarat","zip":"666666","mobile":"123456789089","landline":"","email":"visit@hkak.sjj","how_did_you_find_us":"Google","ailments":"","pain_picture":null,"find_us_reason":""}]
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    /**
     * id : 21
     * branch_id : 6
     * first_name : vishal
     * last_name : patel
     * date_of_birth : 0000-00-00
     * gender : MALE
     * address : address 1
     * address1 : address2
     * city : Mehsana
     * state : Gujarat
     * zip : 666666
     * mobile : 123456789012
     * landline : 123654789012
     * email : vishal@gmail.com
     * how_did_you_find_us : Other
     * ailments :
     * pain_picture : null
     * find_us_reason : other data
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
        @SerializedName("address1")
        private String address1;
        @SerializedName("city")
        private String city;
        @SerializedName("state")
        private String state;
        @SerializedName("zip")
        private String zip;
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
        private Object painPicture;
        @SerializedName("find_us_reason")
        private String findUsReason;

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

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
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

        public Object getPainPicture() {
            return painPicture;
        }

        public void setPainPicture(Object painPicture) {
            this.painPicture = painPicture;
        }

        public String getFindUsReason() {
            return findUsReason;
        }

        public void setFindUsReason(String findUsReason) {
            this.findUsReason = findUsReason;
        }
    }
}
