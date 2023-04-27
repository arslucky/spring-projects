package org.demo.ars.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.UnknownHostException;

import org.demo.ars.commons.AppPropertiesLookup;
import org.junit.jupiter.api.Test;

/**
 * Integration tests without connection to external services
 *
 * @author arsen.ibragimov
 *
 */
public class ConfigServerApplicationUnitTests {

    @Test
    public void settings() throws UnknownHostException {

        assertEquals( AppPropertiesLookup.get( "name"), "config-server");
    }
}
