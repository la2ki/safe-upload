package rs.marko.lalic.safe.core.security;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import rs.marko.lalic.safe.core.Utils;
import rs.marko.lalic.safe.core.constants.Event;
import rs.marko.lalic.safe.core.exceptions.InternalErrorException;
import rs.marko.lalic.safe.core.exceptions.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Security interceptor checking all requests for authorization token
 * Created by Marko Lalic
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    /**
     * Class logger
     */
    private static final Logger LOGGER = Logger.getLogger(SecurityInterceptor.class);
    /**
     * Holder for authorization token
     */
    private String authorizationToken;

    /**
     * Empty construct
     */
    public SecurityInterceptor() {
    }

    /**
     * Setter for authorization token
     *
     * @param authorizationToken authorization token
     */
    public void setAuthorizationToken(String authorizationToken) throws InternalErrorException {
	if (StringUtils.isEmpty(authorizationToken)) {
	    throw new InternalErrorException("Invalid authorization token: " + authorizationToken);
	}
	this.authorizationToken = authorizationToken;
    }

    /**
     * @see HandlerInterceptorAdapter#preHandle(HttpServletRequest, HttpServletResponse, Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		    throws Exception {
	String token = request.getHeader(Event.TOKEN);
	if (!authorizationToken.equals(token)) {
	    LOGGER.error(Utils.buildString("Access denied. Time: ", System.currentTimeMillis()));
	    throw new UnauthorizedException("Access denied.");
	}
	return super.preHandle(request, response, handler);
    }
}
