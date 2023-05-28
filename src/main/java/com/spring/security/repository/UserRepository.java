package com.spring.security.repository;


import com.spring.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA는 기본 CRUD를 JpaRepository가 상속하는 CrudRepository가 가지고 있다.
 * JPA는 method names 전략을 가진다.
 * @Repository 어노테이션 없어도 된다.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // SELECT * FROM user WHERE username = (:username)
    User findByUsername(String username);

}
