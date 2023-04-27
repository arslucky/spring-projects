package org.demo.ars.ui.controller;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.ApplicationInfoManager;

/**
 * @author arsen.ibragimov
 *
 */
@RestController
public class AppRestController {
    Logger log = LoggerFactory.getLogger( getClass());

    @Autowired
    private ApplicationInfoManager applicationInfoManager;

    private Environment env;

    public AppRestController( Environment env) {
        this.env = env;
    }

    @RequestMapping( path = "/getDate", method = { RequestMethod.GET, RequestMethod.POST })
    String getDate() {
        log.info( "getDate");
        return Calendar.getInstance().getTime().toString();
    }

    @GetMapping( path = "/getInstanceId")
    String getInstanceId() {
        log.info( "getInstanceId");
        String instId = applicationInfoManager.getInfo().getId();
        return instId;
    }

    @RequestMapping( path = "/getLogLevel", method = { RequestMethod.GET, RequestMethod.POST })
    String getLogLevel() {
        log.info( "getLogLevel");
        return env.getProperty( "LOG_LEVEL");
    }
}
