package com.intellias.service;

import com.google.inject.Inject;
import com.intellias.dao.RoleDAO;
import com.intellias.dao.UserDAO;
import com.intellias.model.Role;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class RoleService {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    public List<Role> getAllRoles() {
        return roleDAO.listRoles();
    }

    public List<Role> getUserRoles(long id) {
        if (!userDAO.userExists(id))
            return null;
        return roleDAO.listUserRoles(id);
    }

    public Role getRole(long id) {
        return roleDAO.getRoleById(id);
    }

    public Role updateRole(long id, Role role) {
        if (roleDAO.getRoleById(id) == null) {
            return null;
        }
        role.setId(id);
        roleDAO.updateRole(role);
        return role;
    }

    public boolean deleteRole(long id) {
        if (roleDAO.getRoleById(id) == null) {
            return false;
        }
        roleDAO.deleteRole(id);
        return true;
    }
}
