package zerobase.easybookservice.domain.constant;

public enum ReservationStatus {
    PENDING, // 예약 신청 상태 (아직 승인 보류)
    APPROVED, // 예약 승인 (아직 방문 이전)
    REJECTED, // 예약 거부
    VISITED // 방문 확인
}
