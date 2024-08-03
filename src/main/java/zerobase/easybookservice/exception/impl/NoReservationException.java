package zerobase.easybookservice.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.easybookservice.exception.AbstractException;

public class NoReservationException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 예약입니다.";
    }
}
