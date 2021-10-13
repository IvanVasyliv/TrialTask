package com.intellias.app.resource;

import com.intellias.dao.RoleDAO;
import com.intellias.dao.UserDAO;
import com.intellias.model.Role;
import java.util.List;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class RoleController {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

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
    public Response getUserRoles(@PathParam("id") long id) {
        if (userDAO.userExists(id)) {
            List<Role> roles = roleDAO.listUserRoles(id);
            return Response.ok(roles).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateRole(@PathParam("id") long id, @Valid Role newRole) {
        Role role = roleDAO.getRoleById(id);
        if (role != null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        newRole.setId(id);
        roleDAO.updateRole(newRole);
        return Response.accepted().entity(newRole).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeUserById(@PathParam("id") long id) {
        if (roleDAO.getRoleById(id) != null) {
            roleDAO.deleteRole(id);
            return Response.accepted().build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
