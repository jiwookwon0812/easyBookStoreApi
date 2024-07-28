package zerobase.easybookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.easybookservice.domain.Reservation;
import zerobase.easybookservice.domain.Review;
import zerobase.easybookservice.domain.Store;
import zerobase.easybookservice.domain.constant.ReservationStatus;
import zerobase.easybookservice.dto.ReviewDto;
import zerobase.easybookservice.repository.ReservationRepository;
import zerobase.easybookservice.repository.ReviewRepository;
import zerobase.easybookservice.repository.StoreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    // 리뷰 작성
    @Transactional
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

    // 특성 상점 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewDto> searchReviews(String storeName) {
        List<ReviewDto> reviewDtos;
        List<Review> reviews;

        reviews = reviewRepository.findByStoreName(storeName);
        reviewDtos = reviews.stream().map(e -> new ReviewDto(e.getReservationNumber(),
                e.getUserName(), e.getStoreName(), e.getReviewText(), e.getRating()))
                .toList();
        return reviewDtos;
    }

    // 내 리뷰 조회 <- 아이디로?


    // 리뷰 삭제 <- 이것도 아이디로?

}
