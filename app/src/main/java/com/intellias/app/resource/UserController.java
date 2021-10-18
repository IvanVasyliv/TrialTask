package com.intellias.app.resource;

import com.intellias.model.Role;
import com.intellias.model.User;
import com.intellias.service.UserService;
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

    private final UserService userService;

    @GET
    public Response listUsers() {
        return Response.ok(userService.getAllUsers()).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") long id) {
        User user = userService.getUser(id);
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @POST
    public Response createUser(@Valid User user) throws URISyntaxException {
        long id = userService.createUser(user);
        return Response.created(new URI(Long.toString(id))).build();
    }

    @POST
    @Path("/{id}/role")
    public Response grantUserRole(@PathParam("id") long id, @Valid Role role) {
        return grantUserRoles(id, role);
    }

    @POST
    @Path("/{id}/roles")
    public Response grantUserRoles(@PathParam("id") long id, Role... roles) {
        User updatedUser = userService.grantRoles(id, roles);
        if (updatedUser == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.accepted().entity(updatedUser).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUserById(@PathParam("id") long id, @Valid User user) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.accepted().entity(updatedUser).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeUserById(@PathParam("id") Integer id) {
        if (userService.deleteUser(id)) {
            return Response.accepted().build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
