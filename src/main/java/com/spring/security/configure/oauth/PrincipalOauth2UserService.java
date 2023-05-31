package com.spring.security.configure.oauth;

import com.spring.security.configure.auth.PrincipalDetails;
import com.spring.security.configure.oauth.provider.FacebookUserInfo;
import com.spring.security.configure.oauth.provider.GoogleUserInfo;
import com.spring.security.configure.oauth.provider.NaverUserInfo;
import com.spring.security.configure.oauth.provider.OAuth2UserInfo;
import com.spring.security.model.User;
import com.spring.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    // 구글로 받은 userRequest 데이터에 대한 후처리 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest : " + userRequest.getClientRegistration()); // 어떤 OAuth 로 들어왔는지
        System.out.println("userRequest : " + userRequest.getAccessToken());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글 로그인 버튼  클릭 -> 구글 로그인 창에서 로그인 프로세스 완료 -> code를 리턴받고 OAuth-client 라이브러리에서 이를 처리
        // -> AccessToken 으로 userRequest 정보 가져옴 -> loadUser 함수로 회원 프로필을 받아줌
        System.out.println("userRequest : " + oAuth2User.getAttributes());


        // 강제 회원가입 시키기 (OAuth 에서 받은 정보로 회원 정보 생성하고 DB에 저장)
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserInfo oAuth2UserInfo = null;
        if (provider.equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(attributes);
        } else if (provider.equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfo(attributes);
        } else if (provider.equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo(attributes);
        } else {
            System.out.println("지원하지 않는 형식의 로그인입니다.");
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("의미없음"); // 의미없음
        String email = oAuth2UserInfo.getEmail();
        String role = "GENERAL_USER";

        User userEntity = userRepository.findByUsername(username); // 이 프로젝트에서는 이름으로 중복을 관리
        if (userEntity == null) {
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("Already Signed up.");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
