package org.demo.ars.authorization.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author arsen.ibragimov
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure( HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .antMatcher( "/user")
            .authorizeRequests()
                .antMatchers( "/user").access( "#oauth2.hasScope('read')");

        http.authorizeRequests().anyRequest().authenticated();
        // @formatter:on
    }

}
