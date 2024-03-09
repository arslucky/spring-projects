package org.demo.ars.commons;

import static java.net.InetAddress.getLocalHost;
import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;
import java.util.Properties;

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
    }

    @Test
    public void log4j2lookup() throws UnknownHostException {

        AppPropertiesLookup lookup = new AppPropertiesLookup();

        assertEquals( lookup.lookup( "name"), "null");
        assertEquals( lookup.lookup( "host"), getLocalHost().getHostName());
        assertEquals( lookup.lookup( "port"), "null");
    }

    @Test
    public void checkGetValue() {
        Properties prop = new Properties();

        prop.put("server.port", "${app.port:0}");
        prop.put("app.port", "9000");
        assertEquals(AppPropertiesLookup.getValue("server.port", prop), "9000");

        prop.clear();
        prop.put("server.port", "8000");
        assertEquals(AppPropertiesLookup.getValue("server.port", prop), "8000");

        prop.clear();
        prop.put("server.port", "${app.port}");
        assertEquals(AppPropertiesLookup.getValue("server.port", prop), null);
    }
}
