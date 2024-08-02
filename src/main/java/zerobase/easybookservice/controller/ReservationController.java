package zerobase.easybookservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zerobase.easybookservice.dto.ReservationDto;
import zerobase.easybookservice.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    // 상점 예약
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ReservationDto reserveStore(@RequestBody ReservationDto reservationDto) {
        return reservationService.reserveStore(reservationDto);
    }

    // 예약 조회
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public List<ReservationDto> searchReservations(@RequestParam String userName,
                                                   @RequestParam String birth) {
        return reservationService.searchReservations(userName, birth);
    }

    // 예약 삭제
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public void DeleteReservation(@RequestParam String userName,
                                  @RequestParam String birth,
                                  @RequestParam(required = false) String storeName) {
        // storeName 없으면 그냥 예약자가 예약한 모든 예약 삭제
        reservationService.deleteReservation(userName, birth, storeName);
    }

    // 예약 승인 (점장이) (예약 번호 입력해서)
    @PutMapping("/approve/{reservationNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public void approveReservation(@PathVariable String reservationNumber) {
        reservationService.approveReservation(reservationNumber);
    }


    // 예약 거절
    @PutMapping("reject/{reservationNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public void rejectReservation(@PathVariable String reservationNumber) {
        reservationService.rejectReservation(reservationNumber);
    }

    // 방문 확인 api (예약 상태가 approve 인 상태일 때만 진행 가능) (고객이)
    @PutMapping("visited/{reservationNumber}")
    @PreAuthorize("hasRole('USER')")
    public void confirmVisited(@PathVariable String reservationNumber,
                               @RequestParam String userName) {
        reservationService.confirmVisited(reservationNumber, userName);
    }
}
