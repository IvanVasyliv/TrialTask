package com.intellias.resources;

import com.google.inject.Inject;
import com.intellias.api.Role;
import com.intellias.api.User;
import com.intellias.db.RoleDAO;
import com.intellias.db.UserDAO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final Validator validator;

    @Inject
    public UserController(UserDAO userDAO, RoleDAO roleDAO, Validator validator) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.validator = validator;
    }

    @GET
    public Response listUsers() {
        return Response.ok(userDAO.listUsers()).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") long id) {
        User user = userDAO.getUserById(id);
        if (user != null) {
            return Response.ok(user).build();
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
        long id = userDAO.insertUser(user);
        // If the user that is sent has roles, properly add them to the database.
        // If a role with this name is already registered to the user, no action is taken.
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            roleDAO.insertUserRoles(id, user.getRoles().toArray(new Role[0]));
        }
        return Response.created(new URI(Long.toString(id))).build();
    }

    @POST
    @Path("/{id}/role")
    public Response grantUserRole(@PathParam("id") long id, Role role) {
        return grantUserRoles(id, role);
    }

    @POST
    @Path("/{id}/roles")
    public Response grantUserRoles(@PathParam("id") long id, Role... roles) {
        if (!userDAO.userExists(id)) {
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
        roleDAO.insertUserRoles(id, roles);
        return Response.accepted().entity(userDAO.getUserById(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUserById(@PathParam("id") long id, User user) {
        if (!userDAO.userExists(id)) {
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
        // The id of the received user will be overwritten.
        user.setId(id);
        userDAO.updateUser(user);
        return Response.accepted().entity(user).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeUserById(@PathParam("id") Integer id) {
        if (userDAO.userExists(id)) {
            userDAO.deleteUser(id);
            return Response.accepted().build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
