package rs.marko.lalic.safe.core.audit;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import rs.marko.lalic.safe.core.Utils;
import rs.marko.lalic.safe.core.constants.Event;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Interceptor for Audit Events
 */
public class AuditInterceptor extends HandlerInterceptorAdapter {

    /**
     * Class logger
     */
    private static final Logger LOGGER = Logger.getLogger(AuditInterceptor.class);

    /**
     * Empty constructor
     */
    public AuditInterceptor() {
    }

    /**
     * @see HandlerInterceptorAdapter#preHandle(HttpServletRequest, HttpServletResponse, Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		    throws Exception {
	try {
	    HandlerMethod hm = (HandlerMethod) handler;
	    Method method = hm.getMethod();
	    if (method.isAnnotationPresent(Auditable.class)) {
		long start = System.currentTimeMillis();
		String event = method.getAnnotation(Auditable.class).value();
		request.setAttribute(Event.EVENT_TYPE, event);
		request.setAttribute(Event.START, start);
		if (LOGGER.isInfoEnabled()) {
		    LOGGER.info(Utils.buildString("START - ", event, ". StartTime [", start, "]"));
		}
	    }
	}
	catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	}
	return super.preHandle(request, response, handler);
    }

    /**
     * @see HandlerInterceptorAdapter#afterCompletion(HttpServletRequest, HttpServletResponse, Object, Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
		    throws Exception {
	try {
	    HandlerMethod hm = (HandlerMethod) handler;
	    Method method = hm.getMethod();
	    if (method.isAnnotationPresent(Auditable.class)) {
		long start = (long) request.getAttribute(Event.START);
		String event = method.getAnnotation(Auditable.class).value();
		long execTime = System.currentTimeMillis() - start;
		if (LOGGER.isInfoEnabled()) {
		    LOGGER.info(Utils.buildString("END - ", event, ". ExecTime [", execTime, " ms]"));
		}
	    }
	}
	catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	}
	super.afterCompletion(request, response, handler, ex);
    }
}
