package org.demo.ars.gateway.filter;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * @author arsen.ibragimov
 *
 */
@Component
public class CustomPreZuulFilter extends ZuulFilter {

    Logger log = LoggerFactory.getLogger( getClass());

    private static final String TRACE_ID = "trace-id";

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpSession session = request.getSession();

        if( session == null) {
            return null;
        }

        String traceId = (String) session.getAttribute( TRACE_ID);

        if( traceId == null) {
            traceId = UUID.randomUUID().toString();
            session.setAttribute( TRACE_ID, traceId);
            MDC.put( TRACE_ID, traceId);
        }

        ctx.addZuulRequestHeader( TRACE_ID, traceId);

        log.info( String.format( "traceId: %s", traceId));

        return null;
    }

}
