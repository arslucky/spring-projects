package org.demo.ars.authorization.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author arsen.ibragimov
 *
 */
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${redirect.host}")
    private String redirectHost;

    @Value("${redirect.port}")
    private String redirectPort;

    @Override
    public void configure( AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager( authenticationManager); // grant type: password

        endpoints.addInterceptor( new HandlerInterceptorAdapter() {
            @Override
            public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                if( modelAndView != null
                        && modelAndView.getView() instanceof RedirectView) {
                    RedirectView redirect = (RedirectView) modelAndView.getView();
                    String url = redirect.getUrl();
                    if( url.contains( "code=") || url.contains( "error=")) {
                        HttpSession session = request.getSession( false);
                        if( session != null) {
                            session.invalidate();
                        }
                    }
                }
            }
        });

    }

    @Override
    public void configure( AuthorizationServerSecurityConfigurer security) {
        security.checkTokenAccess( "hasAuthority('TRUST_CLIENT')")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure( ClientDetailsServiceConfigurer clients) throws Exception {
        // @formatter:off
        clients.inMemory()
                    .withClient( "ui_client")
                    .secret( "password")
                    .authorizedGrantTypes( "authorization_code", "refresh_token")
                    .scopes( "read", "write")
                    .authorities( "ROLE_UI_CLIENT")
                    .resourceIds( "oauth2-resource")
//                    .accessTokenValiditySeconds( 30)
                    .autoApprove( true)
                    .redirectUris( String.format("http://%s:%s/login", redirectHost, redirectPort))
                .and()
                    .withClient( "trust_client")
                    .secret( "password")
                    .authorizedGrantTypes( "authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                    .scopes( "read", "write")
                    .authorities( "TRUST_CLIENT");
        // @formatter:on
    }

}
