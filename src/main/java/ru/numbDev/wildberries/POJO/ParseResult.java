package ru.numbDev.wildberries.POJO;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ParseResult {

    private long productId;
    private String name;
    private String brand;
    private long brandId;
    private long priceU;
    private long salePriceU;
    private int rating;
    private int salesResult;
    private Set<Long> nomenclatures = new HashSet<>();

    private String page;

    public ParseResult addNomenclatures(List<Long> nomenclatures) {
        this.nomenclatures.addAll(nomenclatures);
        return this;
    }

}
