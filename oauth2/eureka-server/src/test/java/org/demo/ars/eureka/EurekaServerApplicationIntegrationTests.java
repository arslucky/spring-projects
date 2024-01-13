package org.demo.ars.eureka;

import static java.net.InetAddress.getLocalHost;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.UnknownHostException;

import org.demo.ars.commons.AppPropertiesLookup;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;

/**
 * Integration tests without connection to external services
 *
 * @author arsen.ibragimov
 *
 */
// @EnabledIf( "#{${integration-tests:false} || ${ms-integration-tests:false}}")
// @formatter:off
@EnabledIfSystemProperty( named = "group-tests", matches = "integration-tests|ms-integration-tests")
// @formatter:on
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
class EurekaServerApplicationIntegrationTests {

    @Autowired
    Environment env;

    @Test
    public void settings() throws UnknownHostException {

        assertEquals( AppPropertiesLookup.get( "name"), "eureka-server");
        assertEquals( AppPropertiesLookup.get( "name"), env.getProperty( "spring.application.name"));
        assertEquals( AppPropertiesLookup.get( "host"), getLocalHost().getHostName());
    }

}
