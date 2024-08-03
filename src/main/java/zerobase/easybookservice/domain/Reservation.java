package zerobase.easybookservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zerobase.easybookservice.domain.constant.ReservationStatus;
import zerobase.easybookservice.dto.ReservationDto;

import java.util.UUID;

@Entity(name = "Reservation")
@Getter
@ToString
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String storeName; // 예약할 매장 이름
    private String storeLocation; // 예약할 매장 위치
    private String userName; // 예약자 이름
    private String email; // 예약자 이메일
    private String reservationDate; // 예약 날짜 (YYYY-MM-DD) 형식
    private String reservationTime; // 예약 시간 (정각 마다 예약 받기로 가정)
    private String reservationNumber; // 랜덤으로 예약 번호

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public Reservation(ReservationDto reservationDto, String userEmail) {
        this.storeName = reservationDto.getStoreName();
        this.storeLocation = reservationDto.getStoreLocation();
        this.userName = reservationDto.getUserName();
        this.email = userEmail;
        this.reservationDate = reservationDto.getReservationDate();
        this.reservationTime = reservationDto.getReservationTime();
        this.status = ReservationStatus.PENDING;
    }

    // 예약번호는 중복되면 안됨 + 0부터 시작하면 고객이 예약의 개수 알 수 있음
    public void generateReservationNumber() {
        this.reservationNumber = String.valueOf(this.id + 54398710);
    }

    // 점장 예약 승인
    public void approve() {
        this.status = ReservationStatus.APPROVED;
    }

    // 점장 예약 거절
    public void reject() {
        this.status = ReservationStatus.REJECTED;
    }

    // 예약 승인 후 방문 확인
    public void visited() {
        this.status = ReservationStatus.VISITED;
    }

}
