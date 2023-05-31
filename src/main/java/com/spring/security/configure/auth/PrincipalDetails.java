package com.spring.security.configure.auth;

import com.spring.security.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// spring security 가 /loginProc 주소 요청이 오면 낚아채서 로그인을 진행
// PrincipalDetailsService의 loadUserByUserName이 호출되고 user객체가 담긴 PrincipalDetails(UserDetails, OAuth2User 구현) 를
// SecurityContextHolder 의 Authentication 에 저장함.
// 후에 Controller에서 Authentication 매개변수로 호출하면 이때 저장된 객체를 접근할 수 있다.

// Security session 에 저장될 수 있는 객체 타입 = Authentication 타입 객체
// Authentication 객체는 UserDetails, OAuth2User 타입 객체를 가질 수 있음.

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // Composition
    private Map<String, Object> attributes;

    // 일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(() -> user.getRole());
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 휴면회원이거나 블랙회원이거나 하면 False
        return true;
    }


    //////OAuth2User///
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
