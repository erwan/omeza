package models;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PeriodSerializer implements JsonSerializer<Period> {

    public JsonElement serialize(Period period, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        JsonArray temperature = new JsonArray();
        JsonArray sex = new JsonArray();
        JsonArray special = new JsonArray();
        int i = 1;
        for (Day day: period.days()) {
            JsonArray tday = new JsonArray();
            JsonArray sday = new JsonArray();
            JsonArray spday = new JsonArray();

            // Temperature
            tday.add(new JsonPrimitive(i));
            if (day.temperature == null) {
                tday.add(new JsonNull());
            } else {
                BigDecimal bd = new BigDecimal(day.temperature).divide(BigDecimal.TEN);
                tday.add(new JsonPrimitive(bd));
            }
            temperature.add(tday);

            // Sex
            sday.add(new JsonPrimitive(i));
            if (day.sex == null || "".equals(day.sex)) {
                sday.add(new JsonNull());
            } else {
                Integer temp = day.temperature != null ? day.temperature : 370;
                BigDecimal bd2 = new BigDecimal(temp).divide(BigDecimal.TEN).subtract(new BigDecimal("0.1"));
                sday.add(new JsonPrimitive(bd2));
            }
            sex.add(sday);

            // Special
            spday.add(new JsonPrimitive(i));
            if (day.special == null || "".equals(day.special)) {
                spday.add(new JsonNull());
            } else {
                Integer temp = day.temperature != null ? day.temperature : 370;
                BigDecimal bd2 = new BigDecimal(temp).divide(BigDecimal.TEN).add(new BigDecimal("0.1"));
                spday.add(new JsonPrimitive(bd2));
            }
            special.add(spday);

            i++;
        }
        result.add("temperature", temperature);
        result.add("sex", sex);
        result.add("special", special);
        return result;
    }

}
