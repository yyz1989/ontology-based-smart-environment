package models;
import java.util.List;

/**
 * The class to describe an individual in the ontology of a smart environment. It could be anything that describes a
 * THING acknowledged by the ontology. Such an individual must have a list of attributes including name, types, location,
 * current state, device(if it has a variable state), controllable(if it is a sensor or actuator)
 */
public class Individual {
    private String name;
    private List<String> type;
    private String location;
    private String state;
    private boolean device;
    private boolean controllable;;

    public Individual(String name, List<String> type, String location, String state, boolean device,
                      boolean controllable) {
        this.name = name;
        this.location = location;
        this.state = state;
        this.controllable = controllable;
        this.type = type;
        this.device = device;
    }

    public boolean isControllable() {
        return controllable;
    }

    public void setControllable(boolean controllable) {
        this.controllable = controllable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public boolean isDevice() {
        return device;
    }

    public void setDevice(boolean device) {
        this.device = device;
    }
}