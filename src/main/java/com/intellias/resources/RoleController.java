package com.intellias.resources;

import com.google.inject.Inject;
import com.intellias.api.Role;
import com.intellias.db.RolesDAO;
import java.util.ArrayList;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/roles")
public class RoleController {
    private final RolesDAO rolesDAO;
    private final Validator validator;

    @Inject
    public RoleController(RolesDAO rolesDAO, Validator validator) {
        this.rolesDAO = rolesDAO;
        this.validator = validator;
    }

    @GET
    public Response listRoles() {
        return Response.ok(rolesDAO.listRoles()).build();
    }

    @GET
    @Path("/{id}")
    public Response getRole(@PathParam("id") long id) {
        Role role = rolesDAO.getRoleById(id);
        if (role != null) {
            return Response.ok(role).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateRole(@PathParam("id") long id, Role newRole) {
        Role role = rolesDAO.getRoleById(id);
        if (role!=null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        Set<ConstraintViolation<Role>> violations = validator.validate(newRole);
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<>();
            for (ConstraintViolation<Role> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        newRole.setId(id);
        rolesDAO.updateRole(newRole);
        return Response.accepted().entity(newRole).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeUserById(@PathParam("id") Integer id) {
        if (rolesDAO.getRoleById(id) != null) {
            rolesDAO.deleteRole(id);
            return Response.accepted().build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
