package zerobase.easybookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.easybookservice.domain.Review;

import java.awt.print.Book;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStoreName(String storeName);
}
