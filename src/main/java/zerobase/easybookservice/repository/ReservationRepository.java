package zerobase.easybookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.easybookservice.domain.Reservation;
import zerobase.easybookservice.dto.ReservationDto;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByStoreNameAndStoreLocationAndReservationDateAndReservationTime
            (String storeName, String storeLocation, String reservationDate, String reservationTime);

    boolean existsByUserNameAndBirth(String userName, String birth);

    List<Reservation> findByUserNameAndBirth(String userName, String birth);

    List<Reservation> findByStoreNameAndUserNameAndBirth(String storeName, String userName, String birth);

    Optional<Reservation> findByReservationNumber(String reservationNumber);
    // 예약번호는 고유함

    Optional<Reservation> findByUserNameAndReservationNumber(String username, String reservationNumber);

}
