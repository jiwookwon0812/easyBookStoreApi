package zerobase.easybookservice.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.easybookservice.exception.AbstractException;

public class InvalidReservationException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "현재보다 이전 시간은 예약이 불가합니다.";
    }
}
