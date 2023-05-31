package com.spring.security.controller;

import com.spring.security.configure.auth.PrincipalDetails;
import com.spring.security.model.User;
import com.spring.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final UserService userService;



    @ResponseBody
    @GetMapping("/test/login")
    public String testLogin(
            Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println("/test/login/===========");
        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
        System.out.println("authentication = " + principalDetails.getUser());

        System.out.println("userDetails = " + userDetails.getUser());

        return "세션 정보 확인하기";
    }

    @ResponseBody
    @GetMapping("/test/oauth/login")
    public String testOauthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oAuth) {
        System.out.println("/test/oauth/login/===========");
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        System.out.println("oAuth2User = " + oAuth2User.getAttributes());

        System.out.println("oAuth = " + oAuth.getAttributes());

        return "세션 정보 확인하기";
    }

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @ResponseBody
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails = " + principalDetails.getUser());
        return "user";
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    // @Secured("ROLE_ADIN")
    @ResponseBody
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @ResponseBody
    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProc(User user) {
        userService.join(user);
        return "redirect:/login";
    }


}
