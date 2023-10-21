package com.courses.springsecuritycourse.config.filter;

import com.courses.springsecuritycourse.models.User;
import com.courses.springsecuritycourse.repository.UserRepository;
import com.courses.springsecuritycourse.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {



        // 1 Obtener el header que contiene el JWT
        String authHeader = request.getHeader("Authorization"); // Bearer jwt

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        // 2 Sacar el JWT de ese Header
        String jwt = authHeader.split(" ")[1];

        // 3 Obtener el subject/username desde el JWT
        String username = jwtService.extractUsername(jwt);

        // 4 Setear un objeto Authentication dentro del SecurityContextHolder

        User user = userRepository.findByUsername(username).get();

        UsernamePasswordAuthenticationToken authToken  = new UsernamePasswordAuthenticationToken(
                username,
                null,
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 5 Ejecutar el resto de filtros, como el next() de Express.js
        filterChain.doFilter(request, response);

    }
}
