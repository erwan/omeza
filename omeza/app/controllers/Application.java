package controllers;

import java.util.Date;
import java.util.List;

import models.Period;
import play.Play;
import play.Play.Mode;
import play.i18n.Lang;
import play.i18n.Messages;
import play.modules.gae.GAE;
import play.mvc.Before;
import play.mvc.Controller;

import com.google.gson.Gson;


public class Application extends Controller {

    public static void index() {
        Period period = Period.lastPeriodFor(getEmail());
        render(period);
    }

    public static void i18n() {
        response.contentType = "application/javascript";
        if (Play.mode == Mode.PROD) response.cacheFor("3h");
        renderText("var i18n = " + new Gson().toJson(Messages.all(Lang.get())));
    }

    public static void lang(String locale) {
        Lang.change(locale);
        index();
    }

    public static void period(Long periodId) {
        Period period = Period.findById(periodId);
        notFoundIfNull(period);
        render("Application/index.html", period);
    }

    public static void archive() {
        List<Period.Year> periods = Period.allByYear(getEmail());
        render(periods);
    }

    public static void longGraph(Date start, Date stop) {
        ok();
    }

    public static void longData(Date start, Date stop) {
        ok();
    }

    public static void logout() {
        GAE.logout("Application.index");
    }

    public static void login() {
        GAE.login("Application.index");
    }

    public static void favicon() {
        response.contentType = "image/x-icon";
        renderBinary(Play.getFile("/public/images/favicon.gif"));
    }

    @Before(unless={"login", "logout"})
    static void auth() {
        if (!GAE.isLoggedIn()) login();
        renderArgs.put("user", GAE.getUser());
        renderArgs.put("admin", GAE.isAdmin());
    }

    private static String getEmail() {
        return GAE.isLoggedIn() ? GAE.getUser().getEmail() : null;
    }

}
