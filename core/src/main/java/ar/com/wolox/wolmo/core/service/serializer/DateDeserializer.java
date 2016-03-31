package ar.com.wolox.wolmo.core.service.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Type;

/**
 * Transforms a date string with the format yyyy-MM-dd into a LocalDate class
 * from the JodaTime library and viceversa
 */
public class DateDeserializer implements JsonDeserializer<LocalDate>,
        JsonSerializer<LocalDate> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public LocalDate deserialize(JsonElement element, Type arg1,
                                 JsonDeserializationContext arg2) throws JsonParseException {
        String date = element.toString();
        date = date.substring(1, date.length() - 1);
        DateTimeFormatter dtf = DateTimeFormat.forPattern(DATE_FORMAT);
        return dtf.parseLocalDate(date);
    }

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonParser().parse(src.toString());
    }
}