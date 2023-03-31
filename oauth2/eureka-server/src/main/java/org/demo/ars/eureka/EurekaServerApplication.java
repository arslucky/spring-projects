package org.demo.ars.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author arsen.ibragimov
 *
 */
@EnableEurekaServer
@SpringBootApplication( scanBasePackages = { "org.demo.ars.eureka", "org.demo.ars.commons" })
public class EurekaServerApplication {

    public static void main( String[] args) {
        SpringApplication.run( EurekaServerApplication.class, args);
    }

}
