package edu.tum.ase.errorhandling.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ValidationErrorResponse extends ErrorResponse {

    private final List<Violation> violations;

    public ValidationErrorResponse(String message, List<Violation> violations) {
        super(HttpStatus.BAD_REQUEST, message);
        this.violations = violations;
    }
}
