package zerobase.easybookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.easybookservice.domain.Reservation;
import zerobase.easybookservice.domain.constant.ReservationStatus;
import zerobase.easybookservice.dto.ReservationDto;
import zerobase.easybookservice.exception.impl.*;
import zerobase.easybookservice.repository.ReservationRepository;
import zerobase.easybookservice.repository.StoreRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        // 로그인한 사용자 이메일을 가져옴
        String email =getCurrentUserEmail();
        confirmReservation(reservationDto);
        // 예약 조건 만족 시
        reservation = new Reservation(reservationDto, email);
        reservationRepository.save(reservation);
        reservation.generateReservationNumber(); // 처음 저장할 때 id 엔티티가 할당되기 때문에
        // 한번 더 저장해주기 (예약번호 할당해주기 위해)
        reservationRepository.save(reservation);
        return reservationDto;
    }

    // 로그인한 사용자의 이메일 가져오는 메서드
    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    // 상점 예약 시 확인 조건 (예약 가능 여부 확인)
    public void confirmReservation(ReservationDto reservationDto) {
        // 1. 해당 상점 이름과 위치에 맞는 상점이 있어야 함
        if (!storeRepository.existsByNameAndLocation(reservationDto.getStoreName(), reservationDto.getStoreLocation())) {
            throw new NoStoreException();
        }

        // 2. 현재보다 이전 시간 안됨
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDto.getReservationDate(), reservationDto.getReservationTime());
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException();
        }

        // 2. 중복 예약 안됨
        if (reservationRepository
                .existsByStoreNameAndStoreLocationAndReservationDateAndReservationTime
                        (reservationDto.getStoreName()
                                , reservationDto.getStoreLocation()
                                , reservationDto.getReservationDate()
                                , reservationDto.getReservationTime())) {
            throw new AlreadyExistReservationException();
        }
    }

    // 예약 조회 (예약자 이름으로)
    @Transactional(readOnly = true)
    public List<ReservationDto> searchReservations(String name) {
        List<ReservationDto> reservationDtos;
        List<Reservation> reservations;

        String email = getCurrentUserEmail();

        if (!reservationRepository.existsByUserNameAndEmail(name, email)) { // 예약자 이름과 로그인한 이메일로 조회 시 예약정보 X 때
            throw new NoReservationException();
        }

        // 예약정보 조회 성공시
        reservations = reservationRepository.findByUserNameAndEmail(name, email);

        reservationDtos = reservations.stream()
                .map(e -> new ReservationDto(e.getStoreName(), e.getStoreLocation(), e.getUserName(),
                         e.getReservationDate(), e.getReservationTime(), e.getReservationNumber(), e.getStatus().toString()))
                .toList();
        return reservationDtos;
    }

    // 점장이 예약 조회 (날짜 별로 가능, 상점 이름만 입력 시 전체 예약 (날짜 순으로))
    @Transactional(readOnly = true)
    public List<ReservationDto> searchAdminReservations(String storeName, LocalDate date) {
        List<Reservation> reservations;
        if (date == null) {
            // 상점의 모든 예약 조회 (날짜 순으로)
            reservations = reservationRepository.findByStoreNameOrderByReservationDateAscReservationTimeAsc(storeName);
        } else {
            reservations = reservationRepository.findByStoreNameAndReservationDateOrderByReservationDateAscReservationTimeAsc(storeName, date);
        }
        return reservations.stream()
                .map(e -> new ReservationDto(
                        e.getStoreName(),
                        e.getStoreLocation(),
                        e.getUserName(),
                        e.getReservationDate(),
                        e.getReservationTime(),
                        e.getReservationNumber(),
                        e.getStatus().toString()))
                .toList();
    }


    // 예약 삭제
    @Transactional
    public void deleteReservation(String userName, String storeName) {
        List<Reservation> reservations;

        String email = getCurrentUserEmail();

        // 1. storeName null 일 때 -> 해당 이용자의 모든 예약 삭제
        if (storeName == null) {
            reservations = reservationRepository.findByUserNameAndEmail(userName, email);
            if (reservations.isEmpty()) {
                throw new NoReservationException();
            }
            reservationRepository.deleteAll(reservations);
        } else {
            // 2. storeName 으로 특정 예약 삭제
            reservations = reservationRepository.findByStoreNameAndUserNameAndEmail(storeName, userName, email);
            if (reservations.isEmpty()) {
                throw new NoReservationException();
            }
            reservationRepository.deleteAll(reservations);
        }
    }

    // 예약 승인
    @Transactional
    public void approveReservation(String reservationNumber) {
        Reservation reservation;
        reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new NoReservationException());
        reservation.approve();
        reservationRepository.save(reservation);
    }

    // 예약 거절
    @Transactional
    public void rejectReservation(String reservationNumber) {
        Reservation reservation;
        reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new NoReservationException());
        reservation.reject();
        reservationRepository.save(reservation);
    }

    // 방문 확인
    @Transactional
    public void confirmVisited(String reservationNumber, String userName) {
        Reservation reservation;
        reservation = reservationRepository.findByUserNameAndReservationNumber(userName, reservationNumber)
                .orElseThrow(() -> new NoReservationException());
        if (reservation.getStatus() != ReservationStatus.APPROVED) {
            throw new NotApprovedReservationException();
        } // 예약 승인 된 건에 한해서만 방문 확인 가능

        // 현재 시간이 예약시간 10분 전인지
        LocalDateTime reservationDateTime = LocalDateTime.of(reservation.getReservationDate(), reservation.getReservationTime());
        if (!reservationDateTime.isAfter(LocalDateTime.now().plusMinutes(10))) {
            throw new LateReservationException();
        }

        reservation.visited(); // 방문 확인 !!
        reservationRepository.save(reservation);
    }
}
