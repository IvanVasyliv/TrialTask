package com.intellias;

import com.intellias.resources.UserController;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
    public void run(TrialTaskConfiguration config, Environment e) throws Exception {
        LOGGER.info("Launching application");
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(e, config.getDataSourceFactory(), "mysql");
        e.jersey().register(new UserController(jdbi, e.getValidator()));
    }

}
