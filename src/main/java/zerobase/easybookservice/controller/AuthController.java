package zerobase.easybookservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zerobase.easybookservice.security.TokenProvider;
import zerobase.easybookservice.dto.UserDto;
import zerobase.easybookservice.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody UserDto.SignUp request) {
        userService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto.SignIn request){
        // id 와 비밀번호 검증
        UserDto.SignInResponse user = userService.authenticate(request);
        String token = tokenProvider.generateToken(user.getEmail(), user.getAuthorities());
        log.info("user login -> " + user.getEmail());
        return ResponseEntity.ok(token); // 토큰값 반환
    }

}
