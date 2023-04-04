package org.demo.ars.config;

import static java.net.InetAddress.getLocalHost;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.UnknownHostException;

import org.demo.ars.commons.AppPropertiesLookup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;

@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
public class ConfigServerApplicationTests {

    @Autowired
    Environment env;

    @Test
    public void settings() throws UnknownHostException {

        assertEquals( AppPropertiesLookup.get( "name"), "config-server");
        assertEquals( AppPropertiesLookup.get( "name"), env.getProperty( "spring.application.name"));
        assertEquals( AppPropertiesLookup.get( "host"), getLocalHost().getHostName());
        assertEquals( AppPropertiesLookup.get( "config.server.port"), env.getProperty( "config.server.port"));

        assertEquals( env.getProperty( "spring.cloud.config.server.git.default-label"), "main");
        assertEquals( env.getProperty( "spring.cloud.config.server.git.uri"), "https://github.com/arslucky/properties");
    }
}
