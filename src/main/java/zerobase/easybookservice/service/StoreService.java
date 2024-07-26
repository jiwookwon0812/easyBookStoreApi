package zerobase.easybookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.easybookservice.domain.Reservation;
import zerobase.easybookservice.domain.Review;
import zerobase.easybookservice.domain.Store;
import zerobase.easybookservice.domain.constant.ReservationStatus;
import zerobase.easybookservice.dto.ReservationDto;
import zerobase.easybookservice.dto.ReviewDto;
import zerobase.easybookservice.dto.StoreDto;
import zerobase.easybookservice.repository.ReservationRepository;
import zerobase.easybookservice.repository.ReviewRepository;
import zerobase.easybookservice.repository.StoreRepository;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    // 상점 등록
    public StoreDto registerStore(StoreDto storeDto) {
        boolean exists = storeRepository.existsByNameAndLocation(storeDto.getName(), storeDto.getLocation());
        if (exists) {
            throw new RuntimeException("Store already exists");
        }

        storeRepository.save(new Store(storeDto));
        return storeDto;
    }

    // 상점 삭제 (상점 이름이랑 장소로 -> 딱 하나만 존재하고 있음)
    public void deleteStoreByName(String name, String location) {
        Store store = storeRepository.findByNameAndLocation(name, location)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        storeRepository.delete(store);
    }

    // 상점 조회 (이름으로, 이름 없으면 전체 조회)
    public List<StoreDto> searchStores(String name, String location) {
        List<StoreDto> storeDtos;
        List<Store> stores;
        if (name == null && location == null) { // 전체 상점 조회
            stores = storeRepository.findAll();
            if (stores.isEmpty()) {
                throw new RuntimeException("No stores found");
            }
            storeDtos = stores.stream()
                    .map(e -> new StoreDto(e.getName(), e.getLocation(), e.getDescription()))
                    .toList();
            return  storeDtos;
        } else if (name == null && location != null) { // 이름 없고 위치만 입력 했을 때
            stores = storeRepository.findAllByLocation(location);
            if (stores.isEmpty()) {
                throw new RuntimeException("No stores found");
            }
            storeDtos = stores.stream()
                    .map(e -> new StoreDto(e.getName(), e.getLocation(), e.getDescription()))
                    .toList();
            return storeDtos;
        } else if (name != null && location == null) { // 이름만 입력
            stores = storeRepository.findAllByName(name);
            if (stores.isEmpty()) {
                throw new RuntimeException("No stores found");
            }
            storeDtos = stores.stream()
                    .map(e -> new StoreDto(e.getName(), e.getLocation(), e.getDescription()))
                    .toList();
            return storeDtos;
        } else { // 이름, 위치 모두 입력
            Optional<Store> store = storeRepository.findByNameAndLocation(name, location);
            if (store.isEmpty()) {
                throw new RuntimeException("Store not found");
            }
            storeDtos = store.stream()
                    .map(e-> new StoreDto(e.getName(), e.getLocation(), e.getDescription()))
                    .toList();
            return storeDtos;
        }
    }

    // 상점 예약
    public ReservationDto reserveStore(ReservationDto reservationDto) {
        Reservation reservation;
        confirmReservation(reservationDto);
        // 예약 조건 만족 시
        reservation = new Reservation(reservationDto);
        reservation.generateReservationNumber();
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
    public void approveReservation(String reservationNumber) {
        Reservation reservation;
        reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.approve();
        reservationRepository.save(reservation);
    }

    // 예약 거절
    public void rejectReservation(String reservationNumber) {
        Reservation reservation;
        reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.reject();
        reservationRepository.save(reservation);
    }

    // 방문 확인
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

    // 리뷰 작성
    public ReviewDto writeReview(ReviewDto reviewDto) {
        Reservation reservation;

        String reservationNumber = reviewDto.getReservationNumber();
        String userName = reviewDto.getUserName();
        reservation = reservationRepository.findByUserNameAndReservationNumber(userName, reservationNumber)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.VISITED) {
            throw new RuntimeException("You can write review after visit store");
        } // 방문 확인 후 리뷰 작성 가능

        Review review = new Review(reviewDto);
        reviewRepository.save(review);
        return reviewDto;
    }

}
