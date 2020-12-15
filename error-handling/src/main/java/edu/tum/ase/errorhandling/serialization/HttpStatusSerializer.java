package edu.tum.ase.errorhandling.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * Custom serializer to map a {@link HttpStatus} to its representation in JSON objects.
 */
public class HttpStatusSerializer extends JsonSerializer<HttpStatus> {

    @Override
    public void serialize(HttpStatus httpStatus, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(httpStatus.value());
    }
}
