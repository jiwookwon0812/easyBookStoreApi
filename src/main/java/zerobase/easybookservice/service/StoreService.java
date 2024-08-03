package zerobase.easybookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.easybookservice.domain.Store;
import zerobase.easybookservice.dto.StoreDto;
import zerobase.easybookservice.exception.impl.AlreadyExistStoreException;
import zerobase.easybookservice.exception.impl.NoStoreException;
import zerobase.easybookservice.repository.StoreRepository;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    // 상점 등록
    @Transactional
    public StoreDto registerStore(StoreDto storeDto) {
        boolean exists = storeRepository.existsByNameAndLocation(storeDto.getName(), storeDto.getLocation());
        if (exists) {
            throw new AlreadyExistStoreException();
        }

        storeRepository.save(new Store(storeDto));
        return storeDto;
    }

    // 상점 삭제 (상점 이름이랑 장소로 -> 딱 하나만 존재하고 있음)
    @Transactional
    public void deleteStoreByName(String name, String location) {
        Store store = storeRepository.findByNameAndLocation(name, location)
                .orElseThrow(() -> new NoStoreException());
        storeRepository.delete(store);
    }

    // 상점 조회 (이름으로, 이름 없으면 전체 조회)
    @Transactional(readOnly = true)
    public List<StoreDto> searchStores(String name, String location) {
        List<StoreDto> storeDtos;
        List<Store> stores;
        if (name == null && location == null) { // 전체 상점 조회
            stores = storeRepository.findAll();
            if (stores.isEmpty()) {
                throw new NoStoreException();
            }
            storeDtos = stores.stream()
                    .map(e -> new StoreDto(e.getName(), e.getLocation(), e.getDescription()))
                    .toList();
            return  storeDtos;
        } else if (name == null && location != null) { // 이름 없고 위치만 입력 했을 때
            stores = storeRepository.findAllByLocation(location);
            if (stores.isEmpty()) {
                throw new NoStoreException();
            }
            storeDtos = stores.stream()
                    .map(e -> new StoreDto(e.getName(), e.getLocation(), e.getDescription()))
                    .toList();
            return storeDtos;
        } else if (name != null && location == null) { // 이름만 입력
            stores = storeRepository.findAllByName(name);
            if (stores.isEmpty()) {
                throw new NoStoreException();
            }
            storeDtos = stores.stream()
                    .map(e -> new StoreDto(e.getName(), e.getLocation(), e.getDescription()))
                    .toList();
            return storeDtos;
        } else { // 이름, 위치 모두 입력
            Optional<Store> store = storeRepository.findByNameAndLocation(name, location);
            if (store.isEmpty()) {
                throw new NoStoreException();
            }
            storeDtos = store.stream()
                    .map(e-> new StoreDto(e.getName(), e.getLocation(), e.getDescription()))
                    .toList();
            return storeDtos;
        }
    }


}
