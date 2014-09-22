package models;

import controllers.OntoControlApi;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: yuanzhe
 * Date: 13-11-27
 * Time: 下午5:07
 * To change this template use File | Settings | File Templates.
 */

/**
 * A singleton object to initialize an Environment instance at a given state
 */
public class Environments {
    private static Environments ourInstance = new Environments();

    public static Environments getInstance() {
        return ourInstance;
    }

    /**
     * Get the current state of the environment variables from the ontology
     * @return an Environment instance described by a set of sensors, actuators, rules and manually controlled devices
     */
    public static Environment getCurrentEnvironment() {
        List sensors=Individuals.create(OntoControlApi.getReasoner().getInstances(OntoControlApi.getSensor(), true)
                .getFlattened());
        List actuators=Individuals.create(OntoControlApi.getReasoner().getInstances(OntoControlApi.getActuator(), true)
                .getFlattened());
        List rules=Rules.createRules(OntoControlApi.getSmenvOntology().getAxioms(AxiomType.SWRL_RULE));
        List manuallyControlled=Individuals.create(OntoControlApi.getReasoner()
                .getInstances(OntoControlApi.getManuallyControlledHasValueOn(),true).getFlattened());
        return new Environment(sensors, actuators, rules, manuallyControlled);
    }
}
