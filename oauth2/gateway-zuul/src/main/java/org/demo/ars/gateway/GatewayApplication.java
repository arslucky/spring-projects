package org.demo.ars.gateway;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.demo.ars.gateway.config.RibbonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
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
@RibbonClients( defaultConfiguration = RibbonConfiguration.class)
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

    @RequestMapping( path = "/user", method = { RequestMethod.POST })
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
