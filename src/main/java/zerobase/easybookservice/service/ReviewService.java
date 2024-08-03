package zerobase.easybookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.easybookservice.domain.Reservation;
import zerobase.easybookservice.domain.Review;
import zerobase.easybookservice.domain.constant.ReservationStatus;
import zerobase.easybookservice.dto.ReviewDto;
import zerobase.easybookservice.exception.impl.NoReservationException;
import zerobase.easybookservice.exception.impl.NotVisitedReservationException;
import zerobase.easybookservice.repository.ReservationRepository;
import zerobase.easybookservice.repository.ReviewRepository;

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
        String email = getCurrentUserEmail();
        reservation = reservationRepository.findByUserNameAndReservationNumber(userName, reservationNumber)
                .orElseThrow(() -> new NoReservationException());

        if (reservation.getStatus() != ReservationStatus.VISITED) {
            throw new NotVisitedReservationException();
        } // 방문 확인 후 리뷰 작성 가능

        Review review = new Review(reviewDto, email);
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
    @Transactional(readOnly = true)
    public List<ReviewDto> searchMyReviews(String storeName) {
        List<ReviewDto> reviewDtos;
        List<Review> reviews;
        String email = getCurrentUserEmail();

        if (storeName == null) {
            reviews = reviewRepository.findByEmail(email);
            reviewDtos = reviews.stream()
                    .map(e -> new ReviewDto(e.getReservationNumber(),
                            e.getUserName(),
                            e.getStoreName(),
                            e.getReviewText(),
                            e.getRating())).toList();
            return reviewDtos;
        } else {
            reviews = reviewRepository.findByStoreNameAndEmail(storeName, email);
            reviewDtos = reviews.stream().map(e -> new ReviewDto(e.getReservationNumber(),
                    e.getUserName(), e.getStoreName(), e.getReviewText(), e.getRating()))
                    .toList();
            return reviewDtos;
        }
    }

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }


    // 리뷰 삭제 <- 이것도 아이디로?
    public void deleteReview(String storeName) {
        String email = getCurrentUserEmail();
        List<Review> reviews = reviewRepository.findByStoreNameAndEmail(storeName, email);
        reviewRepository.deleteAll(reviews);
    }


}
