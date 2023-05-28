package com.spring.security.configure.auth;

import com.spring.security.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// spring security 가 /loginProc 주소 요청이 오면 낚아채서 로그인을 진행
// 로그인 진행이 완료가 되면 spring security session 을 만들어서 반환한다. (Security ContextHolder)에다가 저장
// Security session 에 저장될 수 있는 객체 타입 = Authentication 타입 객체
// Authentication 객체는 UserDetails 타입 객체를 가지고 있음.

public class PrincipalDetails implements UserDetails {

    private User user; // Composition

    public PrincipalDetails(User user) {
        this.user = user;
    }


    // 해당 유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
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
}
