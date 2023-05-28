package com.spring.security.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfigure {

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 해제
        http.csrf().disable();

        // 인증, 권한 필터 설정
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**")
                .access("hasRole('ADMIN') or hasRole('MANAGER')")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("usernameParam") // login.html에 username 파라미터 이름이랑 매칭해야한다.
                .loginProcessingUrl("/loginProc") // loginProc 가 호출되면 security 에서 대신 로그인을 진행해준다.
                .defaultSuccessUrl("/"); // 로그인 성공하면 메인 페이지로

        return http.build();
    }

}
