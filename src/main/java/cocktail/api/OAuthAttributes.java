package cocktail.api;

import cocktail.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String username;
    private String nickname;
    private String email;
    private String picture;

    //of() OAuth2User에서 반환하는 사용자 정보는Map이기에 값 하나하나를 변환해야 한다
    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        //구글인지 네이버인지 카카오인지 구분하기 위한 메소드 (ofNaver, ofKaKao)

        if("naver".equals(registrationId)) {
            return ofNaver("id",attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()                .
                username((String) attributes.get("email"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .username((String) response.get("id"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    public User toEntity() {
        return User.builder()
                .username(email)
                .nickname(nickname)
                .build();
    }

}
