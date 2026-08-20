/*
 * This filter intercepts every incoming request.
 * It extracts the JWT token from the Authorization header,
 * validates it, and authenticates the user before
 * the request reaches the controller.
 */
package com.dreamcart.backend.security;

import com.dreamcart.backend.service.CustomUserDetailsService;
import com.dreamcart.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService) {

        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        String path =
                request.getServletPath();

        return path.equals(
                "/api/auth/login"
        )
                ||
                path.equals(
                        "/api/auth/register"
                );
    }
    /**
     * Validates JWT token for every incoming request.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader(
                        "Authorization"
                );

        if (authHeader == null
                ||
                !authHeader.startsWith(
                        "Bearer "
                )) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String jwt =
                authHeader.substring(7);

        String email;
        try {
            email = jwtService.extractEmail(jwt);

        }
        catch (Exception exception) {
            filterChain.doFilter(request, response);
            return;

        }
        // Only Authenticate if the request does not already have an authenticated user.
        if (email != null
                &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        == null) {

            UserDetails userDetails =
                    customUserDetailsService
                            .loadUserByUsername(
                                    email
                            );

            if (jwtService.isTokenValid(
                    jwt,
                    userDetails.getUsername()
            )) {

                UsernamePasswordAuthenticationToken
                        authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails
                                        .getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                authentication
                        );
            }
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}