package ru.numbDev.wildberries.POJO.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private Long id;
    private Long root;
    private String name;
    private String brand;
    private long brandId;
    private int priceU;
    private long salePriceU;
    private int rating;
}

//        "id": 28790068,
//        "name": "Игрушка антистресс",
//        "brand": "Попит",
//        "siteBrandId": 257786,
//        "brandId": 247786,
//        "priceU": 20000,
//        "price": 200,
//        "salePriceU": 14400,
//        "salePrice": 144,
//        "sale": 27,
//        "diffPrice": false,
//        "rating": 1,
//        "feedbacks": 1,
//        "pics": 1,
//        "isNew": false,
//        "colors": [],
//        "sizes": [
//        {
//        "name": "",
//        "origName": "0",
//        "rank": 0,
//        "optionId": 64760173
//        }
//        ],
//        "isAdult": false,
//        "isDigital": false,
//        "subjectId": 2547,
//        "root": 21180855
//        },
//        {