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

        assertEquals( AppPropertiesLookup.get( "log.file"), "oauth2.log");
        assertEquals( AppPropertiesLookup.get( "log.level"), "INFO");
        assertEquals( AppPropertiesLookup.get( "log.error.file"), "oauth2_error.log");
    }

    @Test
    public void log4j2lookup() throws UnknownHostException {

        AppPropertiesLookup lookup = new AppPropertiesLookup();

        assertEquals( lookup.lookup( "name"), "null");
        assertEquals( lookup.lookup( "host"), getLocalHost().getHostName());
        assertEquals( lookup.lookup( "port"), "null");

        assertEquals( lookup.lookup( "log.file"), "oauth2.log");
        assertEquals( lookup.lookup( "log.level"), "INFO");
        assertEquals( lookup.lookup( "log.error.file"), "oauth2_error.log");
    }

}
