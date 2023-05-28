package com.spring.security.service;

import com.spring.security.model.User;
import com.spring.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(User user) {
        user.setRole("GENERAL_USER");
        String rawPassword = user.getPassword();
        String encodPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encodPassword);
        userRepository.save(user);

        return user.getId();
    }

    /**
     * 모든 유저 조회
     */
    public List<User> findUsers() {
        return userRepository.findAll();
    }

}
