package org.demo.ars.commons.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author arsen.ibragimov
 *
 */
@Component
public class RefreshScopeRefresheListener implements ApplicationListener<RefreshScopeRefreshedEvent> {

    private static Logger log = LoggerFactory.getLogger( RefreshScopeRefresheListener.class);

    @Autowired
    Environment env;

    @Override
    public void onApplicationEvent( RefreshScopeRefreshedEvent event) {
        log.info( "RefreshScopeRefreshedEvent");
        ListenerUtils.updateLogLevel( env, getClass().getSimpleName());
    }

}
