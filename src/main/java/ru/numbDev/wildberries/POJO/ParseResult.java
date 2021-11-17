package ru.numbDev.wildberries.POJO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
    private List<Long> nomenclatures = new ArrayList<>();

    private String page;

    public ParseResult addNomenclatures(List<Long> nomenclatures) {
        this.nomenclatures.addAll(nomenclatures);
        return this;
    }

}
