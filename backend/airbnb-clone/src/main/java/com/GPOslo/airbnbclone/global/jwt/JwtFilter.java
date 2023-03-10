package com.GPOslo.airbnbclone.global.jwt;

import com.GPOslo.airbnbclone.global.jwt.domain.TokenStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";
    private final TokenProvider tokenProvider;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);
        log.debug("token = {}", token);

        if(StringUtils.hasText(token)) {
            final TokenStatus flag = tokenProvider.validateToken(token);

            log.debug("flag = {}", flag);
            if(flag == TokenStatus.VALID_TOKEN) {
                this.setAuthentication(token);
            } else if (flag == TokenStatus.EXPIRED_TOKEN) {
                // 클라이언트가 reissue 요청을 하도록 약속된 JSON 값을 전달 해야한다.
            } else { // (flag == TokenStatus.WRONG_TOKEN) {

            }
        }

        filterChain.doFilter(request, response);

        // "/auth" 로 시작하는 요청은 그냥 통과
//        if(request.getServletPath().startsWith("/auth")) {
//            filterChain.doFilter(request, response);
//        } else {    // 그 외의 요청은 토큰의 값을 검사
//            String token = resolveToken(request);
//
//            log.debug("token = {}", token);
//
//            if(StringUtils.hasText(token)) {
//                final int flag = tokenProvider.validateToken(token);
//
//                log.debug("flag = {}", flag);
//                // 토큰이 유효한 경우
//                if (flag == 1) {
//                    this.setAuthentication(token);
//                } else if(flag == 2) {      // 토큰이 만료된 경우
//                    response.setContentType("application/json");
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);   // 403
//                    response.setCharacterEncoding("UTF-8");
//                    PrintWriter out = response.getWriter();
//                    log.debug("doFilterInternal Exception CALL!");
//                    out.println("{\"error\" : \"ACCESS_TOKEN_EXPIRED\", \"message\" : \"엑세스 토큰이 만료되었습니다.\"}");
//                } else {    // 잘못된 토큰인 경우
//                    response.setContentType("application/json");
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);   // 403
//                    response.setCharacterEncoding("UTF-8");;
//                    PrintWriter out = response.getWriter();
//                    out.println("{\"error\": \"EMPTY_TOKEN\", \"message\" : \"토큰 값이 비어있습니다.\"}");
//                }
//            } else {
//                filterChain.doFilter(request, response);
//            }
//        }

    }

    /**
     *
     * @param token
     * 토큰이 유효한 경우 SecurityContext에 저장
     */
    private void setAuthentication(String token) {
        Authentication authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     *
     * @param request
     * @return RequestHeader에서 토큰 정보를 꺼낸다
     */
    private String resolveToken(HttpServletRequest request) {
        // bearer : 123123123123123 -> return 123123123123123123
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
