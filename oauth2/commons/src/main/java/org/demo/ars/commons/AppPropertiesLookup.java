/**
 *
 */
package org.demo.ars.commons;

import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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

    private static final String logDir = "log.dir";

    private static final String logFile = "log.file";

    private static final String logErrorFile = "log.error.file";

    private static final String logLevel = "log.level";

    private static final String kafkaLogDisable = "kafka.log.disable";

    private static final String kafkaHost = "kafka.host";

    private static final String kafkaPort = "kafka.port";

    private static final String kafkaLogLevel = "kafka.log.level";

    private static final String eurekaHost = "eureka.host";

    private static final String eurekaPort = "eureka.port";

    private static final String authHost = "auth.host";

    private static final String authPort = "auth.port";

    private static final String authHostAuthorization = "auth.host.authorization";

    private static final String configServerHost = "config.server.host";

    private static final String configServerPort = "config.server.port";

    private static final String zooHost = "zoo.host";

    private static final String zooPort = "zoo.port";
    // For Spring Boot 1.5.22
    private static final String springCloudConfigUri = "spring.cloud.config.uri";

    private static final String LOG4J2_XML = "log4j2.xml";

    private static final Map<String, String> map = new HashMap<>();

    private static boolean initialized = false;

    private static ReentrantLock rel = new ReentrantLock();

    static {
        init();
    }

    private static Logger logger;

    public static void init() {
        try {
            if( rel.tryLock( 1, TimeUnit.SECONDS) && !initialized) {
                Properties prop = new Properties();

                prop.putAll( PropertiesUtils.loadPropertiesFromYaml( "application"));
                prop.putAll( PropertiesUtils.loadProperties( "default"));
                prop.putAll( PropertiesUtils.loadProperties( "application"));
                prop.putAll( System.getProperties());
                prop.putAll( System.getenv());

                map.put( "name", String.valueOf( prop.get( "spring.application.name")));
                map.put( "host", InetAddress.getLocalHost().getHostName());
                map.put( "port", getValue( String.valueOf( prop.get( "server.port")), String.valueOf( prop.get( "port"))));

                map.put( logDir, String.valueOf( prop.get( logDir)));
                map.put( logFile, String.valueOf( prop.get( logFile)));
                map.put( logLevel, String.valueOf( prop.get( logLevel)));

                map.put( logErrorFile, String.valueOf( prop.get( logErrorFile)));

                map.put( kafkaHost, String.valueOf( prop.get( kafkaHost)));
                map.put( kafkaPort, String.valueOf( prop.get( kafkaPort)));

                boolean kafkaLogDis = Boolean.parseBoolean( (String) prop.get( kafkaLogDisable));
                map.put( kafkaLogLevel, kafkaLogDis ? "OFF" : map.get( logLevel));

                map.put( zooHost, String.valueOf( prop.get( zooHost)));
                map.put( zooPort, String.valueOf( prop.get( zooPort)));

                map.put( configServerHost, String.valueOf( prop.get( configServerHost)));
                map.put( configServerPort, String.valueOf( prop.get( configServerPort)));

                map.put( authHost, String.valueOf( prop.get( authHost)));
                map.put( authPort, String.valueOf( prop.get( authPort)));
                map.put( authHostAuthorization, String.valueOf( prop.get( authHostAuthorization)));

                map.put( eurekaHost, String.valueOf( prop.get( eurekaHost)));
                map.put( eurekaPort, String.valueOf( prop.get( eurekaPort)));
                /************************************************************/

                System.setProperty( logLevel, map.get( logLevel));

                System.setProperty( kafkaHost, map.get( kafkaHost));
                System.setProperty( kafkaPort, map.get( kafkaPort));

                System.setProperty( zooHost, map.get( zooHost));
                System.setProperty( zooPort, map.get( zooPort));

                System.setProperty( configServerHost, map.get( configServerHost));
                System.setProperty( configServerPort, map.get( configServerPort));
                // For Spring Boot 1.5.22
                System.setProperty( springCloudConfigUri, String.format( "http://%s:%s", map.get( configServerHost), map.get( configServerPort)));

                System.setProperty( authHost, map.get( authHost));
                System.setProperty( authPort, map.get( authPort));
                System.setProperty( authHostAuthorization, map.get( authHostAuthorization));

                System.setProperty( eurekaHost, map.get( eurekaHost));
                System.setProperty( eurekaPort, map.get( eurekaPort));

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

    private static String getValue( String value1, String value2) {
        if( StringUtils.isNotBlank( value1) && !value1.trim().startsWith( "$")) {
            return value1;
        }
        return value2;
    }

}
