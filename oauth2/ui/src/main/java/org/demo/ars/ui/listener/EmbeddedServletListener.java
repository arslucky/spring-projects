package org.demo.ars.ui.listener;

import org.demo.ars.commons.AppPropertiesLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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

        AppPropertiesLookup.setValue( "port", appPort);
        AppPropertiesLookup.reInitLogger();
    }

}
