package cocktail.application.User;

import cocktail.application.auth.SessionUser;
import cocktail.domain.user.Follow;
import cocktail.domain.user.User;
import cocktail.dto.FollowDto;
import cocktail.infra.user.FollowRepository;
import cocktail.infra.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public void follow(SessionUser user, String toUsername) {
        User fromUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(IllegalArgumentException::new);
        User toUser = userRepository.findByUsername(toUsername)
                .orElseThrow(IllegalArgumentException::new);

        // 이미 팔로우 중일경우 에러
        followRepository.findByUsers(fromUser, toUser)
                .orElseThrow(() -> new IllegalStateException("이미 팔로우 중입니다"));

        followRepository.save(new Follow(fromUser, toUser));
    }

    @Transactional
    public void unFollow(SessionUser user, String toUsername){
        User fromUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(IllegalArgumentException::new);
        User toUser = userRepository.findByUsername(toUsername)
                .orElseThrow(IllegalArgumentException::new);

    }

    /**
     * 팔로잉 리스트 찾기
     *
     * 1. userA가 팔로잉 하고 있는 유저 목록(닉네임, url, 내가 팔로잉하고 있는지)
     * 2. 내가 팔로잉 하고 있는 유저 목록과 비교
     * 3-1. 내가 팔로잉 하고 있는 유저가 있다면 팔로우 o 체크
     * 3-2. 내가 로그인중이 아니라면 전부 팔로우 x
     */
    @Transactional
    public List<FollowDto> findFollowingList(String username, SessionUser sessionUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(IllegalArgumentException::new);

        List<Follow> followingList = followRepository.findFollowingList(user);
        //make dtoList
        List<FollowDto> followerDtos = followingList.stream()
                .map(f -> FollowDto.fromUser(f.getToUser()))
                .collect(Collectors.toList());

        if (sessionUser != null) {
            compMyFollow(sessionUser, followerDtos);
        }
        return followerDtos;
    }

    /**
     * userA를 팔로우 하는 유저 리스트
     * 1. userA를 팔로우 하고 있는 유저 목록(닉네임, url, 내가 팔로잉하고 있는지)
     * 2. 내가 팔로잉 하고 있는 유저 목록과 비교
     * 3-1. 내가 팔로잉 하고 있는 유저가 있다면 팔로우 o 체크
     * 3-2. 내가 로그인중이 아니라면 전부 팔로우 x
     */
    @Transactional
    public List<FollowDto> findFollowerList(String username, SessionUser sessionUser) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(IllegalArgumentException::new);

        List<Follow> followerList = followRepository.findFollowerList(user);
        //make dtoList
        List<FollowDto> followerDtos = followerList.stream()
                .map(f -> FollowDto.fromUser(f.getFromUser()))
                .collect(Collectors.toList());

        System.out.println(sessionUser);
        if (sessionUser != null) {
            compMyFollow(sessionUser, followerDtos);
        }
        return followerDtos;
    }

    // 내 팔로우 리스트와 followDto의 유저들을 비교한다.
    // 만약 내 팔로우 리스트 중 겹치는 사람이 있다면 following을 true로 해준다
    private void compMyFollow(SessionUser sessionUser, List<FollowDto> followerDtos) {
        User myUser = userRepository.findByUsername(sessionUser.getUsername()).get();
        List<Follow> myFollowingList = followRepository.findFollowingList(myUser);

        for (FollowDto dto : followerDtos) {
            for (Follow follow : myFollowingList) {
                if(dto.getUsername().equals(follow.getToUser().getUsername())){
                    dto.setFollowing(true);
                }
            }
        }
    }
}
