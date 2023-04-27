package org.demo.ars.listener;

import org.demo.ars.commons.AppPropertiesLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author arsen.ibragimov
 *
 */
@Component
public class ServletWebServerInitializedEventListener implements ApplicationListener<ServletWebServerInitializedEvent> {

    Logger log = LoggerFactory.getLogger( getClass());

    public static String appPort = "";

    Environment env;

    public ServletWebServerInitializedEventListener( Environment env) {
        this.env = env;
        appPort = env.getProperty( "server.port");
    }

    @Override
    public void onApplicationEvent( ServletWebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        if( appPort.equals( String.valueOf( port))) {
            return;
        }
        appPort = String.valueOf( port);
        log.info( String.format( "setting port %s to logger", appPort));

        AppPropertiesLookup.setValue( "port", appPort);
        AppPropertiesLookup.reInitLogger();
    }

}
