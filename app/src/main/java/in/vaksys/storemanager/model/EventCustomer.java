package in.vaksys.storemanager.model;

/**
 * Created by lenovoi3 on 9/2/2016.
 */
public class EventCustomer {

    String id,name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EventCustomer(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
