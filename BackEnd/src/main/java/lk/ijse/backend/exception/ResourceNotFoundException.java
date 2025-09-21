package lk.ijse.backend.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;

    public ResourceNotFoundException(String resourceName) {
        super(resourceName + " not found");
        this.resourceName = resourceName;
    }

}

