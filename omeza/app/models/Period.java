package models;

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

public class Period extends Model {
    @Id
    public Long id;

    public String user;
    public Date start;
    public Date end;

    public Period(String user, Date start) {
        this(user, start, DateUtils.addDays(start, 28));
    }

    public Period(String user, Date start, Date end) {
        if (user == null || start == null || end == null) {
            throw new IllegalArgumentException();
        }
        this.user = user;
        this.start = start;
        this.end = end;
    }

    public long length() {
        return Lib.daysDifference(start, end);
    }

    public List<Day> days() {
        List<Day> days = Day.between(user, start, end);
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
        return Day.between(user, start, end);
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

    public static Period lastPeriodFor(String user) {
        List<Period> result = all().filter("user", user).order("-start").fetch(1);
        return result.size() > 0 ? result.get(0) : null;
    }

}
