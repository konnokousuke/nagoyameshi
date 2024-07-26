package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
	public Page<Store> findByStoreNameLike(String keyword, Pageable pageable);
	
	public Page<Store> findByStoreNameLikeOrDescriptionLikeOrderByCreatedAtDesc(String storeNameKeword, String descriptionKeyword, Pageable pageable);
	public Page<Store> findByStoreNameLikeOrDescriptionLikeOrderByPriceAsc(String storeNameKeword, String descriptionKeyword, Pageable pageable);
	public Page<Store> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);
	public Page<Store> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable);
	public Page<Store> findAllByOrderByCreatedAtDesc(Pageable pageable);
	public Page<Store> findAllByOrderByPriceAsc(Pageable pageable);
}
