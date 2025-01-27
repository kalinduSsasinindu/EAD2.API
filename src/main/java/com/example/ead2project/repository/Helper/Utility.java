package com.example.ead2project.repository.Helper;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

public class Utility {
    
    public static String getUserIdFromContext() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            Object clientId = request.getAttribute("ClientId");
            return clientId != null ? clientId.toString() : null;
        }
        return null;
    }

    public static String getUserIdFromClaims(HttpServletRequest httpRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserIdFromClaims'");
    }
}