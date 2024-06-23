package com.example.samuraitravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;
import com.example.samuraitravel.service.HouseService;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {
	
	private final FavoriteService favoriteService;
	private final HouseService houseService;
	
	@Autowired
	public FavoriteController(FavoriteService favoriteService, HouseService houseService) {
		this.favoriteService = favoriteService;
		this.houseService = houseService;
	}
	
	@PostMapping("/add/{houseId}")
	@ResponseBody
	public ResponseEntity<String> addFavorite(@PathVariable Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		User user = userDetails.getUser();
		House house = houseService.findById(houseId);
		if (house != null) {
			favoriteService.addFavorite(user, house);
			return ResponseEntity.ok("added");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("House not found");
	}
	
    @PostMapping("/remove/{houseId}")
    @ResponseBody
    public ResponseEntity<String> removeFavorite(@PathVariable Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        House house = houseService.findById(houseId);
		if (house != null) {
			favoriteService.removeFavorite(user, house);
	        return ResponseEntity.ok("removed");
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("House not found");
	}

    @GetMapping("/houses/{houseId}")
    public String showHouse(@PathVariable Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) { 
    	House house = houseService.findById(houseId);
    	User user = userDetails.getUser();
    	boolean isFavorite = favoriteService.isFavorite(user, house);
    	
    	model.addAttribute("house", house);
    	model.addAttribute("isFavorite", isFavorite);
    	
    	return "houses/show";
    }

    @GetMapping
    public String listFavorites(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model, @PageableDefault(size = 10, sort = "id", direction = Direction.ASC)Pageable pageable) {
        User user = userDetails.getUser();
        Page<Favorite> favoritePage = favoriteService.getFavorites(user, pageable);
        model.addAttribute("favorites", favoritePage.getContent());
        model.addAttribute("favoritePage", favoritePage);
        
        return "favorites/list";
    }
}