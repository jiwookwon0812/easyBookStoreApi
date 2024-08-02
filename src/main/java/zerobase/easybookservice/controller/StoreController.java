package zerobase.easybookservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zerobase.easybookservice.dto.StoreDto;
import zerobase.easybookservice.service.StoreService;

import java.util.List;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    // partner 회원만 가능하게끔
    // 상점 등록
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public StoreDto registerStore(@RequestBody StoreDto storeDto) {
        return storeService.registerStore(storeDto);
    }

    // 상점 삭제 (이름과 장소 입력해서)
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void DeleteStore(@RequestParam String name,
                            @RequestParam String location) {
        storeService.deleteStoreByName(name, location);
    }

    // 상점 조회
    @GetMapping
    public List<StoreDto> searchStores(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) String location) {
        // 상점 이름 넣지 않으면 전체 상점을 조회한다.
        return storeService.searchStores(name, location);
    }


}
