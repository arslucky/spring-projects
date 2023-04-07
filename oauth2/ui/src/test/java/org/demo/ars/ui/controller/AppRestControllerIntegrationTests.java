package org.demo.ars.ui.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration tests without connection to external services
 *
 * @author arsen.ibragimov
 *
 */
@IfProfileValue( name = "group-tests", values = { "integration-tests", "ms-integration-tests" })
@RunWith( SpringRunner.class)
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
public class AppRestControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    Environment env;

    @Autowired
    private TestRestTemplate template;

    private static String path = "http://localhost";

    @Test
    public void getDate() {
        ResponseEntity<String> response = template.getForEntity( String.format( "%s:%s/getDate", path, port), String.class);
        assertEquals( HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void getDatePost() {
        ResponseEntity<String> response = template.postForEntity( String.format( "%s:%s/getDate", path, port), null, String.class);
        assertEquals( HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getInstanceId() {
        ResponseEntity<String> response = template.getForEntity( String.format( "%s:%s/getInstanceId", path, port), String.class);
        assertEquals( HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void getLogLevel() {
        ResponseEntity<String> response = template.getForEntity( String.format( "%s:%s/getLogLevel", path, port), String.class);
        assertEquals( HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void getLogLevelPost() {
        ResponseEntity<String> response = template.postForEntity( String.format( "%s:%s/getLogLevel", path, port), null, String.class);
        assertEquals( HttpStatus.FORBIDDEN, response.getStatusCode());
    }

}
