package models;

import controllers.OntoControlApi;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: yuanzhe
 * Date: 13-11-27
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */

/**
 * A singleton object to initialize Individual instances, given individuals from the ontology
 */
public class Individuals {
    private static Individuals ourInstance = new Individuals();

    public static Individuals getInstance() {
        return ourInstance;
    }

    /**
     * Given an OWL named individual, create an Individual instance for this application
     * @param ind an OWL named individual
     * @return an Individual instance initialized with given properties
     */
    public static Individual create(OWLNamedIndividual ind) {
        String name=ind.getIRI().getFragment();

        // each OWL individual may belong to a series of types
        Set<OWLClass> typeSet=OntoControlApi.getReasoner().getTypes(ind,true).getFlattened();
        List<String> list=new ArrayList<String>();
        if (typeSet.isEmpty()) list.add("undefined");
        else {
            for (OWLClass cls:typeSet) {
                list.add(cls.getIRI().getFragment());
            }
        }

        // the location of an individual is related to a ContainedIn object property
        Set<OWLNamedIndividual> locSet=OntoControlApi.getReasoner()
                .getObjectPropertyValues(ind,OntoControlApi.getContainedIn()).getFlattened();
        String location;
        if (locSet.isEmpty()) location="undefined";
        else location=locSet.iterator().next().getIRI().getFragment();

        // check if an individual has a Controllable data property
        Set<OWLLiteral> controlSet=OntoControlApi.getReasoner()
                .getDataPropertyValues(ind,OntoControlApi.getControllable());
        boolean device,controllable;

        // an individual without such a property does not have a state either (e.g., desks, chairs)
        String state="N/A";
        if (controlSet.isEmpty()) {
            device=false;
            controllable=false;
        }

        // a controllable individual indicates an actuator, otherwise it is a sensor
        else {
            device=true;
            controllable=controlSet.iterator().next().getLiteral().equals("true");
            Set<OWLLiteral> stateSet;
            if(controllable) stateSet=OntoControlApi.getReasoner()
                    .getDataPropertyValues(ind,OntoControlApi.getHasConfiguredState());
            else stateSet=OntoControlApi.getReasoner().getDataPropertyValues(ind,OntoControlApi.getHasSensedState());
            if(!stateSet.isEmpty()) state=stateSet.iterator().next().getLiteral().toString();
            else state="undefined";
        }
        return new Individual(name,list,location,state,device,controllable);
    }

    /**
     * A batch operation to create a group of individuals (e.g., a set of sensors)
     * @param set a set of OWL named individuals
     * @return a list of Individual instances for this application
     */
    public static List<Individual> create(Set<OWLNamedIndividual> set) {
        List list=new ArrayList<Individual>();
        for(OWLNamedIndividual ind:set) {
            list.add(create(ind));
        }
        return list;
    }
}
