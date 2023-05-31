package org.demo.ars.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

/**
 * @author arsen.ibragimov
 *
 */
@EnableOAuth2Sso
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    Logger log = LoggerFactory.getLogger( getClass());

    @Override
    public void configure( HttpSecurity http) throws Exception {

        // @formatter:off
        http
            .authorizeRequests()
                .anyRequest().authenticated()
            .and()
            .logout()
                .logoutUrl( "/logout")
                .logoutSuccessUrl( "/")
            .and()
            .csrf()
                .csrfTokenRepository( this.getCsrfTokenRepository())
            ;
        // @formatter:on
    }

    private CsrfTokenRepository getCsrfTokenRepository() {
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        tokenRepository.setCookiePath( "/");
        return tokenRepository;
    }
}
