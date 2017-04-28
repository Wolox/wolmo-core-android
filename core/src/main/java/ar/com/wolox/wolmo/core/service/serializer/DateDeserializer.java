/*
 * Copyright (c) Wolox S.A
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
 * Transforms a date string with the format yyyy-MM-dd into a {@link LocalDate} class
 * from the JodaTime library and viceversa
 */
public class DateDeserializer implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public LocalDate deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2)
        throws JsonParseException {

        String date = element.toString();
        date = date.substring(1, date.length() - 1);
        DateTimeFormatter dtf = DateTimeFormat.forPattern(getDateFormat());
        return dtf.parseLocalDate(date);
    }

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonParser().parse(src.toString());
    }

    /**
     * Returns the date format for the deserializer.
     * The date format utilized is: "<b>yyyy-MM-dd</b>"
     *
     * @return Date format
     */
    protected String getDateFormat() {
        return DATE_FORMAT;
    }
}