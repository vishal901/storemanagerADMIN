package in.vaksys.storemanager.model;

/**
 * Created by lenovoi3 on 8/11/2016.
 */
public class branch {

    String id_branch;
    String branchname,branchaddress;

    public String getId_branch() {
        return id_branch;
    }

    public void setId_branch(String id_branch) {
        this.id_branch = id_branch;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getBranchaddress() {
        return branchaddress;
    }

    public void setBranchaddress(String branchaddress) {
        this.branchaddress = branchaddress;
    }
}
