package cocktail.api;

import cocktail.domain.User;
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

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User users = userRepository.findByUsername(name)
                .orElseThrow(()->new UsernameNotFoundException("Could not found user" + name));

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
