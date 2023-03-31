package org.demo.ars.commons.listener;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.demo.ars.commons.AppPropertiesLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

/**
 * @author arsen.ibragimov
 *
 */
public class ListenerUtils {

    private static Logger log = LoggerFactory.getLogger( ListenerUtils.class);

    static void updateLogLevel( Environment env, String caller) {
        String key = "log.level";

        String currentLevel = AppPropertiesLookup.get( key);
        String newLevel = env.getProperty( key);

        if( isNotBlank( newLevel) && !currentLevel.equalsIgnoreCase( newLevel)) {
            log.info( String.format( "Updating log level from %s to %s by %s", currentLevel, newLevel, caller));
            AppPropertiesLookup.setValue( key, newLevel);
            AppPropertiesLookup.reInitLogger();
        }
    }

}
