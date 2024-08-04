package zerobase.easybookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zerobase.easybookservice.dto.ReservationDto;
import zerobase.easybookservice.service.ReservationService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    // 상점 예약
    @Operation(summary = "상점 예약")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ReservationDto reserveStore(@Parameter(description = "예약 날짜 형식 : YYYY-MM-DD / 시간 형식 : HH:MM", example = "2024-08-12, 14:30", required = true)
            @RequestBody ReservationDto reservationDto) {
        return reservationService.reserveStore(reservationDto);
    }

    // 예약 조회 (예약 가능 여부는 service 코드 내에)
    @Operation(summary = "예약자 이름으로 예약 조회")
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public List<ReservationDto> searchReservations(@RequestParam String userName) {
        return reservationService.searchReservations(userName);
    }

    // (점장 권한) 예약 조회 -> 상점 이름으로 조회시 날짜별로 예약 리스트 뜨도록
    // (날짜 별로 가능, 상점 이름만 입력 시 전체 예약 (날짜 순으로))
    @Operation(summary = "점장 권한으로 날짜별 예약 리스트 조회")
    @GetMapping("/admin/{storeName}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReservationDto> searchAdminReservations(@PathVariable String storeName,
                                                        @RequestParam(required = false) LocalDate date) {
        return reservationService.searchAdminReservations(storeName, date);
    }

    // 예약 삭제
    @Operation(summary = "예약 삭제")
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public void DeleteReservation(@RequestParam String userName,
                                  @RequestParam(required = false) String storeName) {
        // storeName 없으면 그냥 예약자가 예약한 모든 예약 삭제
        reservationService.deleteReservation(userName, storeName);
    }

    // 예약 승인 (점장이) (예약 번호 입력해서)
    @Operation(summary = "(점장 권한) 들어온 예약 승인")
    @PutMapping("/approve/{reservationNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public void approveReservation(@PathVariable String reservationNumber) {
        reservationService.approveReservation(reservationNumber);
    }

    // 예약 거절
    @Operation(summary = "(점장 권한) 들어온 예약 거절")
    @PutMapping("/reject/{reservationNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public void rejectReservation(@PathVariable String reservationNumber) {
        reservationService.rejectReservation(reservationNumber);
    }

    // 방문 확인 api (예약 상태가 approve 인 상태일 때만 진행 가능)
    // 키오스크를 통해 진행되므로 USER 가 예약번호를 입력하도록
    // 예약시간 10분전 이후면 자동으로 확인 불가하게끔 예외처리. 관리자에게 직접 문의하도록 안내
    @Operation(summary = "(점장 권한) 승인된 예약건에 한하여 예약자 방문 확인")
    @PutMapping("/visited/{reservationNumber}")
    @PreAuthorize("hasRole('USER')")
    public void confirmVisited(@PathVariable String reservationNumber,
                               @RequestParam String userName) {
        reservationService.confirmVisited(reservationNumber, userName);
    }
}