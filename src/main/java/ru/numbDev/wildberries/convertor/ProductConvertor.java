package ru.numbDev.wildberries.convertor;

import org.springframework.stereotype.Component;
import ru.numbDev.wildberries.db.entity.ProductEntity;
import ru.numbDev.wildberries.db.entity.StatisticEntity;
import ru.numbDev.wildberries.POJO.json.ProductFront;
import ru.numbDev.wildberries.POJO.json.Statistic;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductConvertor {

    public List<ProductFront> entityToFront(List<ProductEntity> entities) {
        return entities
                .stream()
                .map(e ->
                        new ProductFront()
                                .setId(e.getId())
                                .setBrand(e.getBrand())
                                .setProductId(e.getProductId())
                                .setProductName(e.getProductName())
                                .addStatistics(entityToFrontStatistic(e.getStatistics()))
                )
                .collect(Collectors.toList());

    }

    public List<Statistic> entityToFrontStatistic(List<StatisticEntity> entities) {
        return entities
                .stream()
                .map(e ->
                        new Statistic()
                                .setId(e.getId())
//                                .setCountOfProductType(e.getCountOfProductType())
                                .setCountOfSales(e.getCountOfSales())
                                .setPrice(e.getPrice())
                                .setCreatedDate(e.getCreatedDate())
                )
                .collect(Collectors.toList());
    }
}
