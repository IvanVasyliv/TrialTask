package com.intellias.resources;

import com.google.inject.Inject;
import com.intellias.api.Role;
import com.intellias.api.User;
import com.intellias.db.RolesDAO;
import com.intellias.db.UsersDAO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    private final UsersDAO usersDAO;
    private final RolesDAO rolesDAO;
    private final Validator validator;

    @Inject
    public UserController(UsersDAO usersDAO, RolesDAO rolesDAO, Validator validator) {
        this.usersDAO = usersDAO;
        this.rolesDAO = rolesDAO;
        this.validator = validator;
    }

    @GET
    public Response listUsers() {
        return Response.ok(usersDAO.listUsers()).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") long id) {
        User user = usersDAO.getUserById(id);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/roles/")
    public Response getUserRoles(@PathParam("id") Integer id) {
        List<Role> roles = rolesDAO.listUserRoles(id);
        if (!roles.isEmpty()) {
            return Response.ok(roles).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response createUser(User user) throws URISyntaxException {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<>();
            for (ConstraintViolation<User> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        long id = usersDAO.insertUser(user);
        return Response.created(new URI(Long.toString(id))).build();
    }

    @POST
    @Path("/{id}/roles")
    public Response grantRoles(@PathParam("id") long id, Role... roles) {
        if (usersDAO.userExists(id)) {
            return Response.status(Status.NOT_FOUND).build();
        }
        for (Role role : roles) {
            Set<ConstraintViolation<Role>> violations = validator.validate(role);
            if (violations.size() > 0) {
                ArrayList<String> validationMessages = new ArrayList<>();
                for (ConstraintViolation<Role> violation : violations) {
                    validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
                }
                return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
            }
        }
        rolesDAO.insertUserRoles(id, roles);
        return Response.accepted().entity(usersDAO.getUserById(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUserById(@PathParam("id") long id, User user) {
        if (!usersDAO.userExists(id)) {
            return Response.status(Status.NOT_FOUND).build();
        }
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<>();
            for (ConstraintViolation<User> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        user.setId(id);
        usersDAO.updateUser(user);
        return Response.accepted().entity(user).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeUserById(@PathParam("id") Integer id) {
        if (usersDAO.userExists(id)) {
            usersDAO.deleteUser(id);
            return Response.accepted().build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
