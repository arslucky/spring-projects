package org.demo.ars.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.Base64;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Integration tests with real connection to external authorization-server
 *
 * @author arsen.ibragimov
 *
 */
@IfProfileValue( name = "group-tests", values = { "ms-integration-tests" })
@RunWith( SpringRunner.class)
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
public class ResourceApplicationMicroserviceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    private static String path = "http://localhost";

    // --------------------------------- getStatistic ---------------------------------------------------//
    @Test
    public void getStatisticWithClientToken() throws JsonProcessingException, IOException {
        String accessToken = getToken();
        HttpHeaders headers = createHttpBearerHeaders( accessToken);
        HttpEntity<Object> entity = new HttpEntity<>( headers);

        ResponseEntity<String> response = template.exchange( String.format( "%s:%s/getStatistic", path, port), HttpMethod.GET, entity, String.class);
        assertEquals( HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getStatisticPostWithClientToken() throws JsonProcessingException, IOException {
        String accessToken = getToken();
        HttpHeaders headers = createHttpBearerHeaders( accessToken);
        HttpEntity<Object> entity = new HttpEntity<>( headers);

        ResponseEntity<String> response = template.postForEntity( String.format( "%s:%s/getStatistic", path, port), entity, String.class);
        assertEquals( HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getStatisticPostWithUserToken() throws JsonProcessingException, IOException {
        String accessToken = getToken( "user");
        HttpHeaders headers = createHttpBearerHeaders( accessToken);
        HttpEntity<Object> entity = new HttpEntity<>( headers);

        ResponseEntity<String> response = template.postForEntity( String.format( "%s:%s/getStatistic", path, port), entity, String.class);
        assertEquals( HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getStatisticPostWithUserTokenCsrf() throws JsonProcessingException, IOException {
        String accessToken = getToken( "user");
        HttpHeaders headers = createHttpBearerHeaders( accessToken);
        headers = addCsrf( headers);
        HttpEntity<Object> entity = new HttpEntity<>( headers);

        ResponseEntity<String> response = template.postForEntity( String.format( "%s:%s/getStatistic", path, port), entity, String.class);
        assertEquals( HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getStatisticPostWithAdminToken() throws JsonProcessingException, IOException {
        String accessToken = getToken( "admin");
        HttpHeaders headers = createHttpBearerHeaders( accessToken);
        HttpEntity<Object> entity = new HttpEntity<>( headers);

        ResponseEntity<String> response = template.postForEntity( String.format( "%s:%s/getStatistic", path, port), entity, String.class);
        assertEquals( HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getStatisticPostWithAdminTokenCsrf() throws JsonProcessingException, IOException {
        String accessToken = getToken( "admin");
        HttpHeaders headers = createHttpBearerHeaders( accessToken);
        headers = addCsrf( headers);
        HttpEntity<Object> entity = new HttpEntity<>( headers);

        ResponseEntity<String> response = template.postForEntity( String.format( "%s:%s/getStatistic", path, port), entity, String.class);
        assertEquals( HttpStatus.OK, response.getStatusCode());
    }

    // ----------------------------------- getInstanceId ------------------------------------------------//
    @Test
    public void getInstanceIdWithClientToken() throws JsonProcessingException, IOException {
        String accessToken = getToken();
        HttpHeaders headers = createHttpBearerHeaders( accessToken);
        HttpEntity<Object> entity = new HttpEntity<>( headers);

        ResponseEntity<String> response = template.exchange( String.format( "%s:%s/getInstanceId", path, port), HttpMethod.GET, entity, String.class);
        assertEquals( HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void getInstanceIdWithUserToken() throws JsonProcessingException, IOException {
        String accessToken = getToken( "user");
        HttpHeaders headers = createHttpBearerHeaders( accessToken);
        HttpEntity<Object> entity = new HttpEntity<>( headers);

        ResponseEntity<String> response = template.exchange( String.format( "%s:%s/getInstanceId", path, port), HttpMethod.GET, entity, String.class);
        assertEquals( HttpStatus.OK, response.getStatusCode());
    }

    // ------------------------------------- helpers --------------------------------------------------//

    private HttpHeaders createHttpBasicHeaders( String user, String password) {
        String notEncoded = user + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString( notEncoded.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_FORM_URLENCODED);
        headers.add( "Authorization", "Basic " + encodedAuth);
        return headers;
    }

    private HttpHeaders createHttpBearerHeaders( String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON);
        headers.add( "Authorization", String.format( "Bearer %s", accessToken));
        return headers;
    }

    private String getToken() throws JsonProcessingException, IOException {
        return getToken( null);
    }

    private String getToken( String user) throws JsonProcessingException, IOException {
        HttpHeaders headers = createHttpBasicHeaders( "trust_client", "password");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        if( user == null) {
            body.add( "grant_type", "client_credentials");
        } else {
            body.add( "grant_type", "password");
            body.add( "username", user);
            body.add( "password", "password");
        }
        HttpEntity<Object> entity = new HttpEntity<>( body, headers);

        String url = String.format( "http://%s:%s/uaa/oauth/token", System.getProperty( "auth.host"), System.getProperty( "auth.port"));
        ResponseEntity<String> response = template.postForEntity( url, entity, String.class);

        assertEquals( HttpStatus.OK, response.getStatusCode());

        String resp = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree( resp);
        String accessToken = root.get( "access_token").asText();

        assertNotNull( accessToken);

        return accessToken;
    }

    private HttpHeaders addCsrf( HttpHeaders headers) {
        String uuid = UUID.randomUUID().toString();
        HttpCookie cookie = new HttpCookie( "XSRF-TOKEN", uuid);

        headers.add( "X-XSRF-TOKEN", uuid);
        headers.add( "Cookie", cookie.toString());

        return headers;
    }

}
