package com.springboot.ms.moviecatalogservice.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.springboot.ms.moviecatalogservice.models.CatalogItem;
import com.springboot.ms.moviecatalogservice.models.Movie;
import com.springboot.ms.moviecatalogservice.models.Rating;
import com.springboot.ms.moviecatalogservice.models.UserRating;
import com.springboot.ms.moviecatalogservice.services.MovieInfoService;
import com.springboot.ms.moviecatalogservice.services.UserRatingService;
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

    @Autowired
    MovieInfoService movieInfoService;

    @Autowired
    UserRatingService userRatingService;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
        UserRating ratings = getUserRating(userId);
        return ratings
                .getUserRatingList()
                .stream()
                .map(this::getCatalogItem)
                .collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    private CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), rating.getStarRating());
    }

    private CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie Not Found", rating.getStarRating());
    }

    @HystrixCommand(fallbackMethod = "getFallbackUserRating")
    private UserRating getUserRating(String userId) {
        return restTemplate.getForObject("http://ratings-data-service/ratingsData/users/" + userId, UserRating.class);
    }

    private UserRating getFallbackUserRating(@PathVariable("userId") String userId) {
        return new UserRating(userId, List.of(
                new Rating("0", 0)
        ));
    }
}
