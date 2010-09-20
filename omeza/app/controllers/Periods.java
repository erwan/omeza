package controllers;

import java.util.Date;

import models.Day;
import models.Period;
import play.modules.gae.GAE;
import play.mvc.Controller;

public class Periods extends Controller {

    public static void create(Date start) {
        new Period(GAE.getUser().getEmail(), start).insert();
        Application.index();
    }

    public static void delete(Long periodId) {
        Period period = Period.findById(periodId);
        if (period == null || !getEmail().equals(period.user)) {
            badRequest();
        }
        period.delete();
        renderText("OK");
    }

    public static void edit(String id, String value) {
        String[] splitted = id.split("\\-");
        if (splitted.length != 2) badRequest();
        Day day = Day.findById(Long.parseLong(splitted[1]));
        if (day == null || !getEmail().equals(day.user)) {
            badRequest();
        }
        String response = day.updateField(splitted[0], value);
        renderText(response);
    }

    private static String getEmail() {
        return GAE.isLoggedIn() ? GAE.getUser().getEmail() : null;
    }

}
