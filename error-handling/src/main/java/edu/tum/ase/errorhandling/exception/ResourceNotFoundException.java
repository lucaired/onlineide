package edu.tum.ase.errorhandling.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Class c, String id) {
        super(c.getSimpleName() + " with id '" + id + "' was not found");
    }

}
