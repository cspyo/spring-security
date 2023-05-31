package com.spring.security.configure;

import com.spring.security.configure.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class SecurityConfigure {

    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 해제
        http.csrf().disable();

        // 인증, 권한 필터 설정
        http.authorizeRequests()
                // 권한 설정
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**")
                .access("hasRole('ADMIN') or hasRole('MANAGE')")
                .antMatchers("/admin/**").hasRole("ADMIN") // 실제 role String 필드에는 ("ROLE_ADMIN")처럼 접두어가 붙어야 인식
                .anyRequest().permitAll()
                // 기본 로그인 관련
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("usernameParam") // login.html에 username 파라미터 이름이랑 매칭해야한다.
                .loginProcessingUrl("/loginProc") // loginProc 가 호출되면 security 에서 대신 로그인을 진행해준다.
                .defaultSuccessUrl("/") // 로그인 성공하면 메인 페이지로
                // OAuth2.0 관련
                .and()
                .oauth2Login()
                .loginPage("/login") // 구글 로그인 완료 뒤 후처리가 필요 (엑세스토큰 + 사용자프로필 정보)
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
        return http.build();
    }

}
