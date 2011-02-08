package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;

import siena.Id;
import siena.Model;
import siena.Query;
import utils.Lib;

public class Period extends siena.Model { // TODO: See why no crud implements play.db.Model {
    @Id
    public Long id;

    public String user;
    public Date start;

    public static final int DEFAULT_LENGTH = 28;

    public Period(String user, Date start) {
        if (user == null || start == null) {
            throw new IllegalArgumentException();
        }
        this.user = user;
        this.start = start;
    }

    public Date end() {
        List<Period> next = all().filter("user", user).filter("start>", start).order("start").fetch(1);
        if (next.size() > 0) return DateUtils.addDays(next.get(0).start, -1);
        Date today = new Date();
        Date result = DateUtils.addDays(start, DEFAULT_LENGTH - 1);
        // Always give a few more days to allow for periods longer than 28 days
        if (Lib.daysDifference(result, today) < 3 && Lib.daysDifference(result, today) > -3) {
            result = DateUtils.addDays(today, 3);
        }
        return result;
    }

    public long length() {
        return Lib.daysDifference(start, end());
    }

    public List<Day> days() {
        List<Day> days = Day.between(user, start, end());
        if (days.size() == 0) {
            for (int i = 0; i < length(); i++) {
                new Day(user, DateUtils.addDays(start, i)).insert();
            }
       } else if (days.size() < length()) {
            Set<Date> haveit = new HashSet<Date>();
            for (Day day: days) {
                haveit.add(day.date);
            }
            for (int i = 0; i < length(); i++) {
                Date cursor = DateUtils.addDays(start, i);
                if (!haveit.contains(cursor)) {
                    new Day(user, DateUtils.addDays(start, i)).insert();
                }
            }
        } else {
            return days;
        }
        return Day.between(user, start, end());
    }

    public static Query<Period> all() {
        return Model.all(Period.class);
    }

    public static Period findById(Long id) {
        return all().filter("id", id).get();
    }

    public static Collection<Period> findByUser(String user) {
        return all().filter("user", user).fetch();
    }

    public static Set<String> allUsers() {
        Set<String> result = new HashSet<String>();
        for (Period p: all().fetch()) {
            result.add(p.user);
        }
        return result;
    }

    public static Period lastPeriodFor(String user) {
        List<Period> result = all().filter("user", user).order("-start").fetch(1);
        return result.size() > 0 ? result.get(0) : null;
    }

    public static List<Year> allByYear(String user) {
        List<Period> periods = Period.all().filter("user", user).order("-start").fetch();
        List<Year> result = new ArrayList<Year>();
        Year year = null;
        for (Period p: periods) {
            if (year == null || Lib.getYear(p.start) != year.year) {
                year = new Year(Lib.getYear(p.start));
                result.add(year);
            }
            year.periods.add(p);
        }
        return result;
    }

//    @Override
    public Object _key() {
        return id;
    }

//    @Override
    public void _save() {
        update();
    }

//    @Override
    public void _delete() {
        delete();
    }

    public static class Year {
        public int year;
        public List<Period> periods;
        public Year(int year) {
            this.year = year;
            this.periods = new ArrayList<Period>();
        }
        public Period first() {
            return this.periods.get(0);
        }
        public Period last() {
            return this.periods.get(this.periods.size() - 1);
        }
    }

}
