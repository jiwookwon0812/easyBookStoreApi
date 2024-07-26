package zerobase.easybookservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
    @GeneratedValue
    private Long id;
    private String reservationNumber;
    private String userName;
    private String reviewText;
    private int rating; // 별점

    public Review(ReviewDto reviewDto) {
        this.reservationNumber = reviewDto.getReservationNumber();
        this.userName = reviewDto.getUserName();
        this.reviewText = reviewDto.getReviewText();
        this.rating = reviewDto.getRating();
    }
}
