package controllers;

import java.util.Date;
import java.util.List;

import models.Period;
import play.modules.gae.GAE;
import play.modules.gae.WS;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
        Period period = Period.lastPeriodFor(getEmail());
        render(period);
    }

    public static void period(Long periodId) {
        Period period = Period.findById(periodId);
        notFoundIfNull(period);
        render("Application/index.html", period);
    }

    public static void newPeriod(Date start) {
        new Period(GAE.getUser().getEmail(), start).insert();
        index();
    }

    public static void delete(Long periodId) {
        Period period = Period.findById(periodId);
        if (period == null || !getEmail().equals(period.user)) {
            badRequest();
        }
        period.delete();
        renderText("OK");
    }

    public static void archive() {
        List<Period> periods = Period.all().filter("user", getEmail()).order("-start").fetch();
        render(periods);
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

    private static String getEmail() {
        return GAE.isLoggedIn() ? GAE.getUser().getEmail() : null;
    }

}
