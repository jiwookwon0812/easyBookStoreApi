package zerobase.easybookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private String storeName; // 예약할 매장 이름
    private String storeLocation; // 예약할 매장 위치
    private String userName; // 예약자 이름
    private LocalDate reservationDate; // 예약 날짜 (YYYY-MM-DD) 형식
    private LocalTime reservationTime; // 예약 시간 (분까지만)(HH:MM)
    private String reservationNumber;
    private String status;
}
