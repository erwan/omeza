package controllers;

import java.util.Date;
import java.util.List;

import models.Period;
import models.User;
import play.Play;
import play.Play.Mode;
import play.i18n.Lang;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;

import com.google.gson.Gson;

@With(Auth.class)
public class Application extends Controller {

    public static void index() {
        Period period = Period.lastPeriodFor(Auth.getEmail());
        render(period);
    }

    public static void i18n() {
        response.contentType = "application/javascript";
        if (Play.mode == Mode.PROD) response.cacheFor("3h");
        renderText("var i18n = " + new Gson().toJson(Messages.all(Lang.get())));
    }

    public static void lang(String locale) {
        Lang.change(locale);
        User user = Auth.getUser();
        user.locale = locale;
        user.update();
        index();
    }

    public static void period(Long periodId) {
        Period period = Period.findById(periodId);
        notFoundIfNull(period);
        render("Application/index.html", period);
    }

    public static void archive() {
        List<Period.Year> periods = Period.allByYear(Auth.getEmail());
        render(periods);
    }

    public static void longGraph(Date start, Date stop) {
        ok();
    }

    public static void longData(Date start, Date stop) {
        ok();
    }

    public static void favicon() {
        response.contentType = "image/x-icon";
        renderBinary(Play.getFile("/public/images/favicon.gif"));
    }

}
