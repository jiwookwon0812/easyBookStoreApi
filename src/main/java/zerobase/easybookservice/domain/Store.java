package zerobase.easybookservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zerobase.easybookservice.dto.StoreDto;

@Entity (name = "Store")
@Getter
@ToString
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // 상점 이름
    private String location; // 상점 위치
    private String description; // 상점 세부 정보

    public Store(StoreDto storeDto) {
        this.name = storeDto.getName();
        this.location = storeDto.getLocation();
        this.description = storeDto.getDescription();
    }
}
