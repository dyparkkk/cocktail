package cocktail.application;

import cocktail.application.MyUserDetailsService;
import cocktail.domain.User;
import cocktail.global.exception.DuplicateUserIdException;
import cocktail.global.exception.PwNotMatchException;
import cocktail.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cocktail.dto.UserDto.LoginRequestDto;
import static cocktail.dto.UserDto.SignUpRequestDto;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MyUserDetailsService myUserDetailsService;

    @Transactional
    public Long signUp(SignUpRequestDto dto) { // 회원가입
        // id 중복체크
        validateDuplicateUser(dto.getUsername());
        // pw 암호화
        String encodePw = passwordEncoder.encode(dto.getPw());

        // member 생성 후 저장
        return userRepository
                .save(new User(dto.getUsername(),encodePw,dto.getUsername()))
                .getId();
    }

    @Transactional
    public String signIn(LoginRequestDto dto) {
        // findMember
        // 예외처리 문제 있음 ....filter라서
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(dto.getUsername());

        // pw 체크
        if(!passwordEncoder.matches(dto.getPw(), userDetails.getPassword())){
            throw new PwNotMatchException("userId : " + userDetails.getUsername() + " Invalid password");
        }

        return userDetails.getUsername();
    }

    // 유저 중복 확인
    public void validateDuplicateUser(String userName) {
         userRepository.findByUsername(userName)
                .ifPresent(member -> {
                    log.debug("userId : {}, 아이디 중복 발생", userName);
                    throw new DuplicateUserIdException("아이디 중복");
                });
    }
}
