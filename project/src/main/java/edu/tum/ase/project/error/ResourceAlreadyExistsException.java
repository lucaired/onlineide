package edu.tum.ase.project.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceAlreadyExistsException extends ResponseStatusException {

    public ResourceAlreadyExistsException(Class c, String field, String value) {
        super(HttpStatus.CONFLICT, c.getSimpleName() + " with " + field + " '" + value + "' already exists");
    }
}
