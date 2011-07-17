package models;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import play.utils.HTML;
import siena.Id;
import siena.Model;
import siena.Query;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

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

    public boolean today() {
        return DateUtils.isSameDay(this.date, new Date());
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
            String result = "";
            if (value == null || "".equals(value)) {
                this.temperature = null;
            } else {
                this.temperature = new BigDecimal(value).multiply(BigDecimal.TEN).intValue();
                result = value;
                if (this.temperature <= 0) {
                    this.temperature = null;
                    result = "";
                }
            }
            this.update();
            return result;
        }
        if ("sex".equals(field)) {
            if ("".equals(value) || " ".equals(value)) value = null;
            this.sex = value;
            this.update();
            return value;
        }
        if ("special".equals(field)) {
            if ("".equals(value) || " ".equals(value)) value = null;
            this.special = value;
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

    public static class DaySerializer implements JsonSerializer<Day> {

        public JsonElement serialize(Day day, Type type, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            result.addProperty("id", day.id);
            result.add("date", context.serialize(day.date));
            result.addProperty("temperature", day.temperature);
            if (day.temperature != null) {
                BigDecimal bd = new BigDecimal(day.temperature).divide(BigDecimal.TEN);
                result.add("temperature", new JsonPrimitive(bd));
            }
            result.addProperty("sex", HTML.htmlEscape(day.sex));
            result.addProperty("special", HTML.htmlEscape(day.special));
            result.addProperty("memo", HTML.htmlEscape(day.memo));
            result.addProperty("mucus", day.mucus);
            result.addProperty("blood", day.blood);
            return result;
        }
    }

}
