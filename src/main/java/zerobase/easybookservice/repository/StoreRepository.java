package zerobase.easybookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.easybookservice.domain.Store;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByNameAndLocation(String name, String location);
    // Optional : 단일 객체..
    // 리스트로 반환하고싶으면 List<>로

    List<Store> findAllByName(String name);
    List<Store> findAllByLocation(String location);

    boolean existsByNameAndLocation(String name, String location);
}
