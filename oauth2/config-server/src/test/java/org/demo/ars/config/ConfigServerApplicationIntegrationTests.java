package org.demo.ars.config;

import static java.net.InetAddress.getLocalHost;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.UnknownHostException;

import org.demo.ars.commons.AppPropertiesLookup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit.jupiter.EnabledIf;

/**
 * Integration tests without connection to external services
 *
 * @author arsen.ibragimov
 *
 */
//@formatter:off
//@EnabledIf( "#{systemProperties['group-tests'] != null "
//        + "and (systemProperties['group-tests'].toLowerCase().contains('integration-tests')"
//                + "or systemProperties['group-tests'].toLowerCase().contains('ms-integration-tests'))}")
//@formatter:on
@EnabledIfSystemProperty( named = "group-tests", matches = "integration-tests|ms-integration-tests")
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
public class ConfigServerApplicationIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private Environment env;

    private static String path = "http://localhost";

    @Test
    public void settings() throws UnknownHostException {

        assertEquals( AppPropertiesLookup.get( "name"), env.getProperty( "spring.application.name"));
        assertEquals( AppPropertiesLookup.get( "host"), getLocalHost().getHostName());

        assertEquals( env.getProperty( "spring.cloud.config.server.git.default-label"), "main");
        assertEquals( env.getProperty( "spring.cloud.config.server.git.uri"), "https://github.com/arslucky/properties");
    }

    @Test
    public void actuator() {
        ResponseEntity<String> response = template.getForEntity( String.format( "%s:%s/actuator/health", path, port), String.class);
        assertEquals( HttpStatus.OK, response.getStatusCode());

        response = template.getForEntity( String.format( "%s:%s/actuator/info", path, port), String.class);
        assertEquals( HttpStatus.OK, response.getStatusCode());

        response = template.getForEntity( String.format( "%s:%s/actuator/refresh", path, port), String.class);
        assertEquals( HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());

        response = template.postForEntity( String.format( "%s:%s/actuator/refresh", path, port), null, String.class);
        assertEquals( HttpStatus.OK, response.getStatusCode());

        response = template.getForEntity( String.format( "%s:%s/actuator/busrefresh", path, port), String.class);
        assertEquals( HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());

        response = template.postForEntity( String.format( "%s:%s/actuator/busrefresh", path, port), null, String.class);
        assertEquals( HttpStatus.NO_CONTENT, response.getStatusCode());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>( "{\"name\":\"LOG_LEVEL\",\"value\":\"DEBUG\"}", headers);
        response = template.postForEntity( String.format( "%s:%s/actuator/busenv", path, port), entity, String.class);
        assertEquals( HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
