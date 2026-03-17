package com.habitia.shared.domain.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String resource, String id){
        super(resource + " not found whit id: " + id);
    }
}
