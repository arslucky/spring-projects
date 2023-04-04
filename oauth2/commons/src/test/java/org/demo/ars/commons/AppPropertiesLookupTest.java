package org.demo.ars.commons;

import static java.net.InetAddress.getLocalHost;
import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;

import org.junit.Test;

/**
 * @author arsen.ibragimov
 *
 */
public class AppPropertiesLookupTest {

    @Test
    public void log4j2get() throws UnknownHostException {

        assertEquals( AppPropertiesLookup.get( "name"), "null");
        assertEquals( AppPropertiesLookup.get( "host"), getLocalHost().getHostName());
        assertEquals( AppPropertiesLookup.get( "port"), "null");

        assertEquals( AppPropertiesLookup.get( "log.dir"), "c:/logs");
        assertEquals( AppPropertiesLookup.get( "log.file"), "oauth2.log");
        assertEquals( AppPropertiesLookup.get( "log.level"), "INFO");
        assertEquals( AppPropertiesLookup.get( "log.error.file"), "oauth2_error.log");
        assertEquals( AppPropertiesLookup.get( "kafka.host"), "localhost");
        assertEquals( AppPropertiesLookup.get( "kafka.port"), "9092");
        assertEquals( AppPropertiesLookup.get( "zoo.host"), "localhost");
        assertEquals( AppPropertiesLookup.get( "zoo.port"), "2181");
        assertEquals( AppPropertiesLookup.get( "config.server.host"), "localhost");
        assertEquals( AppPropertiesLookup.get( "config.server.port"), "8888");
        assertEquals( AppPropertiesLookup.get( "auth.host"), "localhost");
        assertEquals( AppPropertiesLookup.get( "auth.port"), "9000");
        assertEquals( AppPropertiesLookup.get( "auth.host.authorization"), "localhost");
        assertEquals( AppPropertiesLookup.get( "eureka.host"), "localhost");
        assertEquals( AppPropertiesLookup.get( "eureka.port"), "8761");
    }

    @Test
    public void log4j2lookup() throws UnknownHostException {

        AppPropertiesLookup lookup = new AppPropertiesLookup();

        assertEquals( lookup.lookup( "name"), "null");
        assertEquals( lookup.lookup( "host"), getLocalHost().getHostName());
        assertEquals( lookup.lookup( "port"), "null");

        assertEquals( lookup.lookup( "log.dir"), "c:/logs");
        assertEquals( lookup.lookup( "log.file"), "oauth2.log");
        assertEquals( lookup.lookup( "log.level"), "INFO");
        assertEquals( lookup.lookup( "log.error.file"), "oauth2_error.log");
        assertEquals( lookup.lookup( "kafka.host"), "localhost");
        assertEquals( lookup.lookup( "kafka.port"), "9092");
        assertEquals( lookup.lookup( "zoo.host"), "localhost");
        assertEquals( lookup.lookup( "zoo.port"), "2181");
        assertEquals( lookup.lookup( "config.server.host"), "localhost");
        assertEquals( lookup.lookup( "config.server.port"), "8888");
        assertEquals( lookup.lookup( "auth.host"), "localhost");
        assertEquals( lookup.lookup( "auth.port"), "9000");
        assertEquals( lookup.lookup( "auth.host.authorization"), "localhost");
        assertEquals( lookup.lookup( "eureka.host"), "localhost");
        assertEquals( lookup.lookup( "eureka.port"), "8761");
    }

    @Test
    public void appEnvoirement() {
        assertEquals( System.getProperties().get( "kafka.host"), "localhost");
        assertEquals( System.getProperties().get( "kafka.port"), "9092");
        assertEquals( System.getProperties().get( "zoo.host"), "localhost");
        assertEquals( System.getProperties().get( "zoo.port"), "2181");
        assertEquals( System.getProperties().get( "config.server.host"), "localhost");
        assertEquals( System.getProperties().get( "config.server.port"), "8888");
        assertEquals( System.getProperties().get( "auth.host"), "localhost");
        assertEquals( System.getProperties().get( "auth.port"), "9000");
        assertEquals( System.getProperties().get( "auth.host.authorization"), "localhost");
        assertEquals( System.getProperties().get( "eureka.host"), "localhost");
        assertEquals( System.getProperties().get( "eureka.port"), "8761");
    }
}
