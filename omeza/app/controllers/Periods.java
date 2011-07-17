package controllers;

import java.math.BigDecimal;
import java.util.Date;

import models.Day;
import models.Day.DaySerializer;
import models.Period;
import models.Period.PeriodSerializer;

import org.mortbay.util.IO;

import play.Logger;
import play.modules.gae.GAE;
import play.mvc.Controller;
import play.utils.HTML;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
        ok();
    }

    public static void edit(Long periodId, Long dayId) throws Exception {
        Day day = Day.findById(dayId);
        if (day == null || !getEmail().equals(day.user)) {
            badRequest();
        }
        JsonObject json = ((new JsonParser()).parse(IO.toString(request.body))).getAsJsonObject();
        if (json.get("blood") != null)
            day.blood = json.get("blood").getAsInt();
        if (json.get("mucus") != null)
            day.mucus = json.get("mucus").getAsInt();
        if (json.get("temperature") != null)
            day.temperature = json.get("temperature").getAsBigDecimal().multiply(BigDecimal.TEN).intValue();
        if (json.get("sex") != null)
            day.sex = json.get("sex").getAsString();
        if (json.get("special") != null)
            day.special = json.get("special").getAsString();
        if (json.get("memo") != null)
            day.memo = json.get("memo").getAsString();
        day.update();
        ok();
    }

    // ~~ API

    public static void json(Long periodId) {
        Period period = Period.findById(periodId);
        if (period == null) notFound();
        if (!getEmail().equals(period.user)) {
            badRequest();
        }
        renderJSON(period, new PeriodSerializer(), new DaySerializer());
    }

    // ~~

    private static String getEmail() {
        return GAE.isLoggedIn() ? GAE.getUser().getEmail() : null;
    }

}
