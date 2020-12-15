package edu.tum.ase.errorhandling.exception;

public class ResourceAlreadyExistsException extends RuntimeException{

    public ResourceAlreadyExistsException(Class c, String field, String value){
        super(c.getSimpleName() + " with " + field + " '" + value + "' already exists");
    }
}
