package cocktail.application;


import cocktail.domain.User;
import cocktail.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // 로그인 요청시 AuthenticationManager의 요청에 의해 username에 해당하는 User가 있는지 확인 후 리턴해준다.
    // 로그인 폼 방식에서 인풋의 name이 username이라서 아래 메서드의 파라미터 명이 username이 된 것. (둘이 같아야함)
    // 만약 다른 값으로 설정해주고 싶다면 WebSecurityConfig에서 .usernameParameter()를 설정해줘야한다.
    // 아래 함수 실행 결과로 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User users = userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("Could not found user" + username));

        log.info("Succes find user {}",users);

        return createUserDetails(users);
    }


    private UserDetails createUserDetails(User user) {
        List<SimpleGrantedAuthority> grantedAuthorities = user.getRoleList().stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPw(),
                grantedAuthorities);
    }




}
