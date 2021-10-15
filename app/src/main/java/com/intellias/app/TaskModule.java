package com.intellias.app;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.intellias.dao.RoleDAO;
import com.intellias.dao.UserDAO;
import com.intellias.dao.utils.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@RequiredArgsConstructor
public class TaskModule extends AbstractModule {

    private final Jdbi jdbi;

    @Override
    protected void configure() {
        jdbi.installPlugin(new PostgresPlugin())
                .installPlugin(new SqlObjectPlugin());
        jdbi.registerRowMapper(new RoleMapper());
    }

    @Provides
    @Singleton
    public RoleDAO getRoleDAO() {
        return jdbi.onDemand(RoleDAO.class);
    }

    @Provides
    @Singleton
    public UserDAO getUserDAO() {
        return jdbi.onDemand(UserDAO.class);
    }
}
