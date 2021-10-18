package com.intellias.service.exception;

import java.lang.reflect.Type;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Type t) {
        super(String.format("Entity of type %s could not be found. Check your address.",
                t.getClass().getSimpleName()));
    }
}
