package com.spring.security.configure.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
    // 카카오 Attributes 는 이런 형식으로 반환된다.
    //    {
    //        id = 1234,
    //        properties = {
    //            nickname = cspyo,
    //            profile_image = http://asd.png
    //        },
    //    }

    private Map<String, Object> attributes;
    private Map<String, Object> properties;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.properties = (Map) attributes.get("properties");
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        // 카카오는 이메일을 필수로 받으려면 사업자 등록이 필요
        return null;
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    public String getNickname() {
        return (String) properties.get("nickname");
    }
}
