package com.lightbend.lagom.recipes.cinnamon.hello.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.api.ServiceLocator;
//1.4 import com.lightbend.lagom.javadsl.client.ConfigurationServiceLocator;
import com.lightbend.lagom.javadsl.api.ConfigurationServiceLocator;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.lightbend.lagom.recipes.cinnamon.hello.api.HelloService;
import com.typesafe.config.Config;
import play.Configuration;
import play.Environment;

public class HelloModule extends AbstractModule implements ServiceGuiceSupport {

    private final Environment environment;
    private final Config config;

//    public HelloModule(Environment environment, Config config) {
    public HelloModule(Environment environment, Configuration config) {
        this.environment = environment;
        this.config = config.underlying();
    }

    @Override
    protected void configure() {
        bindService(HelloService.class, HelloServiceImpl.class);

        // Only needed to allow the sample to run from test and dist with the config Service Locator
        if (environment.isProd()) {
            bind(ServiceLocator.class).to(ConfigurationServiceLocator.class);
        }
    }

}
