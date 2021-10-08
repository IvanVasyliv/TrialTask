package com.intellias.core;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.validation.Validator;
import ru.vyarus.dropwizard.guice.module.installer.bundle.GuiceyBundle;
import ru.vyarus.dropwizard.guice.module.installer.bundle.GuiceyEnvironment;

public class ValidatorBundle implements GuiceyBundle {

    @Override
    public void run(GuiceyEnvironment environment) {
        environment.modules(new ValidatorModule(environment.environment().getValidator()));
    }

    private static class ValidatorModule extends AbstractModule {

        private final Validator validator;

        public ValidatorModule(Validator validator) {
            this.validator = validator;
        }

        @Provides
        public Validator getEnvValidator() {
            return validator;
        }
    }

}
