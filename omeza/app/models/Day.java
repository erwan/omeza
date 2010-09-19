package models;

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
    public int temperature; // to divide by 10 to get Celcius, eg. 377 for 37.7
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

}
