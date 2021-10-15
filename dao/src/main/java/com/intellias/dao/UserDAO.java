package com.intellias.dao;

import com.intellias.dao.utils.UserReducer;
import com.intellias.model.User;
import java.util.List;
import java.util.Optional;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;

public interface UserDAO {

    @SqlQuery("INSERT INTO users(id, name) VALUES (:id, :name) "
            + "ON CONFLICT ON CONSTRAINT users_pkey DO UPDATE SET name=:name "
            + "RETURNING id")
    long upsertUser(@BindBean User user);

    @SqlQuery("INSERT INTO users(name) VALUES (:name) RETURNING id")
    long insertUser(@BindBean User user);

    @SqlUpdate("DELETE FROM users WHERE id=:id")
    void deleteUser(@Bind("id") long id);

    @SqlQuery("SELECT u.id AS u_id, u.name AS u_name, r.id AS r_id, r.name AS r_name "
            + "FROM users AS u LEFT JOIN roles AS r ON u.id=r.user_id "
            + "ORDER by u.name")
    @RegisterBeanMapper(value = User.class, prefix = "u")
    @UseRowReducer(UserReducer.class)
    List<User> listUsers();

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @SqlQuery("SELECT EXISTS(SELECT * FROM users WHERE id=:id LIMIT 1)")
    boolean userExists(@Bind("id") long id);

    @SqlQuery("SELECT u.id AS u_id, u.name AS u_name, r.id AS r_id, r.name AS r_name "
            + "FROM users AS u LEFT JOIN roles AS r ON u.id=r.user_id "
            + "WHERE u.id=:id")
    @RegisterBeanMapper(value = User.class, prefix = "u")
    @UseRowReducer(UserReducer.class)
    Optional<User> getUserById(@Bind("id") long id);
}
