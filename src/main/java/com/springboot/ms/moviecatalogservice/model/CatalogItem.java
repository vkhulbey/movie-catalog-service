package com.springboot.ms.moviecatalogservice.model;

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
