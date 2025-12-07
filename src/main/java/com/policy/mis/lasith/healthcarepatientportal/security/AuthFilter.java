package com.policy.mis.lasith.healthcarepatientportal.security;

import com.policy.mis.lasith.healthcarepatientportal.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        if(token == null || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);
        String id = jwtService.extractId(token);
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null && id != null) {

            User user = (User) userDetailsService.loadUserByUsername(username);
            if(jwtService.isTokenValid(token, user)) {

                UsernamePasswordAuthenticationToken tokenAuthentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                tokenAuthentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
