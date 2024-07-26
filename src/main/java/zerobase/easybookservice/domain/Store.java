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
    private String name;
    private String location;
    private String description;

    public Store(StoreDto storeDto) {
        this.name = storeDto.getName();
        this.location = storeDto.getLocation();
        this.description = storeDto.getDescription();
    }
}
