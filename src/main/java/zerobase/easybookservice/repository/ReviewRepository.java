package zerobase.easybookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.easybookservice.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
