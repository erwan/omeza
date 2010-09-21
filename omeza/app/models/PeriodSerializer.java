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
            if (day.sex != null && !"".equals(day.sex)) {
                sex.add(new JsonPrimitive(i));
            }

            // Special
            if (day.special != null && !"".equals(day.special)) {
                special.add(new JsonPrimitive(i));
            }

            i++;
        }
        result.add("temperature", temperature);
        result.add("sex", sex);
        result.add("special", special);
        return result;
    }

}
