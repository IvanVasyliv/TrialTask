package com.intellias;

import com.intellias.core.ValidatorBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.guicey.jdbi3.JdbiBundle;

public class TrialTaskApplication extends Application<TrialTaskConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrialTaskApplication.class);

    public TrialTaskApplication() {
    }

    public static void main(final String[] args) throws Exception {
        new TrialTaskApplication().run(args);
    }

    @Override
    public String getName() {
        return "TrialTask";
    }
 
    @Override
    public void initialize(Bootstrap<TrialTaskConfiguration> b) {
        b.addBundle(GuiceBundle.builder()
                .enableAutoConfig(getClass().getPackageName())
                .bundles(JdbiBundle.<TrialTaskConfiguration>forDatabase(
                        ((config, environment) -> config.getDataSourceFactory()
                        )).withPlugins(new PostgresPlugin()),
                        new ValidatorBundle())
                .build());
    }
 
    @Override
    public void run(TrialTaskConfiguration config, Environment e) throws Exception {
    }

}
