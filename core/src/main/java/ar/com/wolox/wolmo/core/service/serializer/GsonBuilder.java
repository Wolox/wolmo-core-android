package ar.com.wolox.wolmo.core.service.serializer;

import com.google.gson.FieldNamingPolicy;

import org.joda.time.LocalDate;

public class GsonBuilder {

    public static com.google.gson.GsonBuilder getBasicGsonBuilder() {
        return new com.google.gson.GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(LocalDate.class, new DateDeserializer());
    }

}