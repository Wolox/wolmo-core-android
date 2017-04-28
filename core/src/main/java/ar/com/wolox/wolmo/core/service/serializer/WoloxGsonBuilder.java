package ar.com.wolox.wolmo.core.service.serializer;

import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import org.joda.time.LocalDate;

/**
 * This class provides a {@link GsonBuilder} pre configured.
 */
public class WoloxGsonBuilder {

    /**
     * Returns a basic gson builder with {@link FieldNamingPolicy#LOWER_CASE_WITH_UNDERSCORES}
     * and registers a {@link DateDeserializer} as type adapter for {@link LocalDate}.
     *
     * @return Gson builder
     */
    @NonNull
    public static GsonBuilder getBasicGsonBuilder() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(LocalDate.class, new DateDeserializer());
    }

}