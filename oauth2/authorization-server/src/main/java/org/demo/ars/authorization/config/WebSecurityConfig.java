package org.demo.ars.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author arsen.ibragimov
 *
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        UserBuilder user = User.withUsername( "user").password( "password").roles( "USER");
        UserBuilder admin = User.withUsername( "admin").password( "password").roles( "USER", "ADMIN");

        InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
        detailsManager.createUser( user.build());
        detailsManager.createUser( admin.build());

        return detailsManager;
    }

    @Override
    protected void configure( HttpSecurity http) throws Exception {

        // @formatter:off
        http
            .requestMatchers().antMatchers( "/", "/login", "/oauth/authorize", "/test")
            .and()
                .authorizeRequests()
                    .antMatchers( "/").authenticated()
                    .antMatchers( "/login").permitAll()
                    .antMatchers( "/oauth/authorize").authenticated()
                    .antMatchers( "/test").authenticated()
            .and()
                .httpBasic()
            .and()
                .formLogin().permitAll()
            .and()
                .csrf().disable()
            ;
        // @formatter:on
    }

}
