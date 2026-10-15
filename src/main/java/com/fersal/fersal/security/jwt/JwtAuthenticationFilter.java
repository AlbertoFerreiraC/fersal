package com.fersal.fersal.security.jwt;


import io.jsonwebtoken.ExpiredJwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import lombok.RequiredArgsConstructor;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;


    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        final String authHeader = request.getHeader("Authorization");


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;

        }


        final String jwt = authHeader.substring(7);


        try {


            final String email = jwtService.extraerEmail(jwt);


            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {


                UserDetails userDetails = userDetailsService.loadUserByUsername(email);


                if (jwtService.validarToken(jwt)) {


                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());


                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                    SecurityContextHolder.getContext().setAuthentication(authToken);


                }


            }


            filterChain.doFilter(request, response);


        } catch (ExpiredJwtException e) {


            e.printStackTrace();


            SecurityContextHolder.clearContext();


            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);


            response.setContentType("application/json");


            response.getWriter().write("{\"error\":\"TOKEN_EXPIRED\",\"message\":\"La sesión ha expirado\"}");


        } catch (Exception e) {


            // ESTO NOS MOSTRARÁ EL ERROR REAL EN INTELLIJ
            e.printStackTrace();


            SecurityContextHolder.clearContext();


            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);


            response.setContentType("application/json");


            response.getWriter().write("{\"error\":\"INVALID_TOKEN\",\"message\":\"" + e.getClass().getSimpleName() + ": " + e.getMessage() + "\"}");


        }


    }

}