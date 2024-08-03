package zerobase.easybookservice.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.easybookservice.exception.AbstractException;

public class AlreadyExistUserException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 가입된 이메일입니다.";
    }
}
