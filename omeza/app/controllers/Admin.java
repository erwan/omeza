package controllers;

import java.util.Set;

import models.Period;
import play.modules.gae.GAE;
import play.mvc.Before;
import play.mvc.Controller;

public class Admin extends Controller {

    public static void index() {
        Set<String> users = Period.allUsers();
        render(users);
    }

    public static void assume(String username) {
        session.put("assume", username);
    }

    @Before
    static void check() {
        if (!GAE.isLoggedIn()) Auth.login();
        if (!GAE.isAdmin()) forbidden();
        renderArgs.put("user", GAE.getUser());
        renderArgs.put("admin", GAE.isAdmin());
    }

}
