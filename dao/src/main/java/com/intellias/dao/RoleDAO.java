package com.intellias.dao;

import com.intellias.model.Role;
import java.util.List;
import java.util.Optional;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface RoleDAO {
    @SqlBatch("INSERT INTO roles (user_id, name) VALUES (:user_id, :role.name) "
            + "ON CONFLICT ON CONSTRAINT roles_name_user_id_key DO NOTHING")
    void upsertUserRoles(@Bind("user_id") long userId, @BindBean("role") Role... roles);

    @SqlUpdate("UPDATE TABLE roles SET name=:name WHERE id=:id")
    void updateRole(@BindBean Role role);

    @SqlUpdate("DELETE FROM roles WHERE id=:id")
    void deleteRole(@Bind("id") long id);

    @SqlUpdate("DELETE FROM roles WHERE name ILIKE :name AND user_id=:id")
    void deleteRole(@Bind("name") String name, @Bind("id") long userId);

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @SqlQuery("SELECT EXISTS(SELECT * FROM roles WHERE id=:id LIMIT 1)")
    boolean roleExists(@Bind("id") long id);

    @SqlQuery("SELECT * FROM roles ORDER BY name")
    List<Role> listRoles();

    @SqlQuery("SELECT * FROM roles WHERE id=:id")
    Optional<Role> getRoleById(@Bind("id") long id);

    @SqlQuery("SELECT * FROM roles WHERE name=:name && user_id=:id")
    Optional<Role> getRoleByNameAndUser(@Bind("name") String name, @Bind("id") long userId);

    @SqlQuery("SELECT * FROM roles WHERE user_id=:id")
    List<Role> listUserRoles(@Bind("id") long id);
}
