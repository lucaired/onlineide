package edu.tum.ase.errorhandling;

import edu.tum.ase.errorhandling.exception.ResourceAlreadyExistsException;
import edu.tum.ase.errorhandling.exception.ResourceNotFoundException;
import edu.tum.ase.errorhandling.response.ErrorResponse;
import edu.tum.ase.errorhandling.response.ValidationErrorResponse;
import edu.tum.ase.errorhandling.response.Violation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    // TODO: Do we want to override some default responses, e.g. when a malformed JSON body is passed (handleHttpMessageNotReadable) ?

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException e) {
        ErrorResponse err = new ErrorResponse(HttpStatus.NOT_FOUND, e);
        return toResponseEntity(err);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    protected ResponseEntity<Object> handleResourceAlreadyExists(ResourceAlreadyExistsException e) {
        // Use HTTP status 409 if a resource already exists. There are some discussions about this:
        // https://stackoverflow.com/questions/9269040/which-http-response-code-for-this-email-is-already-registered/53144807
        // https://stackoverflow.com/questions/3825990/http-response-code-for-post-when-resource-already-exists
        ErrorResponse err = new ErrorResponse(HttpStatus.CONFLICT, e);

        return toResponseEntity(err);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ArrayList<Violation> violations = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            violations.add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        ValidationErrorResponse err = new ValidationErrorResponse("There have been errors during the request validation. See \"violations\" for details.", violations);

        return toResponseEntity(err);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        // Do not pass the error message on to the client, because it might be an internal/technical message.
        e.printStackTrace();
        ErrorResponse err = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please contact our development team.");
        return toResponseEntity(err);
    }

    private ResponseEntity<Object> toResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
