package com.springboot.ms.moviecatalogservice.controller;

import com.springboot.ms.moviecatalogservice.model.CatalogItem;
import com.springboot.ms.moviecatalogservice.model.Movie;
import com.springboot.ms.moviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient webClient;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating ratings = restTemplate
                .getForObject("http://ratings-data-service/ratingsData/users/" + userId, UserRating.class);

        return ratings
                .getUserRatingList()
                .stream()
                .map(rating -> {
                    Movie movie = restTemplate
                            .getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
                    return new CatalogItem(movie.getName(), rating.getStarRating());
                })
                .collect(Collectors.toList());
    }
}
