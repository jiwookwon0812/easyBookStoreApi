package zerobase.easybookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.easybookservice.domain.Reservation;
import zerobase.easybookservice.domain.constant.ReservationStatus;
import zerobase.easybookservice.dto.ReservationDto;
import zerobase.easybookservice.repository.ReservationRepository;
import zerobase.easybookservice.repository.StoreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    // 상점 예약
    @Transactional
    public ReservationDto reserveStore(ReservationDto reservationDto) {
        Reservation reservation;
        confirmReservation(reservationDto);
        // 예약 조건 만족 시
        reservation = new Reservation(reservationDto);
        reservationRepository.save(reservation);
        reservation.generateReservationNumber(); // 처음 저장할 때 id 엔티티가 할당되기 때문에
        // 한번 더 저장해주기
        return reservationDto;
    }

    // 상점 예약 시 확인 조건 (?)
    public void confirmReservation(ReservationDto reservationDto) {
        // 1. 해당 상점 이름과 위치에 맞는 상점이 있어야 함
        if (!storeRepository.existsByNameAndLocation(reservationDto.getStoreName(), reservationDto.getStoreLocation())) {
            throw new RuntimeException("Store not found");
        }
        // 2. 중복 예약 안됨
        if (reservationRepository
                .existsByStoreNameAndStoreLocationAndReservationDateAndReservationTime
                        (reservationDto.getStoreName()
                                , reservationDto.getStoreLocation()
                                , reservationDto.getReservationDate()
                                , reservationDto.getReservationTime())) {
            throw new RuntimeException("Reservation already exists in this time");
        }
    }

    // 예약 조회 (예약자 이름으로)
    @Transactional(readOnly = true)
    public List<ReservationDto> searchReservations(String name, String birth) {
        List<ReservationDto> reservationDtos;
        List<Reservation> reservations;

        if (!reservationRepository.existsByUserNameAndBirth(name, birth)) { // 예약자 이름과 생년월일로 조회 시 예약정보 X 때
            throw new RuntimeException("Reservation not found");
        }

        // 예약정보 조회 성공시
        reservations = reservationRepository.findByUserNameAndBirth(name, birth);

        reservationDtos = reservations.stream()
                .map(e -> new ReservationDto(e.getStoreName(), e.getStoreLocation(), e.getUserName(),
                        e.getBirth(), e.getReservationDate(), e.getReservationTime(), e.getReservationNumber(), e.getStatus().toString()))
                .toList();
        return reservationDtos;
    }

    // 예약 삭제
    @Transactional
    public void deleteReservation(String userName, String birth, String storeName) {
        List<Reservation> reservations;

        // 1. storeName null 일 때 -> 해당 이용자의 모든 예약 삭제
        if (storeName == null) {
            reservations = reservationRepository.findByUserNameAndBirth(userName, birth);
            if (reservations.isEmpty()) {
                throw new RuntimeException("Reservation not found");
            }
            reservationRepository.deleteAll(reservations);
        } else {
            // 2. storeName 으로 특정 예약 삭제
            reservations = reservationRepository.findByStoreNameAndUserNameAndBirth(storeName, userName, birth);
            if (reservations.isEmpty()) {
                throw new RuntimeException("Reservation not found");
            }
            reservationRepository.deleteAll(reservations);
        }
    }

    // 예약 승인
    @Transactional
    public void approveReservation(String reservationNumber) {
        Reservation reservation;
        reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.approve();
        reservationRepository.save(reservation);
    }

    // 예약 거절
    @Transactional
    public void rejectReservation(String reservationNumber) {
        Reservation reservation;
        reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.reject();
        reservationRepository.save(reservation);
    }

    // 방문 확인
    @Transactional
    public void confirmVisited(String reservationNumber, String userName) {
        Reservation reservation;
        reservation = reservationRepository.findByUserNameAndReservationNumber(userName, reservationNumber)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        if (reservation.getStatus() != ReservationStatus.APPROVED) {
            throw new RuntimeException("Reservation is not approved");
        } // 예약 승인 된 건에 한해서만 방문 확인 가능

        reservation.visited(); // 방문 확인 !!
        reservationRepository.save(reservation);
    }
}
