package zerobase.easybookservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zerobase.easybookservice.dto.ReservationDto;
import zerobase.easybookservice.dto.ReviewDto;
import zerobase.easybookservice.dto.StoreDto;
import zerobase.easybookservice.service.StoreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final StoreService storeService;

    // partner 회원만 가능하게끔
    // 상점 등록
    @PostMapping("/store/register")
    public StoreDto registerStore(@RequestBody StoreDto storeDto) {
        return storeService.registerStore(storeDto);
    }

    // 상점 삭제 (이름과 장소 입력해서)
    @DeleteMapping("/delete/store")
    public void DeleteStore(@RequestParam String name,
                            @RequestParam String location) {
        storeService.deleteStoreByName(name, location);
    }

    // 상점 조회
    @GetMapping("/search/stores")
    public List<StoreDto> searchStores(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) String location) {
        // 상점 이름 넣지 않으면 전체 상점을 조회한다.
        return storeService.searchStores(name, location);
    }

    // 상점 예약
    @PostMapping("/reservation")
    public ReservationDto reserveStore(@RequestBody ReservationDto reservationDto) {
        return storeService.reserveStore(reservationDto);
    }

    // 예약 조회
    @GetMapping("/search/reservation")
    public List<ReservationDto> searchReservations(@RequestParam String userName,
                                                   @RequestParam String birth) {
        return storeService.searchReservations(userName, birth);
    }

    // 예약 삭제
    @DeleteMapping("/delete/reservation")
    public void DeleteReservation(@RequestParam String userName,
                                  @RequestParam String birth,
                                  @RequestParam(required = false) String storeName) {
        // storeName 없으면 그냥 예약자가 예약한 모든 예약 삭제
        storeService.deleteReservation(userName, birth, storeName);
    }

    // 예약 승인 (점장이) (예약 번호 입력해서)
    @PutMapping("/reservation/approve/{reservationNumber}")
    public void approveReservation(@PathVariable String reservationNumber) {
        storeService.approveReservation(reservationNumber);
    }
    

    // 예약 거절
    @PutMapping("/reservation/reject/{reservationNumber}")
    public void rejectReservation(@PathVariable String reservationNumber) {
        storeService.rejectReservation(reservationNumber);
    }

    // 방문 확인 api (예약 상태가 approve 인 상태일 때만 진행 가능) (고객이)
    @PutMapping("/reservation/visited/{reservationNumber}")
    public void confirmVisited(@PathVariable String reservationNumber,
                               @RequestParam String userName) {
        storeService.confirmVisited(reservationNumber, userName);
    }

    // 리뷰 작성 api (예약 상태가 visited 인 상태일 때만 리뷰 작성 가능)
    @PostMapping("/resrvation/review")
    public ReviewDto writeReview(@RequestBody ReviewDto reviewDto) {
        return storeService.writeReview(reviewDto);
    }

}
