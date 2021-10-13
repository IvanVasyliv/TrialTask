package com.intellias.dao;

import com.intellias.model.Role;
import java.util.List;

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

    @SqlQuery("SELECT * FROM roles ORDER BY name")
    List<Role> listRoles();

    @SqlQuery("SELECT * FROM roles WHERE id=:id")
    Role getRoleById(@Bind("id") long id);

    @SqlQuery("SELECT * FROM roles WHERE user_id=:id")
    List<Role> listUserRoles(@Bind("id") long id);
}
