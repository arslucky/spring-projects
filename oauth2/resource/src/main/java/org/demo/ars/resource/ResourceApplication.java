package org.demo.ars.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.stereotype.Component;

/**
 * @author arsen.ibragimov
 *
 */
@EnableResourceServer
@SpringBootApplication
public class ResourceApplication {

    Logger log = LoggerFactory.getLogger( this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}

    @Component
    static class ShutDownBean implements DisposableBean {

        Logger log = LoggerFactory.getLogger( this.getClass());

        @Override
        public void destroy() throws Exception {
            log.info( "Shutting down application");
        }
    }
}
