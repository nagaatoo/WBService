package ru.numbDev.wildberries.convertor;

import org.springframework.stereotype.Component;
import ru.numbDev.wildberries.POJO.json.HistoryMeta;
import ru.numbDev.wildberries.db.entity.MetaEntity;
import ru.numbDev.wildberries.db.entity.ProductEntity;
import ru.numbDev.wildberries.db.entity.StatisticEntity;
import ru.numbDev.wildberries.POJO.json.ProductFront;
import ru.numbDev.wildberries.POJO.json.Statistic;

import java.util.Comparator;
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
                                .setBrand(e.getMetaData()
                                        .stream()
                                        .max(Comparator.comparingInt(MetaEntity::getVersion))
                                        .orElseGet(MetaEntity::new)
                                        .getBrand()
                                )
                                .setProductId(e.getProductId())
                                .setProductName(e.getMetaData()
                                        .stream()
                                        .max(Comparator.comparingInt(MetaEntity::getVersion))
                                        .orElseGet(MetaEntity::new)
                                        .getProductName())
                                .addHistoryMeta(entityToFrontHistoryMeta(e.getMetaData()))
                                .addStatistics(entityToFrontStatistic(e.getStatistics()))
                )
                .collect(Collectors.toList());

    }

    private List<Statistic> entityToFrontStatistic(List<StatisticEntity> entities) {
        return entities
                .stream()
                .map(e ->
                        new Statistic()
                                .setId(e.getId())
                                .setCountOfSales(e.getCountOfSales())
                                .setPrice(e.getPrice())
                                .setCreatedDate(e.getCreatedDate())
                )
                .collect(Collectors.toList());
    }

    private List<HistoryMeta> entityToFrontHistoryMeta(List<MetaEntity> entities) {
        return entities
                .stream()
                .map(e ->
                        new HistoryMeta()
                                .setBrand(e.getBrand())
                                .setProductName(e.getProductName())
                                .setVersion(e.getVersion())
                )
                .collect(Collectors.toList());
    }
}
