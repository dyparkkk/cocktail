package cocktail.application;

import cocktail.domain.User;
import cocktail.dto.OAuthAttributes;
import cocktail.dto.SessionUser;
import cocktail.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService  extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        // OAuth2 서비스 id 구분코드 ( 구글, 카카오, 네이버 )
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //테스트
        System.out.println("==================== "+registrationId +" ==================");

        // OAuth2 로그인 진행시 키가 되는 필드 값 (PK) (구글의 기본 코드는 "sub")
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        //테스트
        System.out.println("================ "+userNameAttributeName+" =================");

        // OAuthAttributes: attribute를 담을 클래스 (개발자가 생성)
        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스(네이버로그인시사용)
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        // 세션 정보를 저장하는 직렬화된 dto 클래스
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoles())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 소셜로그인시 기존 회원이 존재하면 수정날짜 정보만 업데이트해 기존의 데이터는 그대로 보존
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByUsername(attributes.getUsername())
                .map(entity->entity.update(attributes.getUsername(), attributes.getNickname()))
                .orElse(attributes.toEntity());



        return userRepository.save(user);
    }

}
