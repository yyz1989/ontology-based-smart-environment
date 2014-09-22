package controllers;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: yuanzhe
 * Date: 13-11-26
 * Time: 下午1:17
 * To change this template use File | Settings | File Templates.
 */

/**
 * A singleton object communicates between the web application and the ontology
 */
public class OntoControlApi {
    private static OntoControlApi ourInstance = new OntoControlApi();
    private static File file;
    private static OWLOntologyManager manager;
    private static OWLOntology smenvOntology;
    private static IRI documentIRI, ontologyIRI;
    private static OWLDataFactory factory;
    private static OWLReasonerFactory reasonerFactory;
    private static OWLReasoner reasoner;
    private static PrefixManager pm;
    private static OWLObjectRenderer renderer;

    private static OWLObjectProperty isContainedIn;
    private static OWLDataProperty isControllable;
    private static OWLDataProperty hasConfiguredState;
    private static OWLDataProperty hasSensedState;
    private static OWLDataProperty manuallyControlled;
    private static OWLDataHasValue manuallyControlledHasValueOn;
    private static OWLLiteral on;
    private static OWLClass sensor;
    private static OWLClass actuator;

    /**
     * Load the predefined ontology and initialize a set of static variables describing ontology structure
     */
    private OntoControlApi() {
        file=new File("public/onto/ontology.owl");
        manager= OWLManager.createOWLOntologyManager();
        try {
            smenvOntology=manager.loadOntologyFromOntologyDocument(file);
        }
        catch(OWLOntologyCreationException e) {
            System.out.println("error in loading ontology");
            return ;
        }
        System.out.println("loaded ontology:"+smenvOntology);
        documentIRI=manager.getOntologyDocumentIRI(smenvOntology);
        ontologyIRI = smenvOntology.getOntologyID().getOntologyIRI();
        System.out.println("    from: " + documentIRI +"    mapped to: "+ontologyIRI);
        factory = manager.getOWLDataFactory();
        reasonerFactory=new PelletReasonerFactory();
        reasoner=reasonerFactory.createReasoner(smenvOntology);
        reasoner.precomputeInferences();
        pm=new DefaultPrefixManager(ontologyIRI.toString()+"#");
        renderer = new DLSyntaxObjectRenderer();

        isContainedIn=factory.getOWLObjectProperty(":isContainedIn", pm);
        hasSensedState=factory.getOWLDataProperty(":hasSensedState", pm);
        hasConfiguredState=factory.getOWLDataProperty(":hasConfiguredState", pm);
        isControllable=factory.getOWLDataProperty(":isControllable", pm);
        manuallyControlled=factory.getOWLDataProperty(":manuallyControlled", pm);
        on=factory.getOWLLiteral("ON");
        manuallyControlledHasValueOn=factory.getOWLDataHasValue(manuallyControlled,on);
        sensor=factory.getOWLClass(":Sensor",pm);
        actuator=factory.getOWLClass(":Actuator",pm);
        System.out.println("ontology initialized successfully at "+System.currentTimeMillis());
    }

    /**
     * Get an individual with a given name
     * @param name name of the individual in String
     * @return an OWL Individual
     */
    public OWLIndividual getIndividual(String name) {
        OWLIndividual ind=factory.getOWLNamedIndividual("#"+name,pm);
        return ind;
    }

    /**
     * Get a class with a given name
     * @param name name of the class in String
     * @return an OWL Class
     */
    public OWLClass getClass(String name) {
        OWLClass cls=factory.getOWLClass("#"+name,pm);
        return cls;
    }

    /**
     * Add a data property assertion for a state change
     * @param propertyName name of data property
     * @param individualName name of individual
     * @param goalState value of new state
     * @param currentState value of current state
     * @param isControllable discrimination flag for actuators and sensors
     */
    public static void addDataPropertyAssertion(String propertyName, String individualName, String goalState, String currentState,
                                                String isControllable) {
        OWLDataPropertyAssertionAxiom ax;
        // if the individual is a actuator
        if(isControllable.equals("true")) {
            OWLDataPropertyAssertionAxiom axManualon=factory.getOWLDataPropertyAssertionAxiom(manuallyControlled,
                    factory.getOWLNamedIndividual(individualName,pm),"ON");
            OWLDataPropertyAssertionAxiom axManualoff=factory.getOWLDataPropertyAssertionAxiom(manuallyControlled,
                    factory.getOWLNamedIndividual(individualName,pm),"OFF");
            ax=factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(propertyName,pm),
                    factory.getOWLNamedIndividual(individualName,pm),goalState);
            System.out.println(goalState);

            // check if the actuator is under manual control
            if(!goalState.equals("undefined")) {
                manager.removeAxiom(smenvOntology, axManualoff);
                manager.addAxiom(smenvOntology, axManualon);
                deleteDataPropertyAssertion(propertyName, individualName, currentState);
                manager.addAxiom(smenvOntology,ax);
            }
            else {
                deleteDataPropertyAssertion(propertyName, individualName, currentState);
                manager.removeAxiom(smenvOntology, axManualon);
                manager.addAxiom(smenvOntology, axManualoff);
            }
        }

