package org.demo.ars.ui;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author arsen.ibragimov
 *
 */
@RestController
@SpringBootApplication
public class UiApplication {

    Logger logger = LoggerFactory.getLogger( getClass());

	public static void main(String[] args) {
        System.out.println( "ok");
		SpringApplication.run(UiApplication.class, args);
	}

    @RequestMapping( "/user")
    public Map<String, String> user( Principal user) {
        if( user != null) {
            Map<String, String> details = new HashMap<>( 2);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            details.put( "name", user.getName());
            details.put( "role", authentication.getAuthorities().stream().map( a -> a.getAuthority()).collect( Collectors.joining( ",")));

            return details;
        }
        return Collections.emptyMap();
    }

    @RequestMapping( path = "/token", method = { RequestMethod.POST })
    String getToken() {
        String token = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getDetails();
        if( details instanceof OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) details;
            token = oAuth2AuthenticationDetails.getTokenValue();
        }
        return token;
    }
}
