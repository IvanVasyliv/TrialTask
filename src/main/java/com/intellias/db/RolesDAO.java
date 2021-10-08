package com.intellias.db;

import java.util.List;

import com.intellias.api.Role;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;

@JdbiRepository
public interface RolesDAO {
    @SqlUpdate("CREATE TABLE roles (id SERIAL, user_id BIGINT NOT NULL, "+
    "name VARCHAR(40) NOT NULL, PRIMARY KEY (id), FOREIGN KEY (user_id) REFERENCES users (id))")
    void createTable();

    @SqlBatch("INSERT INTO roles (id, user_id, name) VALUES (:role.id, :user_id, :role.name)")
    void insertUserRoles(@Bind("user_id") long userId, @BindBean("role") Role... roles);

    @SqlUpdate("UPDATE TABLE roles SET name=:name WHERE id=:id")
    void updateRole(@BindBean Role role);

    @SqlUpdate("DELETE FROM roles WHERE id=:id")
    void deleteRole(@Bind("id") long id);

    @SqlQuery("SELECT * FROM roles ORDER BY name")
    @RegisterBeanMapper(Role.class)
    List<Role> listRoles();

    @SqlQuery("SELECT * FROM roles WHERE id=:id")
    @RegisterBeanMapper(Role.class)
    Role getRoleById(@Bind("id") long id);

    @SqlQuery("SELECT * FROM roles WHERE user_id=:id")
    @RegisterBeanMapper(Role.class)
    List<Role> listUserRoles(@Bind("id") long id);
}
