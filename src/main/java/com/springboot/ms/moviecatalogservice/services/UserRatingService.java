package com.springboot.ms.moviecatalogservice.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.springboot.ms.moviecatalogservice.models.Rating;
import com.springboot.ms.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserRatingService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackUserRating",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    public UserRating getUserRating(String userId) {
        return restTemplate.getForObject("http://ratings-data-service/ratingsData/users/" + userId, UserRating.class);
    }

    public UserRating getFallbackUserRating(@PathVariable("userId") String userId) {
        return new UserRating(userId, List.of(
                new Rating("0", 0)
        ));
    }
}
