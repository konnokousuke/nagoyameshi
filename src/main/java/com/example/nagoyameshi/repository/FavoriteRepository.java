package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}
