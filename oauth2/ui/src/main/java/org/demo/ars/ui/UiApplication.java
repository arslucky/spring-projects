package org.demo.ars.ui;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.ApplicationInfoManager;

/**
 * @author arsen.ibragimov
 *
 */
@EnableResourceServer
@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class UiApplication {

    Logger logger = LoggerFactory.getLogger( getClass());

    @Autowired
    private ApplicationInfoManager applicationInfoManager;

    public static void main( String[] args) {
        SpringApplication.run( UiApplication.class, args);
	}

    @RequestMapping( path = "/getDate", method = { RequestMethod.GET, RequestMethod.POST })
    String getDate() {
        return Calendar.getInstance().getTime().toString();
    }

    @GetMapping( path = "/getInstanceId")
    String getInstanceId() {
        String instId = applicationInfoManager.getInfo().getId();
        return instId;
    }
}
