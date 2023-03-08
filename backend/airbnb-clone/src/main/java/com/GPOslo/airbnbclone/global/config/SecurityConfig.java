package com.GPOslo.airbnbclone.global.config;

import com.GPOslo.airbnbclone.global.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity  // 기본적인 웹 보안을 사용하겠다는 의미
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)  // @PreAuthorize 사용을 위함
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomEmailPasswordAuthProvider customEmailPasswordAuthProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        ProviderManager authenticationManager = (ProviderManager) authenticationConfiguration.getAuthenticationManager();
        authenticationManager.getProviders().add(customEmailPasswordAuthProvider);
        return authenticationManager;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.ignoring()
                    .antMatchers("/h2-console/**", "/favicon.ico");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf().disable()
                // exception handling 할 때 커스텀 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                /* iframe 관련 설정이고 X-frame-Options Click Jaking 공격을 기본적으로 막는걸로 설정되어있는데
                 이를 풀기위한 설정을 하려면 아래의 설정을 추가하면 됨 */
                /* .and()
                 .headers()
                 .frameOptions()
                 .sameOrigin() */
                .and()

                // 스프링 시큐리티는 기본적으로 세션을 사용하며,
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // http servletRequest 를 사용하는 요청들에 대한 접근제한을 설정
                .authorizeRequests()

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .antMatchers("/auth/**").permitAll()

                // swagger 3 관련 요청 permitAll 설정
                .antMatchers("/v3/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()

                // 나머지 API 는 전부 인증 필요
                .anyRequest().authenticated()
                .and()

                // JwtFilter 등록
                // UsernamePasswordAuthenticationFilter 앞에 등록하는 이유는 딱히 없지만
                // SecurityContext를 사용하기 때문에 앞단의 필터에서 SecurityContext가 설정되고 난뒤 필터를 둔다.
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
