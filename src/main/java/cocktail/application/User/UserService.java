package cocktail.application.User;

import cocktail.application.auth.SessionUser;
import cocktail.domain.Role;
import cocktail.domain.User;
import cocktail.dto.UserDto;
import cocktail.dto.UserProfileDto;
import cocktail.global.exception.DuplicateUserIdException;
import cocktail.global.exception.PwNotMatchException;
import cocktail.infra.user.FollowRepository;
import cocktail.infra.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cocktail.dto.UserDto.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;


    @Transactional
    public Long signUp(SignUpRequestDto dto) { // 회원가입
        // id 중복체크
        validateDuplicateUser(dto.getUsername());
        // pw 암호화
        String encodePw = passwordEncoder.encode(dto.getPw());

        // member 생성 후 저장
        return userRepository
                .save(new User(dto.getUsername(),encodePw, dto.getNickname(),dto.getTitle(),dto.getProfileImgUrl(), Role.USER))
                .getId();
    }

    @Transactional
    public UserDto signIn(LoginRequestDto dto) {
        // findMember
        // 예외처리 문제 있음 ....filter라서
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("userService.signIn : userId 확인 불가"));

        // pw 체크
        if(!passwordEncoder.matches(dto.getPw(), user.getPw())){
            throw new PwNotMatchException("userId : " + user.getUsername() + " Invalid password");
        }

        return new UserDto(user.getUsername(), user.getNickname());
    }

    // 유저 중복 확인
    public void validateDuplicateUser(String userName) {
         userRepository.findByUsername(userName)
                .ifPresent(member -> {
                    log.debug("userId : {}, 아이디 중복 발생", userName);
                    throw new DuplicateUserIdException("아이디 중복");
                });
    }

    // 닉네임 중복 확인
    public void validateDuplicateNickname(String nickName) {
        userRepository.findByNickname(nickName)
                .ifPresent(member -> {
                    log.debug("nickname : {}, 닉네임 중복 발생", nickName);
                    throw new DuplicateUserIdException("닉네임 중복");
                });
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }


    private void isValid(SessionUser sessionUser, User user){
        if(!user.getUsername().equals(user.getUsername())){
            throw new IllegalArgumentException("본인이 아닙니다.");
        }
    }

    @Transactional
    public String userUpdate(UserUpdateDto userUpdateDto, SessionUser sessionUser){
        User user =userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);

        isValid(sessionUser,user);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.update(
                encoder.encode(userUpdateDto.getPw()),
                userUpdateDto.getNickname(),
                userUpdateDto.getTitle(),
                userUpdateDto.getProfileImgUrl()
        );

        return sessionUser.getUsername();

    }

    @Transactional
    public UserProfileDto getProfile(String username,SessionUser sessionId){
        UserProfileDto userProfileDto = new UserProfileDto();

        //현재 id에 해당하는 user정보로 UserDto 생성
        User user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        userProfileDto.setUserDto(UserDto.builder()
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .title(user.getTitle())
                        .profileImgUrl(user.getProfileImgUrl())
                        .build());

        //sessionId로 profileId가 로그인된 유저인지 확인
        User loginUser = userRepository.findByUsername(sessionId.getUsername()).orElseThrow(IllegalArgumentException::new);
        userProfileDto.setLoginUser(loginUser.getUsername() == user.getUsername());
        userProfileDto.setLoginName(loginUser.getUsername());

        //profileId를 가진 유저가 sessionId을 가진 유저를 팔로우했는지 확인
        userProfileDto.setFollow(followRepository.findFollowByFromUserAndToUser(loginUser,user) != null);

        //profileId를 가진 유저의 팔로워,팔로인 수 체크
       // userProfileDto.setUserFollowerCount(followRepository.findFollowerCountById(profileId));
      // userProfileDto.setUserFollowingCount(followRepository.findFollowingCountById(profileId));

        return userProfileDto;
    }

}
