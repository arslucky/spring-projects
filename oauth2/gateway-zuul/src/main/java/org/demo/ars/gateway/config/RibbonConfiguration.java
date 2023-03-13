package org.demo.ars.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;

public class RibbonConfiguration {

    Logger logger = LoggerFactory.getLogger( getClass());


    @Bean
    public IPing ribbonPing( final IClientConfig config) {
        logger.info( "ribbonPing");
        return new PingUrl( false, "/health");
    }

    @Bean
    public IRule ribbonRule( final IClientConfig config) {
        return new AvailabilityFilteringRule();
    }

}
