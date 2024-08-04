package zerobase.easybookservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {
    private String name; // 상점 이름
    private String location; // 상점 위치
    private String description; // 상점 상세 정보
}
