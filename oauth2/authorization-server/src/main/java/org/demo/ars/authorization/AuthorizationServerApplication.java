package org.demo.ars.authorization;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author arsen.ibragimov
 *
 */
@RestController
@SpringBootApplication( scanBasePackages = { "org.demo.ars.authorization", "org.demo.ars.commons" })
public class AuthorizationServerApplication {

    private Logger logger = LoggerFactory.getLogger( getClass());

    @RequestMapping( "/user")
    public Principal user( Principal user) {
        logger.info( "get user");
        return user;
    }

    public static void main( String[] args) {
        SpringApplication.run( AuthorizationServerApplication.class, args);
    }

}
