package controllers;

import models.User;
import play.Logger;
import play.i18n.Lang;
import play.modules.gae.GAE;
import play.mvc.Before;
import play.mvc.Controller;

public class Auth extends Controller {

    @Before
    static void domainFix() {
        if (request.domain.contains("appspot.com")) {
            redirect("http://www.omeza.org" + request.path);
        }
    }

    @Before(unless={"login", "logout", "Application.welcome"})
    static void auth() {
//        if (!GAE.isLoggedIn()) Application.welcome();
        if (!GAE.isLoggedIn()) login();
        renderArgs.put("user", GAE.getUser());
        renderArgs.put("admin", GAE.isAdmin());
        User user = getUser();
        if (user.locale != null) {
            Lang.change(user.locale);
        }
    }

    public static void logout() {
        GAE.logout("Application.index");
    }

    public static void login() {
        GAE.login("Application.index");
    }

    public static String getEmail() {
        return GAE.isLoggedIn() ? GAE.getUser().getEmail() : null;
    }

    public static User getUser() {
        return User.findOrCreate(getEmail());
    }

}
