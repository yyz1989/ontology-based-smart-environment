package controllers;

import models.*;
import play.mvc.*;

/**
 * The entry point of a Play application
 */
public class Application extends Controller {
    
    public static Result index() {
        Environment env=Environments.getCurrentEnvironment();
        return ok(views.html.environment.render(env));
    }
    
}
