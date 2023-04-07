package org.demo.ars.gateway;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration tests with real connection to external config-server
 *
 * @author arsen.ibragimov
 *
 */
@IfProfileValue( name = "group-tests", values = { "ms-integration-tests" })
@RunWith( SpringRunner.class)
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
public class GatewayApplicationMicroserviceTests {

    @Autowired
    Environment env;

    @Test
    public void checkRemote() {
        assertEquals( env.getProperty( "verification"), "ok");
    }
}
