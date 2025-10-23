package co.edu.unicauca.utilities;

import co.edu.unicauca.services.AccountDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AccountDetailsService detailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, AccountDetailsService detailsService) {
        this.jwtUtils = jwtUtils;
        this.detailsService = detailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        try {
            String header = req.getHeader("Authorization");
            String requestURI = req.getRequestURI();

            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                Logger.info(getClass(), "JWT detected in request to " + requestURI);

                if (jwtUtils.validateJwtToken(token)) {
                    String email = jwtUtils.getUserNameFromJwtToken(token);
                    Logger.success(getClass(), "JWT validated successfully for user: " + email);

                    UserDetails userDetails = detailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    Logger.info(getClass(), "Authentication context set for: " + email);
                } else {
                    Logger.warn(getClass(), "Invalid JWT received for request to " + requestURI);
                }
            } else {
                Logger.warn(getClass(), "No JWT found in request to " + requestURI);
            }

        } catch (Exception e) {
            Logger.error(getClass(), "Cannot set user authentication: " + e.getMessage());
        }

        chain.doFilter(req, res);
    }
}
