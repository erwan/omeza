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
        JsonArray mucus = new JsonArray();
        JsonArray blood = new JsonArray();
        int i = 0;
        for (Day day: period.days()) {
            // Temperature
            JsonArray tday = new JsonArray();
            tday.add(new JsonPrimitive(i));
            if (day.temperature == null) {
                tday.add(new JsonNull());
            } else {
                BigDecimal bd = new BigDecimal(day.temperature).divide(BigDecimal.TEN);
                tday.add(new JsonPrimitive(bd));
            }
            temperature.add(tday);

            // Mucus
            JsonArray mday = new JsonArray();
            mday.add(new JsonPrimitive(i));
            mday.add(new JsonPrimitive(day.mucus == null ? 0 : day.mucus));
            mucus.add(mday);

            // Blood
            JsonArray bday = new JsonArray();
            bday.add(new JsonPrimitive(i));
            bday.add(new JsonPrimitive(day.blood == null ? 0 : day.blood));
            blood.add(bday);

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
        result.add("mucus", mucus);
        result.add("blood", blood);
        return result;
    }

}
