/**
 *
 */
package org.demo.ars.commons;

import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * @author arsen.ibragimov
 *
 */
@Plugin( name = "app", category = "Lookup")
public class AppPropertiesLookup implements StrLookup {

    private static Map<String, String> map = new HashMap<>();

    static {
        init();
    }

    // order has to be after init
    private static Logger logger = LoggerFactory.getLogger( AppPropertiesLookup.class);

    public static void init() {
        try {
            Properties prop = new Properties();
            prop.putAll( PropertiesUtils.loadPropertiesFromYaml());
            prop.putAll( PropertiesUtils.loadProperties());
            prop.putAll( System.getenv());

            map.put( "name", String.valueOf( prop.get( "spring.application.name")));
            map.put( "host", InetAddress.getLocalHost().getHostName());
            map.put( "port", getValue( String.valueOf( prop.get( "server.port")), String.valueOf( prop.get( "port"))));
            map.put( "log.dir", String.valueOf( prop.get( "log.dir")));
            map.put( "log.file", String.valueOf( prop.get( "log.file")));
            map.put( "kafka.host", String.valueOf( prop.get( "kafka.host")));
            map.put( "kafka.port", String.valueOf( prop.get( "kafka.port")));
        } catch( Exception e) {
            log( Level.ERROR, "Error:", e);
        }
    }

    public static void reInitLogger() {
        log( Level.INFO, "reInitLogger start");

        try {
            ClassLoader classLoader = AppPropertiesLookup.class.getClassLoader();
            URL file = classLoader.getResource( "log4j2.xml");
            LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext( false);
            context.setConfigLocation( file.toURI());

            log( Level.INFO, "reInitLogger finish");
        } catch( Exception e) {
            log( Level.ERROR, "Error:", e);
        }
    }

    public static void setValue( String key, String value) {
        log( Level.INFO, String.format( "Set key: %s, value: %s", key, value));
        map.put( key, value);
    }

    @Override
    public String lookup( String key) {
        log( Level.INFO, String.format( "getting key: %s, map.isNull: %s", key, map == null));
        if( map == null) {
            return null;
        }
        String value = map.get( key);
        log( Level.INFO, String.format( "key: %s, value: %s", key, value));

        return value;
    }

    @Override
    public String lookup( LogEvent event, String key) {
        return null;
    }

    private static void log( Level level, String message) {
        log( level, message, null);
    }

    private static void log( Level level, String message, Exception e) {
        if( logger != null) {
            if( Level.INFO == level) {
                logger.info( message);
            } else if( Level.ERROR == level) {
                logger.error( message, e);
            }
        } else {
            System.out.println( message);
            if( e != null) {
                e.printStackTrace();
            }
        }
    }

    private static String getValue( String value1, String value2) {
        if( StringUtils.isNotBlank( value1) && !value1.trim().startsWith( "$")) {
            return value1;
        }
        return value2;
    }
}
