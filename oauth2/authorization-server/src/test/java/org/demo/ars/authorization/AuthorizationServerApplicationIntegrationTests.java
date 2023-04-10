package org.demo.ars.authorization;

import static java.net.InetAddress.getLocalHost;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.util.Base64;

import org.demo.ars.commons.AppPropertiesLookup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Integration tests without connection to external services
 *
 * @author arsen.ibragimov
 *
 */
@IfProfileValue( name = "group-tests", values = { "integration-tests", "ms-integration-tests" })
@RunWith( SpringRunner.class)
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthorizationServerApplicationIntegrationTests {

    private static String path = "http://localhost";

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
        assertEquals( AppPropertiesLookup.get( "port"), env.getProperty( "auth.port"));
    }

    @Test
    public void homePageProtected() {
        ResponseEntity<String> response = template.getForEntity( String.format( "%s:%s/uaa/", path, port), String.class);
        assertEquals( HttpStatus.FOUND, response.getStatusCode());
        String location = response.getHeaders().getFirst( "Location");
        assertEquals( String.format( "%s:%s/uaa/login", path, port), location);
    }

    @Test
    public void userEndpointProtected() {
        ResponseEntity<String> response = template.getForEntity( String.format( "%s:%s/uaa/user", path, port), String.class);
        assertEquals( HttpStatus.UNAUTHORIZED, response.getStatusCode());
        String auth = response.getHeaders().getFirst( "WWW-Authenticate");
        assertTrue( auth.startsWith( "Bearer realm=\""));
    }

    @Test
    public void authorizationRedirects() {
        ResponseEntity<String> response = template.getForEntity( String.format( "%s:%s/uaa/oauth/authorize", path, port), String.class);
        assertEquals( HttpStatus.FOUND, response.getStatusCode());
        String location = response.getHeaders().getFirst( "Location");
        assertEquals( String.format( "%s:%s/uaa/login", path, port), location);
    }

    @Test
    public void getTokenUnAuthorized() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add( "grant_type", "client_credentials");
        HttpEntity<Object> entity = new HttpEntity<>( body);

        ResponseEntity<String> response = template.postForEntity( String.format( "%s:%s/uaa/oauth/token", path, port), entity, String.class);
        assertEquals( HttpStatus.UNAUTHORIZED, response.getStatusCode());
        String auth = response.getHeaders().getFirst( "WWW-Authenticate");
        assertTrue( auth.startsWith( "Basic realm=\""));
    }

    @Test
    public void getTokenAuthorized() {
        HttpHeaders headers = createHttpHeaders( "trust_client", "password");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add( "grant_type", "client_credentials");

        HttpEntity<Object> entity = new HttpEntity<>( body, headers);

        ResponseEntity<String> response = template.postForEntity( String.format( "%s:%s/uaa/oauth/token", path, port), entity, String.class);
        assertEquals( HttpStatus.OK, response.getStatusCode());
        String auth = response.getBody();
        assertTrue( auth.contains( "\"token_type\":\"bearer\""));
    }

    private HttpHeaders createHttpHeaders( String user, String password) {
        String notEncoded = user + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString( notEncoded.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_FORM_URLENCODED);
        headers.add( "Authorization", "Basic " + encodedAuth);
        return headers;
    }
}
