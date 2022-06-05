package com.springboot.ms.moviecatalogservice.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CatalogItem {

    private String movieName;
    private int starRating;
}
