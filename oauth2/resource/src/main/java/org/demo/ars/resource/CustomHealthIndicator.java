package org.demo.ars.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * @author arsen.ibragimov
 *         works when eureka.client.healthcheck.enabled: true
 */
@Component
public class CustomHealthIndicator implements HealthIndicator {

    Logger logger = LoggerFactory.getLogger( this.getClass());

    int i = 0;

    @Override
    public Health health() {
        Health status = Health.up().build();
        /*
         * if (i++ >= 2) {
         * status = Health.down().build();
         * }
         */
        logger.info( String.format( "heartbeat: %s", status.getStatus()));
        return status;
    }
}
