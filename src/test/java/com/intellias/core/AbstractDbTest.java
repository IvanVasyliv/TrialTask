package com.intellias.core;

import com.intellias.db.RoleDAO;
import com.intellias.db.UserDAO;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractDbTest {
    protected static UserDAO userDAO;
    protected static RoleDAO roleDAO;

    @BeforeAll
    public static void createTables() {
        Jdbi jdbi = Jdbi.create("jdbc:postgresql://localhost:5432/test?user=tester&password=thisisatest");
        jdbi.installPlugin(new SqlObjectPlugin())
                .installPlugin(new PostgresPlugin());
        userDAO = jdbi.onDemand(UserDAO.class);
        roleDAO = jdbi.onDemand(RoleDAO.class);
        try {
            userDAO.createTable();
        } catch (UnableToExecuteStatementException e) {
            userDAO.dropTable();
            userDAO.createTable();
        }
        try {
            roleDAO.createTable();
        } catch (UnableToExecuteStatementException e) {
            roleDAO.dropTable();
            roleDAO.createTable();
        }
    }

    @AfterAll
    public static void dropTables() {
        userDAO.dropTable();
        roleDAO.dropTable();
    }
}
