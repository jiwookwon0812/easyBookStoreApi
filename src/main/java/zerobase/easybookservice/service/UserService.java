package zerobase.easybookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.easybookservice.domain.User;
import zerobase.easybookservice.dto.UserDto;
import zerobase.easybookservice.exception.impl.AlreadyExistUserException;
import zerobase.easybookservice.exception.impl.InvalidPasswordException;
import zerobase.easybookservice.exception.impl.NoUserException;
import zerobase.easybookservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override // 사용자 이메일로 사용자 정보 가져오는 메서드
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " user not found"));
    }

    // 회원가입
    public UserDto.SignUp register(UserDto.SignUp userDto) {
        User user;
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new AlreadyExistUserException();
        }

        String encodingPassword = passwordEncoder.encode(userDto.getPassword());

        user = new User(userDto.getUsername(), userDto.getEmail(),
                encodingPassword, userDto.getAuthorities());

        userRepository.save(user);
        return userDto;
    }

    // 로그인
    public UserDto.SignInResponse authenticate(UserDto.SignIn userDto) {
        // id 존재하는지 확인
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new NoUserException());

        // id 와 비밀번호 일치하는지 확인
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        // User의 authorities 반환시에는 Collection<? extends GrantedAuthority> 타입
        // 이를 다시 List<String> 으로 변환
        return new UserDto.SignInResponse(user.getUsername(), user.getEmail(), authorities);
    }
}
