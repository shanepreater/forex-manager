package com.ovcs.forex.convertor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * Convert instants to it's string representation.
 */
@Component
public class InstantConvertor {
    public String fromObject(Instant instant) {
        return instant.toString();
    }

    public Instant fromString(String timeString) {
        return Instant.parse(timeString);
    }

    public JsonDeserializer<Instant> getDeserializer() {
        return new JsonDeserializer<Instant>() {
            @Override
            public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return fromString(p.getValueAsString());
            }
        };
    }

    public JsonSerializer<Instant> getSerializer() {
        return new JsonSerializer<Instant>() {
            @Override
            public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                gen.writeString(fromObject(value));
            }
        };
    }
}
