package com.springboot.ms.moviecatalogservice.controllers;

import com.springboot.ms.moviecatalogservice.models.CatalogItem;
import com.springboot.ms.moviecatalogservice.models.UserRating;
import com.springboot.ms.moviecatalogservice.services.MovieInfoService;
import com.springboot.ms.moviecatalogservice.services.UserRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

    @Autowired
    WebClient webClient;

    @Autowired
    MovieInfoService movieInfoService;

    @Autowired
    UserRatingService userRatingService;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        UserRating ratings = userRatingService.getUserRating(userId);
        return ratings
                .getUserRatingList()
                .stream()
                .map(rating -> movieInfoService.getCatalogItem(rating))
                .collect(Collectors.toList());
    }
}
