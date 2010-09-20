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
        int i = 1;
        for (Day day: period.days()) {
            JsonArray arrday = new JsonArray();
            arrday.add(new JsonPrimitive(i++));
            if (day.temperature == null) {
                arrday.add(new JsonNull());
            } else {
                BigDecimal bd = new BigDecimal(day.temperature).divide(BigDecimal.TEN);
                arrday.add(new JsonPrimitive(bd));
            }
            temperature.add(arrday);
        }
        result.add("temperature", temperature);
        return result;
    }

}
