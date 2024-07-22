package com.example.nagoyameshi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
	public Page<Store> findByStoreNameLike(String keyword, Pageable pageable);
	
	public Page<Store> findByStoreNameLikeOrDescriptionLike(String storeNameKeword, String descriptionKeyword, Pageable pageable);
	public Page<Store> findByPriceLessThanEqual(Integer price, Pageable pageable);
}
