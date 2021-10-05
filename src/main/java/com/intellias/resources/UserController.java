package com.intellias.resources;

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

import com.intellias.api.Role;
import com.intellias.api.User;
import com.intellias.db.RolesDAO;
import com.intellias.db.UsersDAO;

import org.jdbi.v3.core.Jdbi;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    private final Validator validator;
    private final Jdbi jdbi;

    public UserController(Jdbi jdbi, Validator validator) {
        this.jdbi = jdbi;
        this.validator = validator;
    }

    @GET
    public Response listUsers() {
        return Response.ok(jdbi.withExtension(UsersDAO.class, dao -> dao.listUsers())).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") long id) {
        User user = jdbi.withExtension(UsersDAO.class, dao -> dao.getUserById(id));
        if (user != null) {
            ArrayList<Object> serialiseList = new ArrayList<Object>();
            serialiseList.add(user);
            serialiseList.add(jdbi.withExtension(RolesDAO.class, dao -> dao.listUserRoles(id)));
            return Response.ok(serialiseList).build();
        }
        else
            return Response.status(Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/roles/")
    public Response getUserRoles(@PathParam("id") Integer id) {
        List<Role> roles = jdbi.withExtension(RolesDAO.class, dao -> dao.listUserRoles(id));
        if (!roles.isEmpty())
            return Response.ok(roles).build();
        else
            return Response.status(Status.NOT_FOUND).build();
    }

    @POST
    public Response createUser(User user) throws URISyntaxException {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<User> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        long id = jdbi.withExtension(UsersDAO.class, dao -> dao.insertUser(user));
        return Response.created(new URI(Long.toString(id))).build();
    }

    @POST
    @Path("/{id}/roles")
    public Response grantRole(@PathParam("id") long id, Role role) throws URISyntaxException {
        if (!jdbi.withExtension(UsersDAO.class, dao -> dao.userExists(id))) 
            return Response.status(Status.NOT_FOUND).build();
        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<Role> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        jdbi.useExtension(RolesDAO.class, dao -> dao.insertUserRoles(id, role));
        return Response.accepted().build();
    }
 
    @PUT
    @Path("/{id}")
    public Response updateUserById(@PathParam("id") Integer id, User user) {
        if (!jdbi.withExtension(UsersDAO.class, dao -> dao.userExists(id)))
            return Response.status(Status.NOT_FOUND).build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<User> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        jdbi.useExtension(UsersDAO.class, dao-> dao.updateUser(user));
        return Response.accepted().build();
    }
 
    @DELETE
    @Path("/{id}")
    public Response removeUserById(@PathParam("id") Integer id) {
        if (jdbi.withExtension(UsersDAO.class, dao -> dao.userExists(id))) {
            jdbi.useExtension(UsersDAO.class, dao -> dao.deleteUser(id));
            return Response.accepted().build();
        } else
            return Response.status(Status.NOT_FOUND).build();
    }
}
