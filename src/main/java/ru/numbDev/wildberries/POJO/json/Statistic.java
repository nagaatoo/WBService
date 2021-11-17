package ru.numbDev.wildberries.POJO.json;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Statistic {

    private Long id;
    private Integer price;
    private long countOfSales;
    private Integer countOfProductType;
    private Date createdDate;

}
