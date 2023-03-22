package org.demo.ars.resource.listener;

import java.net.URL;

import org.demo.ars.resource.config.LoggingPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * @author arsen.ibragimov
 *
 */
@Component
public class EmbeddedServletListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    Logger log = LoggerFactory.getLogger( getClass());

    public static String appPort = "";

    Environment env;

    public EmbeddedServletListener( Environment env) {
        this.env = env;
        appPort = env.getProperty( "server.port");
    }

    @Override
    public void onApplicationEvent( EmbeddedServletContainerInitializedEvent event) {
        int port = event.getEmbeddedServletContainer().getPort();
        if( appPort.equals( String.valueOf( port))) {
            return;
        }
        appPort = String.valueOf( port);
        log.info( String.format( "setting port %s to logger", appPort));

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext( context);
            // Call context.reset() to clear any previous configuration, e.g. default
            // configuration. For multi-step configuration, omit calling context.reset().
            context.reset();

            LoggingPropertiesConfig.reInit( env, appPort);

            URL url = getClass().getClassLoader().getResource( "logback-spring.xml");
            configurator.doConfigure( url);
        } catch( JoranException je) {
            // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings( context);
    }

}
