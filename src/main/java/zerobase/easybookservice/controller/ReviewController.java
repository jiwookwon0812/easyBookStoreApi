package zerobase.easybookservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zerobase.easybookservice.dto.ReviewDto;
import zerobase.easybookservice.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 작성 api (예약 상태가 visited 인 상태일 때만 리뷰 작성 가능)
    @PostMapping("/write")
    @PreAuthorize("hasRole('USER')")
    public ReviewDto writeReview(@RequestBody ReviewDto reviewDto) {
        return reviewService.writeReview(reviewDto);
    }

    // 리뷰 조회 api (특정 상점의)
    @GetMapping
    public List<ReviewDto> searchReviews(@RequestParam String storeName) {
        return reviewService.searchReviews(storeName);
    }

    // 내 리뷰 조회 api

    // 리뷰 삭제 api


}
