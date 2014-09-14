package ru.uproom.service;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by HEDIN on 11.09.2014.
 */
public class SessionFilter implements Filter {

    // @Autowired now working, TODO define how to use it here
    //private SessionHolder sessionHolder = SessionHolderImpl.getInstance();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String sid = (String) request.getSession().getAttribute("SID");
        if (null != sid)
            SessionHolderImpl.getInstance().touchSession(sid);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
