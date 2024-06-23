package com.example.samuraitravel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public void addFavorite(User user, House house) {
        // 既にお気に入りに追加されているか確認
        List<Favorite> existingFavorites = favoriteRepository.findByUserAndHouse(user, house);
        if (existingFavorites.isEmpty()) {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setHouse(house);
            favoriteRepository.save(favorite);
        }
    }

    public void removeFavorite(User user, House house) {
        List<Favorite> favorites = favoriteRepository.findByUserAndHouse(user, house);
        for (Favorite favorite : favorites) {
            favoriteRepository.delete(favorite);
        }
    }

    public boolean isFavorite(User user, House house) {
        return favoriteRepository.findByUserAndHouse(user, house).size() > 0;
    }

    public List<Favorite> getFavorites(User user) {
        return favoriteRepository.findByUser(user);
    }
    
    public Page<Favorite> getFavorites(User user, Pageable pageable) {
    	return favoriteRepository.findByUser(user, pageable);
    }
    
    
}