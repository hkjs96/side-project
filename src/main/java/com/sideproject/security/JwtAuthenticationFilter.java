package com.sideproject.security;

import com.sideproject.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private TokenProvider tokenProvider;

    private UserRepository userRepository;

    @Autowired
    JwtAuthenticationFilter(TokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = parseBearerToken(request);
            log.info("Filter is running...");
            if(token != null && !token.equalsIgnoreCase("null")) {
                try {
                    Claims claims = tokenProvider.validateAndGetUserId(token);
                    String userId = claims.getSubject();
                    // TODO: 시큐리티의 UserDetails 이런 부분 참고 해서 수정 필요, 일단 repository에서 검색해서 넣음...

                    log.info("Authenticated user ID : " + userId);
                    AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        userId, // 인증된 사용자의 정보, 보통 UserDetails 이라는 오브젝트를 넣는데, 여기는 X
                            userRepository.findById(userId).get(),
                            null,
                            AuthorityUtils.NO_AUTHORITIES
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    securityContext.setAuthentication(authentication);
                    SecurityContextHolder.setContext(securityContext);
                } catch (ExpiredJwtException e) {
                    logger.error("JWT ERROR: " + e.getMessage());
                    /*
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setHeader("Content-Type", "application/yml");
                        response.getWriter().println("Token Expired");
                   */
                }
            }
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.split(" ")[1];
        }

        return null;
    }
}
