package com.intellias.service;

import com.google.inject.Inject;
import com.intellias.dao.RoleDAO;
import com.intellias.dao.UserDAO;
import com.intellias.model.Role;
import com.intellias.model.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    public User getUser(long id) {
        return userDAO.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userDAO.listUsers();
    }

    public long createUser(User user) {
        if (user.getId() == -1) {
            // Because only new Users get an id of -1, there's no sense in upserting.
            return userDAO.insertUser(user).getId();
        } else {
            User oldUser = userDAO.getUserById(user.getId());
            if (oldUser != null && oldUser.getRoles() != user.getRoles()) {
                updateUserRoles(oldUser, user);
            }
            return userDAO.upsertUser(user).getId();
        }
    }

    public User updateUser(long id, User user) {
        if (!userDAO.userExists(id))
            return null;
        user.setId(id);
        if (!user.getRoles().isEmpty()) {
            updateUserRoles(userDAO.getUserById(id), user);
        }
        return userDAO.upsertUser(user);
    }

    public boolean deleteUser(long id){
        if (!userDAO.userExists(id))
            return false;
        userDAO.deleteUser(id);
        return true;
    }

    public User grantRoles(long id, Role... roles) {
        if (!userDAO.userExists(id))
            return null;
        roleDAO.upsertUserRoles(id, roles);
        return userDAO.getUserById(id);
    }

    private void updateUserRoles(User oldUser, User newUser) {
        // Delete roles that were present in the oldUser object, but aren't present in user
        oldUser.getRoles().stream().filter(role ->
                        newUser.getRoles().stream()
                                .anyMatch(newRole -> !role.equals(newRole)))
                .forEach(role -> roleDAO.deleteRole(role.getName(),newUser.getId()));
        // Add new roles. Roles that were already present are rejected at Dao level.
        roleDAO.upsertUserRoles(newUser.getId(), newUser.getRoles().toArray(Role[]::new));
    }
}
