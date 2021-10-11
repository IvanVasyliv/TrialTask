package com.intellias.resources;

import com.google.inject.Inject;
import com.intellias.api.Role;
import com.intellias.db.RoleDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
public class RoleController {

    private final RoleDAO roleDAO;
    private final Validator validator;

    @Inject
    public RoleController(RoleDAO roleDAO, Validator validator) {
        this.roleDAO = roleDAO;
        this.validator = validator;
    }

    @GET
    public Response listRoles() {
        return Response.ok(roleDAO.listRoles()).build();
    }

    @GET
    @Path("/{id}")
    public Response getRole(@PathParam("id") long id) {
        Role role = roleDAO.getRoleById(id);
        if (role != null) {
            return Response.ok(role).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/roles/")
    public Response getUserRoles(@PathParam("id") Integer id) {
        List<Role> roles = roleDAO.listUserRoles(id);
        if (!roles.isEmpty()) {
            return Response.ok(roles).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateRole(@PathParam("id") long id, Role newRole) {
        Role role = roleDAO.getRoleById(id);
        if (role != null) {
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
        roleDAO.updateRole(newRole);
        return Response.accepted().entity(newRole).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeUserById(@PathParam("id") Integer id) {
        if (roleDAO.getRoleById(id) != null) {
            roleDAO.deleteRole(id);
            return Response.accepted().build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
