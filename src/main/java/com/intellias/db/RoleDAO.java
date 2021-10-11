package com.intellias.db;

import java.util.List;

import com.intellias.api.Role;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import ru.vyarus.guicey.jdbi3.installer.repository.JdbiRepository;
import ru.vyarus.guicey.jdbi3.tx.InTransaction;

@InTransaction
@JdbiRepository
public interface RoleDAO {
    @SqlUpdate("CREATE TABLE roles (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, user_id BIGINT NOT NULL,"
            + "name VARCHAR(40) NOT NULL, FOREIGN KEY (user_id) REFERENCES users (id),"
            + "UNIQUE (name, user_id))")
    void createTable();

    @SqlBatch("INSERT INTO roles (user_id, name) VALUES (:user_id, :role.name) "
            + "ON CONFLICT ON CONSTRAINT roles_name_user_id_key DO NOTHING")
    void insertUserRoles(@Bind("user_id") long userId, @BindBean("role") Role... roles);

    @SqlUpdate("UPDATE TABLE roles SET name=:name WHERE id=:id")
    void updateRole(@BindBean Role role);

    @SqlUpdate("DELETE FROM roles WHERE id=:id")
    void deleteRole(@Bind("id") long id);

    @SqlQuery("SELECT * FROM roles ORDER BY name")
    List<Role> listRoles();

    @SqlQuery("SELECT * FROM roles WHERE id=:id")
    Role getRoleById(@Bind("id") long id);

    @SqlQuery("SELECT * FROM roles WHERE user_id=:id")
    List<Role> listUserRoles(@Bind("id") long id);

    @SqlUpdate("DROP TABLE roles")
    void dropTable();
}
