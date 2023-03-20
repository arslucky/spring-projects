package org.demo.ars.resource.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * @author arsen.ibragimov
 *
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    Logger log = LoggerFactory.getLogger( getClass());

    @Override
    public void configure( HttpSecurity http) throws Exception {

        // @formatter:off
        http
            .antMatcher( "/")
            .authorizeRequests()
                .antMatchers( "/").permitAll()
            .and()
            .httpBasic().disable()
            .csrf()
                .csrfTokenRepository( CookieCsrfTokenRepository.withHttpOnlyFalse())
            ;
        // @formatter:on
    }

}
