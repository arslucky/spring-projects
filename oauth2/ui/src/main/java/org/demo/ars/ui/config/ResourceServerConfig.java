package org.demo.ars.ui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
            .authorizeRequests()
                .antMatchers( HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers( "/getInstanceId").hasAnyRole( "USER", "ADMIN")
                .antMatchers( "/getStatistic").hasAnyRole( "ADMIN")
                .anyRequest().authenticated()
                //access( "#oauth2.hasScope('read')")
                ;
        // @formatter:on
    }

}
