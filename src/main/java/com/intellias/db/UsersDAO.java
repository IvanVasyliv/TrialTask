package com.intellias.db;

import java.util.List;

import com.intellias.api.User;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UsersDAO {
    @SqlUpdate("CREATE TABLE users (id SERIAL, name VARCHAR(30) NOT NULL, PRIMARY KEY(id))")
    void createTable();

    @SqlUpdate("INSERT INTO users(name) VALUES (:name) " +
                "ON DUPLICATE KEY UPDATE name=:name")
    @GetGeneratedKeys
    long insertUser(@BindBean User user);

    @SqlUpdate("UPDATE users SET name=:name WHERE id=:id")
    void updateUser(@BindBean User user);

    @SqlUpdate("DELETE FROM users WHERE id=:id")
    void deleteUser(@Bind("id") long id);

    @SqlQuery("SELECT * FROM users ORDER BY name")
    @RegisterBeanMapper(User.class)
    List<User> listUsers();

    @SqlQuery("SELECT EXISTS(SELECT * FROM users WHERE id=:id LIMIT 1)")
    boolean userExists(@Bind("id") long id);

    @SqlQuery("SELECT * FROM users WHERE id=:id LIMIT 1")
    @RegisterBeanMapper(User.class)
    User getUserById(@Bind("id") long id);
}
