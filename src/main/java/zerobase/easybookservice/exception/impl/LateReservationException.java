package zerobase.easybookservice.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.easybookservice.exception.AbstractException;

public class LateReservationException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "예약 확인은 예약 시간 10분 전까지만 가능합니다. 관리자에게 직접 문의해주세요.";
    }
}
