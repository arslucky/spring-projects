package org.demo.ars.ui.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author arsen.ibragimov
 *
 */
@Component
public class CustomOncePerRequestFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "trace-id";

    @Override
    protected void doFilterInternal( HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String traceId = request.getHeader( TRACE_ID);

        if( traceId != null) {
            MDC.put( TRACE_ID, traceId);
            logger.info( "headerParam: " + traceId);
        }

        filterChain.doFilter( request, response);
    }
}
