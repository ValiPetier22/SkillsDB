package com.skillsdb.jobseekerservice.jwtfilter;

import io.jsonwebtoken.*;
import io.swagger.models.HttpMethod;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");

        if(httpRequest.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            chain.doFilter(httpRequest, httpResponse);
        } else {
            String authHead = httpRequest.getHeader("Authorization");
            System.out.println("Token is " + authHead);
            String token = authHead.substring(7);

            try {
                JwtParser jwtParser = Jwts.parser().setSigningKey("Key");
                Jwt jwtObject = jwtParser.parse(token);

                Claims claimObj = (Claims) jwtObject.getBody();
                System.out.println("User userId " + claimObj.getSubject());

                HttpSession session = httpRequest.getSession();
                session.getAttribute("User userId " + claimObj.getSubject());
            } catch(SignatureException sign) {
                throw new ServletException("Signature mismatch");
            } catch(MalformedJwtException exception) {
                throw new ServletException("Token modified by unauthorized user");
            }
            chain.doFilter(httpRequest, httpResponse);
        }
    }
}
