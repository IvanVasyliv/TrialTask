package com.intellias.service;

import com.google.inject.Inject;
import com.intellias.dao.RoleDAO;
import com.intellias.dao.UserDAO;
import com.intellias.model.Role;
import com.intellias.model.User;
import com.intellias.service.exception.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    public User getUser(long id) {
        return userDAO.getUserById(id).orElseThrow(() -> new EntityNotFoundException(User.class));
    }

    public List<User> getAllUsers() {
        return userDAO.listUsers();
    }

    public long createUser(User user) {
        if (user.getId() != null) {
            Optional<User> oldUser = userDAO.getUserById(user.getId());
            if (oldUser.isPresent() && oldUser.get().getRoles() != user.getRoles()) {
                updateUserRoles(oldUser.get(), user);
            }
            return userDAO.upsertUser(user);
        } else {
            return userDAO.insertUser(user);
        }
    }

    public User updateUser(long id, User user) {
        user.setId(id);
        updateUserRoles(getUser(id), user);
        //noinspection OptionalGetWithoutIsPresent
        return userDAO.getUserById(userDAO.upsertUser(user)).get();
    }

    public boolean deleteUser(long id){
        if (!userDAO.userExists(id))
            throw new EntityNotFoundException(User.class);
        userDAO.deleteUser(id);
        return true;
    }

    public User grantRoles(long id, Role... roles) {
        if (!userDAO.userExists(id))
            throw new EntityNotFoundException(User.class);
        roleDAO.upsertUserRoles(id, roles);
        //noinspection OptionalGetWithoutIsPresent
        return userDAO.getUserById(id).get();
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
