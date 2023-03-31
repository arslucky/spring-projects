package org.demo.ars.commons.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author arsen.ibragimov
 *
 */
@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger log = LoggerFactory.getLogger( ContextRefreshedListener.class);

    @Autowired
    Environment env;

    @Override
    public void onApplicationEvent( ContextRefreshedEvent event) {
        log.info( "ContextRefreshedEvent");
        ListenerUtils.updateLogLevel( env, getClass().getSimpleName());
    }

}
