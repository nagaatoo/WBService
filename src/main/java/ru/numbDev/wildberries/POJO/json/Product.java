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
