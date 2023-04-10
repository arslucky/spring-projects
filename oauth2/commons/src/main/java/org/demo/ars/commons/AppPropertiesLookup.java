package org.demo.ars.commons;

import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
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

    private static final String logLevel = "log.level";

    private static final String LOG4J2_XML = "log4j2.xml";

    private static final Map<String, String> map = new HashMap<>();

    private static boolean initialized = false;

    private static ReentrantLock rel = new ReentrantLock();

    private static final Pattern pattern = Pattern.compile( "\\$\\{([\\w\\.]+)\\}");

    static {
        init();
    }

    private static Logger logger;

    public static void init() {
        try {
            if( rel.tryLock( 1, TimeUnit.SECONDS) && !initialized) {
                Properties prop = new Properties();

                prop.putAll( PropertiesUtils.loadPropertiesFromYaml( "application"));
                prop.putAll( PropertiesUtils.loadProperties( "application"));

                Properties defaultProperties = PropertiesUtils.loadProperties( "default");
                prop.putAll( defaultProperties);
                int i = 0;
                for( String key : defaultProperties.stringPropertyNames()) {
                    if( System.getenv( key) != null) {
                        log( Level.INFO, String.format( "%s %s=%s", i++, key, System.getenv( key)));
                        System.setProperty( key, System.getenv( key));
                    } else if( System.getProperty( key) != null) {
                        // do nothing;
                        log( Level.INFO, String.format( "%s %s=%s", i++, key, System.getProperty( key)));
                    } else {
                        log( Level.INFO, String.format( "%s %s=%s (default)", i++, key, defaultProperties.getProperty( key)));
                        System.setProperty( key, defaultProperties.getProperty( key));
                    }
                }

                prop.putAll( System.getProperties());
                prop.putAll( System.getenv());

                map.put( "name", String.valueOf( prop.get( "spring.application.name")));
                map.put( "host", InetAddress.getLocalHost().getHostName());

                String port = getValue( String.valueOf( prop.get( "server.port")), prop);
                if( port == null) {
                    port = getValue( String.valueOf( prop.get( "port")), prop);
                }
                if( port != null) {
                    map.put( "port", port);
                }

                map.put( logLevel, String.valueOf( prop.get( logLevel)));

                Configurator.initialize( null, LOG4J2_XML);
                logger = LoggerFactory.getLogger( AppPropertiesLookup.class);

                log( Level.INFO, map.toString());

                initialized = true;
            }
        } catch( Exception e) {
            log( Level.ERROR, "Error:", e);
        } finally {
            rel.unlock();
        }
    }

    public static void reInitLogger() {
        log( Level.INFO, "reInitLogger start");

        try {
            ClassLoader classLoader = AppPropertiesLookup.class.getClassLoader();
            URL file = classLoader.getResource( LOG4J2_XML);
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

    public static String get( String key) {
        if( !initialized) {
            init();
        }
        return map.get( key);
    }

    @Override
    public String lookup( String key) {

        if( !initialized) {
            init();
        }
        String value = map.get( key);
        log( Level.INFO, String.format( "%s=%s", key, value));

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

    private static String getValue( String value, Properties prop) {

        if( StringUtils.isBlank( value)) {
            return value;
        }
        Matcher matcher = pattern.matcher( value);

        if( matcher.matches()) {
            return prop.getProperty( matcher.group( 1));
        }
        return value;
    }

}
