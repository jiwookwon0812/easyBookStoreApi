package zerobase.easybookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private String reservationNumber;
    private String userName;
    private String reviewText;
    private int rating;
}
