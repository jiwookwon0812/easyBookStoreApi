package zerobase.easybookservice.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.easybookservice.exception.AbstractException;

public class AlreadyExistReservationException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 같은 시간대에 존재하는 예약이 있습니다.";
    }
}
