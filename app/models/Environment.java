package models;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yuanzhe
 * Date: 13-11-27
 * Time: 下午5:04
 * To change this template use File | Settings | File Templates.
 */

/**
 * The class to describe an environment at a specific state
 */
public class Environment {
    private List sensors;
    private List actuators;
    private List rules;
    private List manuallyControlled;

    public Environment(List sensors, List actuators, List rules, List manuallyControlled) {
        this.sensors = sensors;
        this.actuators = actuators;
        this.rules = rules;
        this.manuallyControlled = manuallyControlled;
    }

    public List getManuallyControlled() {
        return manuallyControlled;
    }

    public void setManuallyControlled(List manualControlled) {
        this.manuallyControlled = manualControlled;
    }

    public List getSensors() {
        return sensors;
    }

    public void setSensors(List sensors) {
        this.sensors = sensors;
    }

    public List getActuators() {
        return actuators;
    }

    public void setActuators(List actuators) {
        this.actuators = actuators;
    }

    public List getRules() {
        return rules;
    }

    public void setRules(List rules) {
        this.rules = rules;
    }
}