        // if the individual is a sensor
        else {
            if(!currentState.equals("undefined")) deleteDataPropertyAssertion(propertyName, individualName,
                    Double.valueOf(currentState));
            ax=factory.getOWLDataPropertyAssertionAxiom(factory.getOWLDataProperty(propertyName,pm),
                    factory.getOWLNamedIndividual(individualName,pm),Double.valueOf(goalState));
            manager.addAxiom(smenvOntology,ax);
        }

        // synchronize the ontology, abort the change if the change makes ontology inconsistent
        reasoner.flush();
        if(!reasoner.isConsistent()) {
            manager.removeAxiom(smenvOntology,ax);
            System.out.println("ontology inconsistent, operation cancelled");
            reasoner.flush();
        }
    }

    /**
     * Delete a data property assertion based on a state change, if the state is in String type
     * @param propertyName  name of the data property
     * @param individualName name of the individual
     * @param value value of the property
     */
    public static void deleteDataPropertyAssertion(String propertyName, String individualName, String value) {
        OWLDataPropertyAssertionAxiom ax=factory.getOWLDataPropertyAssertionAxiom(
                factory.getOWLDataProperty(propertyName,pm),factory.getOWLNamedIndividual(individualName,pm),value);
        manager.removeAxiom(smenvOntology, ax);
        reasoner.flush();
        if(!reasoner.isConsistent()) {
            manager.addAxiom(smenvOntology, ax);
            System.out.println("ontology inconsistent, operation cancelled");
            reasoner.flush();
        }
    }

    /**
     * Delete a data property assertion based on a state change, if the state is in double type
     * @param propertyName  name of the data property
     * @param individualName name of the individual
     * @param value value of the property
     */
    public static void deleteDataPropertyAssertion(String propertyName, String individualName, double value) {
        OWLDataPropertyAssertionAxiom ax=factory.getOWLDataPropertyAssertionAxiom(
                factory.getOWLDataProperty(propertyName,pm),factory.getOWLNamedIndividual(individualName,pm),value);
        manager.removeAxiom(smenvOntology, ax);
        reasoner.flush();
        if(!reasoner.isConsistent()) {
            manager.addAxiom(smenvOntology, ax);
            System.out.println("ontology inconsistent, operation cancelled");
            reasoner.flush();
        }
    }

    public static OWLDataProperty getHasConfiguredState() {
        return hasConfiguredState;
    }

    public static OWLDataProperty getHasSensedState() {
        return hasSensedState;
    }

    public static OWLDataHasValue getManuallyControlledHasValueOn() {
        return manuallyControlledHasValueOn;
    }

    public static OWLClass getSensor() {
        return sensor;
    }

    public static OWLClass getActuator() {
        return actuator;
    }

    public static OWLObjectRenderer getRenderer() {
        return renderer;
    }

    public static OWLReasonerFactory getReasonerFactory() {
        return reasonerFactory;
    }

    public static OWLReasoner getReasoner() {
        return reasoner;
    }

    public static PrefixManager getPm() {
        return pm;
    }

    public static OWLObjectProperty getContainedIn() {
        return isContainedIn;
    }

    public static OWLDataProperty getControllable() {
        return isControllable;
    }

    public static OWLOntologyManager getManager() {
        return manager;
    }

    public static OWLOntology getSmenvOntology() {
        return smenvOntology;
    }

    public static IRI getDocumentIRI() {
        return documentIRI;
    }

    public static IRI getOntologyIRI() {
        return ontologyIRI;
    }

    public static OWLDataFactory getFactory() {
        return factory;
    }

    public static OntoControlApi getInstance() {
        return ourInstance;
    }

}
