package com.Safe.Link.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return isPublicPath(getRequestPath(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
        throws ServletException, IOException{

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")){
            String token = header.substring(7);
            if (jwtUtil.isTokenValid(token)){
                String correo = jwtUtil.extractCorreo(token);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(correo, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request,response);
    }

    private String getRequestPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestUri = request.getRequestURI();
        if (contextPath != null && !contextPath.isBlank() && requestUri.startsWith(contextPath)) {
            return requestUri.substring(contextPath.length());
        }
        return requestUri;
    }

    private boolean isPublicPath(String path) {
        return path.equals("/api/usuarios/login")
                || path.equals("/api/usuarios/login/")
                || path.equals("/api/usuarios/registro")
                || path.equals("/api/usuarios/registro/")
                || path.equals("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs/");
    }
}
