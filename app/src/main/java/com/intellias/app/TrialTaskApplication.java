package com.intellias.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intellias.app.resource.RoleController;
import com.intellias.app.resource.UserController;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrialTaskApplication extends Application<TrialTaskConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrialTaskApplication.class);

    public static void main(final String[] args) throws Exception {
        new TrialTaskApplication().run(args);
    }

    @Override
    public String getName() {
        return "TrialTask";
    }

    @Override
    public void initialize(Bootstrap<TrialTaskConfiguration> b) {
    }

    @Override
    public void run(TrialTaskConfiguration config, Environment e) {
        final Jdbi jdbi = new JdbiFactory().build(e, config.getDatabase(), "postgresql");
        Injector injector = Guice.createInjector(new TaskModule(jdbi));
        e.jersey().register(injector.getInstance(UserController.class));
        e.jersey().register(injector.getInstance(RoleController.class));

    }
}