package com.intellias.core;

import com.intellias.dao.RoleDAO;
import com.intellias.dao.UserDAO;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractDbTest {
    protected static UserDAO userDAO;
    protected static RoleDAO roleDAO;

    @BeforeAll
    public static void createTables() {
        Jdbi jdbi = Jdbi.create(
                "jdbc:postgresql://localhost:5432/test?user=tester&password=thisisatest");
        jdbi.installPlugin(new SqlObjectPlugin())
                .installPlugin(new PostgresPlugin());
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM roles");
            handle.createUpdate("DELETE FROM users CASCADE");
        });
    }
}
