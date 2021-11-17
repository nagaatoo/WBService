package ru.numbDev.wildberries.POJO.json;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductFront {

    private Long id;
    private Long productId;
    private String productName;
    private String brand;
    private List<Statistic> statistics = new ArrayList<>();

    public ProductFront addStatistics(List<Statistic> st) {
        statistics.addAll(st);
        return this;
    }
}
