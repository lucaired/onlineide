package edu.tum.ase.errorhandling.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.tum.ase.errorhandling.serialization.HttpStatusSerializer;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    @JsonSerialize(using = HttpStatusSerializer.class)
    private final HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private final LocalDateTime timestamp;

    private final String message;

    public ErrorResponse(HttpStatus status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, Throwable ex) {
        this(status, ex.getMessage());
    }
}

