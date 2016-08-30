package in.vaksys.storemanager.model;

/**
 * Created by lenovoi3 on 8/12/2016.
 */
public class user {
    String id_user;
    String username;

    public String getUserbranch() {
        return userbranch;
    }

    public void setUserbranch(String userbranch) {
        this.userbranch = userbranch;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    String userbranch;
}
