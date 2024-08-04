package zerobase.easybookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private String reservationNumber; // 예약 번호
    private String userName; // 예약자 이름
    private String storeName; // 상점 이름
    private String reviewText; // 리뷰 내용
    private int rating; // 별점
}
