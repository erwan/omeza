package controllers;

import java.util.Date;

import models.Period;
import play.Logger;
import play.modules.gae.GAE;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
        Period period = Period.lastPeriodFor(GAE.getUser().getEmail());
        Logger.info("length = " + period.length());
        render(period);
    }

    public static void newPeriod(Date start) {
        Logger.info("start = " + start);
        new Period(GAE.getUser().getEmail(), start).insert();
        index();
    }

    public static void archive() {
        render();
    }

    public static void logout() {
        GAE.logout("Application.index");
    }

    public static void login() {
        GAE.login("Application.index");
    }

    @SuppressWarnings("unused")
    @Before(unless={"login", "logout"})
    private static void auth() {
        if (!GAE.isLoggedIn()) login();
        renderArgs.put("user", GAE.getUser());
    }

}
