package org.demo.ars.log;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.UnknownHostException;

import org.demo.ars.commons.AppPropertiesLookup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit tests
 *
 * @author arsen.ibragimov
 *
 */
@Disabled
class LogServerApplicationUnitTests {

    @BeforeAll
    public static void init() {
        AppPropertiesLookup.init();
    }
    @Test
    public void settings() throws UnknownHostException {

        assertEquals( System.getProperty( "KAFKA_LOG_FILE"), "oauth2_server.log");
    }
}
