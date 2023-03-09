package org.demo.ars.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /*
     * @Autowired
     * private DiscoveryClient discoveryClient;
     *
     * @Autowired
     * private ApplicationInfoManager applicationInfoManager;
     */

    @CrossOrigin( origins = "http://localhost:8091")
    @GetMapping( path = "/hi")
    String hi() {
        return "Hello!";
    }

    // @CrossOrigin( origins = "http://localhost:8091", allowedHeaders = { "x-auth-token", "x-requested-with", "x-xsrf-token", "Authorization", "Access-Control-Allow-Origin" }, methods = {
    // RequestMethod.OPTIONS, RequestMethod.GET })
    @CrossOrigin
    @GetMapping( path = "/getInstanceId")
    String getInstanceId() {
        log.info( "getInstanceId");
        // String instId = applicationInfoManager.getInfo().getId();
        // List<ServiceInstance> services = this.discoveryClient.getInstances( env.getProperty( "spring.application.name"));
        return "ok";
        /*
         * return String.format( "instance: %s, instances[count: %s, ids:%s]",
         * instId,
         * services.size(),
         * services.stream().map( s -> s.getInstanceId()).collect( Collectors.joining( ",")));
         */
    }

    @CrossOrigin
    @GetMapping( "/getStatistic")
    String getStatistic() {
        return "Statistic";
    }
}
