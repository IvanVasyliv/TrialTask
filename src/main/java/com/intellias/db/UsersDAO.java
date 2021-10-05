package com.intellias.db;

import java.util.List;

import com.intellias.api.Role;
import com.intellias.api.User;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UsersDAO {
    @SqlUpdate("CREATE TABLE users (id SERIAL, name VARCHAR(30) NOT NULL, PRIMARY KEY(id))")
    void createTable();

    @SqlUpdate("INSERT INTO users(id, name) VALUES (:id, :name) " +
                "ON DUPLICATE KEY UPDATE name=:name")
    void insertUser(@BindBean User user);

    @SqlUpdate("UPDATE users SET name=:name WHERE id=:id")
    void updateUser(@BindBean User user);

    @SqlUpdate("DELETE FROM users WHERE id=:id")
    void deleteUser(@Bind("id") long id);

    @SqlQuery("SELECT users.*, roles.id AS role_id, roles.name AS role_name FROM users "+
                "LEFT JOIN roles "+
                "ON users.id=roles.user_id")
    @RegisterBeanMapper(value = Role.class, prefix = "role")
    @RegisterConstructorMapper(User.class)
    List<User> listUsers();

    @SqlQuery("SELECT users.*, roles.name AS role_name FROM users "+
                "LEFT JOIN roles "+
                "ON users.id=roles.user_id "+
                "WHERE users.id=:id")
    @RegisterConstructorMapper(User.class)
    User getUserById(@Bind("id") long id);
}
