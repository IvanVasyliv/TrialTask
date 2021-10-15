package com.intellias.app.exception;

import com.intellias.service.exception.EntityNotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class NotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    @Override
    public Response toResponse(EntityNotFoundException exception) {
        return Response
                .status(Status.NOT_FOUND)
                .entity(exception.getMessage())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
