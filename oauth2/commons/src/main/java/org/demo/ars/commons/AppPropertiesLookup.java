package org.demo.ars.commons;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.slf4j.Logger;
import org.slf4j.event.Level;

/**
 * @author arsen.ibragimov
 *
 */
@Plugin( name = "app", category = "Lookup")
public class AppPropertiesLookup implements StrLookup {

    private static final String logLevel = "LOG_LEVEL";

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

                String profile = prop.getProperty( "spring.profiles.active");
                if( isNotBlank( profile)) {
                    defaultProperties.putAll( PropertiesUtils.loadProperties( "default", profile));
                    prop.putAll( defaultProperties);
                } else {
                    int i = 0;
                    while( isNotBlank( profile = prop.getProperty( String.format( "spring.profiles.active[%s]", i++)))) {
                        defaultProperties.putAll( PropertiesUtils.loadProperties( "default", profile));
                        prop.putAll( defaultProperties);
                    }
                }

                int i = 0;
                TreeSet<String> sortedKeys = new TreeSet<>( defaultProperties.stringPropertyNames());

                for( String key : sortedKeys) {
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

                // substitute value reference
                Set<String> names = new HashSet<>( defaultProperties.stringPropertyNames());

                for( String name : names) {
                    String value = defaultProperties.getProperty( name);
                    String oldValue = value;
                    boolean found = false;
                    i = 0;
                    while( true && i++ < 20) {
                        Matcher matcher = pattern.matcher( value);
                        if( matcher.find()) {
                            found = true;
                            String val = System.getProperty( matcher.group( 1));
                            value = value.replaceFirst( pattern.pattern(), val);
                        } else {
                            break;
                        }
                    }
                    if( found) {
                        log( Level.INFO, String.format( "value:%s replaced '%s' on '%s'", name, oldValue, value));
                        System.setProperty( name, value);
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
        log( Level.DEBUG, String.format( "lookup %s=%s", key, value));

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

        if( isBlank( value)) {
            return value;
        }
        Matcher matcher = pattern.matcher( value);

        if( matcher.matches()) {
            return prop.getProperty( matcher.group( 1));
        }
        return value;
    }

}
