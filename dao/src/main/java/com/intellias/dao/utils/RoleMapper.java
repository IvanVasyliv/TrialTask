package com.intellias.dao.utils;

import com.intellias.model.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

/** This RowMapper basically simply capitalizes role names.
 *  It exists solely as an example to show off Guice.
 *  However, now that that's showed off somewhere, I'm keeping it just for the sake of it reminding me how JDBI works.
 */
public class RoleMapper implements RowMapper<Role> {

    @Override
    public Role map(ResultSet rs, StatementContext ctx) throws SQLException {
        boolean withPrefix = false;
        for (int i=1; i<rs.getMetaData().getColumnCount(); i++) {
            if (rs.getMetaData().getColumnName(i).startsWith("r_")) {
                withPrefix = true;
                break;
            }
        }
        String name;
        long id;
        if (withPrefix) {
            name = rs.getString("r_name").toUpperCase(Locale.ROOT);
            id = rs.getLong("r_id");
        } else {
            name = rs.getString("name").toUpperCase(Locale.ROOT);
            id = rs.getLong("id");
        }
        Role role = new Role(name);
        role.setId(id);
        return role;
    }
}
