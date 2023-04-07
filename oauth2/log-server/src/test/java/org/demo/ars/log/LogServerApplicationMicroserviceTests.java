package org.demo.ars.log;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.EnabledIf;

/**
 * Integration tests with real connection to external config-server
 *
 * @author arsen.ibragimov
 *
 */
//@formatter:off
@EnabledIf( "#{systemProperties['group-tests'] != null "
             + "and systemProperties['group-tests'].toLowerCase().contains('ms-integration-tests')}")
//@formatter:on
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
class LogServerApplicationMicroserviceTests {


    @Autowired
    private Environment env;

    @Test
    public void checkRemote() {
        assertEquals( env.getProperty( "verification"), "ok");
    }
}
