package org.demo.ars.ui.config;

import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import ch.qos.logback.classic.LoggerContext;

/**
 * @author arsen.ibragimov
 *
 */
public class LoggingPropertiesConfig {

    public static void reInit( Environment env, String appPort) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        context.putProperty( "logs", env.getProperty( "log.dir"));
        context.putProperty( "app.name", env.getProperty( "spring.application.name"));
        context.putProperty( "app.port", appPort);
    }
}
