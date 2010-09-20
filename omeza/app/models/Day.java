package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import siena.Id;
import siena.Model;
import siena.Query;

public class Day extends Model {
    @Id
    public Long id;

    public String user;
    public Date date;
    public Integer temperature; // to divide by 10 to get Celcius, eg. 377 for 37.7
    public String sex;
    public String special;
    public String memo;
    public Integer mucus;
    public Integer blood;

    public Day(String user, Date date) {
        this.user = user;
        this.date = date;
    }

    public static Query<Day> all() {
        return Model.all(Day.class).order("date");
    }

    public static List<Day> between(String user, Date start, Date end) {
        return Day.all().filter("user", user).filter("date>=", start).filter("date<=", end).fetch();
    }

    public static Day findById(Long id) {
        return all().filter("id", id).get();
    }

    public static List<Day> findByUser(String user) {
        return all().filter("user", user).fetch();
    }

    public String updateField(String field, String value) {
        if ("temperature".equals(field)) {
            this.temperature = new BigDecimal(value).multiply(BigDecimal.TEN).intValue();
            this.update();
            return value;
        }
        if ("sex".equals(field)) {
            this.sex = value;
            this.update();
            return value;
        }
        if ("memo".equals(field)) {
            this.memo = value;
            this.update();
            return value;
        }
        if ("mucus".equals(field)) {
            this.mucus = Integer.parseInt(value);
            this.update();
            return this.mucus.toString();
        }
        if ("blood".equals(field)) {
            this.blood = Integer.parseInt(value);
            this.update();
            return this.blood.toString();
        }
        return null;
    }

}
