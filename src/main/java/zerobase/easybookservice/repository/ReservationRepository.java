package zerobase.easybookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.easybookservice.domain.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByStoreNameAndStoreLocationAndReservationDateAndReservationTime
            (String storeName, String storeLocation, LocalDate reservationDate, LocalTime reservationTime);

    boolean existsByUserNameAndEmail(String userName, String userEmail);

    List<Reservation> findByUserNameAndEmail(String userName, String userEmail);

    List<Reservation> findByStoreNameOrderByReservationDateAscReservationTimeAsc(String storeName);
    List<Reservation> findByStoreNameAndReservationDateOrderByReservationDateAscReservationTimeAsc(String storeName, LocalDate reservationDate);

    List<Reservation> findByStoreNameAndUserNameAndEmail(String storeName, String userName, String userEmail);

    Optional<Reservation> findByReservationNumber(String reservationNumber);
    // 예약번호는 고유함

    Optional<Reservation> findByUserNameAndReservationNumber(String username, String reservationNumber);


}
