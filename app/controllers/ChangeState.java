package controllers;

import play.data.*;
import play.mvc.*;

/**
 * Created with IntelliJ IDEA.
 * User: yuanzhe
 * Date: 13-11-28
 * Time: 下午3:09
 * To change this template use File | Settings | File Templates.
 */

/**
 * The class to process a state change request from the Web UI
 */
public class ChangeState extends Controller {
    private static ChangeState ourInstance = new ChangeState();

    public static ChangeState getInstance() {
        return ourInstance;
    }

    /**
     * Given a state change request from the Web UI, propagate the change to the ontology and synchronize the ontology
     * @return the Webpage describing the current environment state after the state change
     */
    public static Result changeWorkingState() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String name=requestData.get("indName");
        String state=requestData.get("stateValue");
        String currentState=requestData.get("currentState");
        String isControllable=requestData.get("isControllable");
        if(isControllable.equals("true")) OntoControlApi.addDataPropertyAssertion("hasConfiguredState",
                name,state,currentState,isControllable);
        else {
            try {
                Double.valueOf(state);
            }
            catch (Exception e) {
                return badRequest("Only numbers are allowed");
            }
            OntoControlApi.addDataPropertyAssertion("hasSensedState",name,state,currentState,isControllable);
        }
        return redirect("/");
    }
}
