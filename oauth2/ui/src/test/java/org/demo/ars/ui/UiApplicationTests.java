package org.demo.ars.ui;

import static java.net.InetAddress.getLocalHost;
import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;

import org.demo.ars.commons.AppPropertiesLookup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author arsen.ibragimov
 *
 */
@RunWith( SpringRunner.class)
@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
public class UiApplicationTests {

    @Autowired
    Environment env;

    @Test
    public void settings() throws UnknownHostException {

        assertEquals( AppPropertiesLookup.get( "name"), "ui");
        assertEquals( AppPropertiesLookup.get( "name"), env.getProperty( "spring.application.name"));
        assertEquals( AppPropertiesLookup.get( "host"), getLocalHost().getHostName());
        assertEquals( env.getProperty( "server.port"), "0");
    }

}
