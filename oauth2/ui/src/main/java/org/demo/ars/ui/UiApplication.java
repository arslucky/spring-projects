package org.demo.ars.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author arsen.ibragimov
 *
 */
@EnableResourceServer
@RestController
@EnableDiscoveryClient
@SpringBootApplication( scanBasePackages = { "org.demo.ars.ui", "org.demo.ars.commons" })
public class UiApplication {

    public static void main( String[] args) {
        SpringApplication.run( UiApplication.class, args);
	}

}
