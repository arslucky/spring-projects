package org.demo.ars.log;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

/**
 * Unit tests
 *
 * @author arsen.ibragimov
 *
 */
class LogServerApplicationUnitTests {


    @Test
    public void settings() throws UnknownHostException {

        assertEquals( System.getProperty( "kafka.log.file"), "oauth2_server.log");
    }
}
