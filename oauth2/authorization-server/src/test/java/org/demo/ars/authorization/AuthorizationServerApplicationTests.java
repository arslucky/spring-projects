package org.demo.ars.authorization;

import static java.net.InetAddress.getLocalHost;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;

import org.demo.ars.commons.AppPropertiesLookup;
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
import org.springframework.test.context.junit4.SpringRunner;

@RunWith( SpringRunner.class)
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthorizationServerApplicationTests {

    @Autowired
    Environment env;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void settings() throws UnknownHostException {

        assertEquals( AppPropertiesLookup.get( "name"), "authorization-server");
        assertEquals( AppPropertiesLookup.get( "name"), env.getProperty( "spring.application.name"));
        assertEquals( AppPropertiesLookup.get( "host"), getLocalHost().getHostName());
        assertEquals( AppPropertiesLookup.get( "port"), env.getProperty( "port"));
    }

    @Test
    public void homePageProtected() {
        ResponseEntity<String> response = template.getForEntity( String.format( "http://localhost:%s/uaa/", port), String.class);
        assertEquals( HttpStatus.FOUND, response.getStatusCode());
        String location = response.getHeaders().getFirst( "Location");
        assertEquals( String.format( "http://localhost:%s/uaa/login", port), location);
    }

    @Test
    public void userEndpointProtected() {
        ResponseEntity<String> response = template.getForEntity( String.format( "http://localhost:%s/uaa/user", port), String.class);
        assertEquals( HttpStatus.UNAUTHORIZED, response.getStatusCode());
        String auth = response.getHeaders().getFirst( "WWW-Authenticate");
        assertTrue( auth, auth.startsWith( "Bearer realm=\""));
    }

    @Test
    public void authorizationRedirects() {
        ResponseEntity<String> response = template.getForEntity( String.format( "http://localhost:%s/uaa/oauth/authorize", port), String.class);
        assertEquals( HttpStatus.FOUND, response.getStatusCode());
        String location = response.getHeaders().getFirst( "Location");
        assertEquals( String.format( "http://localhost:%s/uaa/login", port), location);
    }

}
