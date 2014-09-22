package models;
import controllers.OntoControlApi;
import org.semanticweb.owlapi.model.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: yuanzhe
 * Date: 13-11-28
 * Time: 下午7:44
 * To change this template use File | Settings | File Templates.
 */

/**
 * A singleton object to initialize a set of rules stored in the ontology
 */
public class Rules {
    private static Rules ourInstance = new Rules();

    public static Rules getInstance() {
        return ourInstance;
    }

    public static Rule createRule(SWRLRule rule) {
        return new Rule(OntoControlApi.getRenderer().render(rule));
    }

    public static List<Rule> createRules(Set<SWRLRule> set) {
        List list=new ArrayList<Rule>();
        for (SWRLRule rule:set) {
            list.add(createRule(rule));
        }
        return list;
    }
}
