package org.demo.ars.resource;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.ApplicationInfoManager;

//import com.netflix.appinfo.ApplicationInfoManager;

/**
 * @author arsen.ibragimov
 *
 */
@RestController
public class ResourceController {

    Logger log = LoggerFactory.getLogger( getClass());

    @Autowired
    Environment env;

    @Autowired
    HttpServletRequest request;

    // @Autowired
    // private DiscoveryClient discoveryClient;

    @Autowired
    private ApplicationInfoManager applicationInfoManager;


    @GetMapping( path = "/hi")
    String hi() {
        return "Hello!";
    }

    @GetMapping( path = "/getInstanceId")
    String getInstanceId() {
        log.info( "getInstanceId");
        String instId = applicationInfoManager.getInfo().getId();
        return instId;
        /*
         * List<ServiceInstance> services = this.discoveryClient.getInstances( env.getProperty( "spring.application.name"));
         * return String.format( "instance: %s, instances[count: %s, ids:%s]",
         * instId,
         * services.size(),
         * services.stream().map( s -> s.getInstanceId()).collect( Collectors.joining( ",")));
         */
    }

    @RequestMapping( path = "/getStatistic", method = { RequestMethod.GET, RequestMethod.POST })
    String getStatistic() {
        return "Statistic, method:" + request.getMethod();
    }

}
