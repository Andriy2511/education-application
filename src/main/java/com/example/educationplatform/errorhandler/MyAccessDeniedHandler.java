package com.example.educationplatform.errorhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("myAccessDeniedHandler")
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private final RequestCache myRequestCache;

    @Autowired
    public MyAccessDeniedHandler(@Qualifier("myRequestCache") RequestCache myRequestCache) {
        this.myRequestCache = myRequestCache;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc)
            throws IOException, ServletException {

        if (!response.isCommitted()) {
            myRequestCache.saveRequest(request, response);
            request.getRequestDispatcher("/login").forward(request, response);
        }
    }
}
