package com.intellias.app.resource;

import com.intellias.model.Role;
import com.intellias.model.User;
import com.intellias.dao.RoleDAO;
import com.intellias.dao.UserDAO;
import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Inject;
import javax.validation.Valid;
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
import lombok.RequiredArgsConstructor;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserController {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

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
    public Response createUser(@Valid User user) throws URISyntaxException {
        long id = userDAO.upsertUser(user);
        // If the user that is sent has roles, properly add them to the database.
        // If a role with this name is already registered to the user, no action is taken.
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            roleDAO.upsertUserRoles(id, user.getRoles().toArray(new Role[0]));
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
        roleDAO.upsertUserRoles(id, roles);
        return Response.accepted().entity(userDAO.getUserById(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUserById(@PathParam("id") long id, User user) {
        if (!userDAO.userExists(id)) {
            return Response.status(Status.NOT_FOUND).build();
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
