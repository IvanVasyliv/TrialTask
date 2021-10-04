package com.intellias.db;

import java.util.List;

import com.intellias.api.User;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UsersDAO {
    @SqlUpdate("CREATE TABLE users (id SERIAL, name VARCHAR(30) NOT NULL, PRIMARY KEY(id))")
    void createTable();

    @SqlQuery("INSERT INTO users(id, name) VALUES (:id, :name) " +
                "ON DUPLICATE KEY UPDATE name=:name; SELECT * FROM users WHERE id=:id")
    @RegisterBeanMapper(User.class)
    User insertUser(@BindBean User user);

    @SqlQuery("UPDATE users SET name=:name WHERE id=:id; SELECT * FROM users WHERE id=:id")
    @RegisterBeanMapper(User.class)
    User updateUser(@BindBean User user);

    @SqlUpdate("DELETE FROM users WHERE id=:id")
    void deleteUser(@Bind("id") int id);

    @SqlQuery("SELECT * FROM users ORDER BY name")
    @RegisterBeanMapper(User.class)
    List<User> listUsers();

    @SqlQuery("SELECT * FROM users WHERE id=:id")
    @RegisterBeanMapper(User.class)
    User getUserById(@Bind("id") int id);
}
