package org.demo.ars.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author arsen.ibragimov
 *
 */
@EnableConfigServer
@SpringBootApplication( scanBasePackages = { "org.demo.ars.config", "org.demo.ars.commons" })
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
