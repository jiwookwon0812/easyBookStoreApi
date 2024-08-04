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
        private String email; // 이메일
        private String password; // 비밀번호
    }

    // 회원가입
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {
        private String username; // 회원 이름
        private String email; // 회원 이메일
        private String password; // 회원 비밀번호
        private List<String> authorities; // 회원 권한
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
