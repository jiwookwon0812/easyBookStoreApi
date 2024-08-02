package zerobase.easybookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;


public class UserDto {
    // 로그인
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignIn {
        private String email;
        private String password;
    }

    // 회원가입
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {
        private String username;
        private String email;
        private String password;
        private List<String> authorities;
    }

    // 로그인시 응답 dto (권한정보 제공) (패스워드를 직접 보내는 건 보안상 이유로 X)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInResponse{
        private String username;
        private String email;
        private List<String> authorities;

    }
}
