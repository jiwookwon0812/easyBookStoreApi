package zerobase.easybookservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zerobase.easybookservice.dto.ReviewDto;

@Entity(name = "Review")
@Getter
@ToString
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reservationNumber; // 예약 번호
    private String userName; // 예약자 이름
    private String email; // 이메일
    private String storeName; // 상점 이름
    private String reviewText; // 리뷰 내용
    private int rating; // 별점

    public Review(ReviewDto reviewDto, String email) {
        this.reservationNumber = reviewDto.getReservationNumber();
        this.userName = reviewDto.getUserName();
        this.email = email;
        this.storeName = reviewDto.getStoreName();
        this.reviewText = reviewDto.getReviewText();
        this.rating = reviewDto.getRating();
    }
}
