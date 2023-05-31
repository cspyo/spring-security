package com.spring.security.configure;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomBcryptPasswordEncoder extends BCryptPasswordEncoder {
}
