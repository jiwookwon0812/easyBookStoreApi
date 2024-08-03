package zerobase.easybookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private String storeName; // 예약할 매장 이름
    private String storeLocation; // 예약할 매장 위치
    private String userName; // 예약자 이름
    private String reservationDate; // 예약 날짜
    private String reservationTime; // 예약 시간
    private String reservationNumber;
    private String status;
}
