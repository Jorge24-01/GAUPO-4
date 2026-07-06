package com.Safe.Link.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.UsuarioRepository;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String path = getRequestPath(request);
        return isPublicPath(path) || isPublicGet(request.getMethod(), path);
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
                usuarioRepository.findByCorreo(correo).ifPresent(usuario -> {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(correo, null, authorities(usuario));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                });
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
                || path.equals("/api/asistente/preguntar")
                || path.equals("/api/asistente/preguntar/")
                || path.equals("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs/");
    }

    private boolean isPublicGet(String method, String path) {
        if (!HttpMethod.GET.matches(method)) {
            return false;
        }

        return path.equals("/api/educacion/consejos")
                || path.startsWith("/api/educacion/consejos/")
                || path.equals("/api/soporte/faq")
                || path.startsWith("/api/soporte/faq/")
                || path.equals("/api/puntos-seguros")
                || path.equals("/api/puntos-seguros/")
                || path.matches("^/api/puntos-seguros/\\d+/?$")
                || path.matches("^/api/puntos-seguros/tipo/[^/]+/?$")
                || path.equals("/api/refugios/disponible")
                || path.equals("/api/items-kit/recomendados")
                || path.equals("/api/emergencia/numeros")
                || path.startsWith("/api/emergencia/numeros/");
    }

    private List<SimpleGrantedAuthority> authorities(Usuario usuario) {
        String tipoUsuario = usuario.getTipo_usuario() == null ? "" : usuario.getTipo_usuario().trim().toUpperCase();
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuario));
    }
}
